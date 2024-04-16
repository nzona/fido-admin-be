package it.be.izi.dog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.izi.dog.common.payload.response.MessageResponse;
import it.be.izi.dog.endpoints.training_field.dto.TrainingFieldDto;
import it.be.izi.dog.entities.TrainingFieldEntity;
import it.be.izi.dog.utils.DbUtil;
import it.be.izi.dog.utils.TokenUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static it.be.izi.dog.utils.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingFieldControllerTest extends IzidogControllerTest {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    DbUtil dbUtil;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TokenUtil tokenUtil;

    /*
     * recupero di tutti i campi da training presenti nel db
     * */
    @Test
    public void getAllTrainingFields_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createTrainingFields();
        MvcResult mvcResult = mockMvc.perform(get("/api/training-fields")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        List<TrainingFieldDto> trainingFieldDtos = createTrainingFieldDtosResponse();
        // uso getContentAsString(StandardCharsets.UTF_8)) perchè altrimenti non riesce a gestire i caratteri speciali
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(trainingFieldDtos));
    }

    /*
     * tentativo di recupero di tutti i campi da training presenti nel db con utenza non admin
     * */
    @Test
    public void getAllTrainingFields_roleEmployee_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenEmployeeRole(mockMvc);
        MvcResult mvcResult = mockMvc.perform(get("/api/training-fields")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isUnauthorized())
                .andReturn();

        MessageResponse messageResponseExpected = getMessageREsponse("The user mrossi is not authorized to access the resources of the uri /api/training-fields", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * tentativo di recupero di tutti i campi da training presenti nel db con utenza non admin
     * */
    @Test
    public void getAllTrainingFields_roleUser_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokeUserRole(mockMvc);
        MvcResult mvcResult = mockMvc.perform(get("/api/training-fields")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isUnauthorized())
                .andReturn();

        MessageResponse messageResponseExpected = getMessageREsponse("The user mrossi is not authorized to access the resources of the uri /api/training-fields", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * recupero un campo da training tramite l'id passato in path
     * */
    @Test
    public void getTrainingField_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createTrainingFields();
        MvcResult mvcResult = mockMvc.perform(get("/api/training-fields/1")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        TrainingFieldDto trainingFieldDto = createTrainingFieldDtosResponse().get(0);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(trainingFieldDto));
    }

    /*
     * creazione di un campo da training
     * uso l'annotation transactional perchè ho bisogno di accedere a un oggetto dentro una entity
     * */
    @Test
    @Transactional
    public void createTrainingField_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        TrainingFieldDto trainingFieldDto = createTrainingFieldDtosResponse().get(0);
        mockMvc.perform(post("/api/training-fields")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(trainingFieldDto)))
                .andReturn();

        TrainingFieldEntity trainingFieldEntity = dbUtil.getTrainingFieldEntities().get(0);
        assertThat(trainingFieldDto.getName()).isEqualTo(trainingFieldEntity.getName());
        assertThat(trainingFieldDto.getDetail()).isEqualTo(trainingFieldEntity.getDetail());

        assertThat(trainingFieldDto.getActivityDto().getName()).isEqualTo(trainingFieldEntity.getActivity().getName());
        assertThat(trainingFieldDto.getActivityDto().getDetail()).isEqualTo(trainingFieldEntity.getActivity().getDetail());
    }

    /*
     * modifica di un campo da training
     * */
    @Test
    @Transactional
    public void updateTrainingField_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createTrainingFields();
        TrainingFieldDto trainingFieldDto = createTrainingFieldDtosResponse().get(0);
        trainingFieldDto.setDetail("new");
        trainingFieldDto.setName("new");
        trainingFieldDto.setActivityDto(null);
        mockMvc.perform(put("/api/training-fields")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(trainingFieldDto)))
                .andReturn();

        TrainingFieldEntity trainingFieldEntity = dbUtil.getTrainingFieldEntities().get(0);
        assertThat(trainingFieldDto.getName()).isEqualTo(trainingFieldEntity.getName());
        assertThat(trainingFieldDto.getDetail()).isEqualTo(trainingFieldEntity.getDetail());

        assertThat(trainingFieldEntity.getActivity()).isNull();
    }

    /*
     * modifica di un campo da training
     * */
    @Test
    @Transactional
    public void updateTrainingField_withValidActivity_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createTrainingFields();
        TrainingFieldDto trainingFieldDto = createTrainingFieldDtosResponse().get(0);
        trainingFieldDto.setActivityDto(createActivityDto("piste", "piste"));
        mockMvc.perform(put("/api/training-fields")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(trainingFieldDto)))
                .andReturn();

        TrainingFieldEntity trainingFieldEntity = dbUtil.getTrainingFieldEntities().get(0);
        assertThat(trainingFieldDto.getName()).isEqualTo(trainingFieldEntity.getName());
        assertThat(trainingFieldDto.getDetail()).isEqualTo(trainingFieldEntity.getDetail());

        assertThat("piste").isEqualTo(trainingFieldEntity.getActivity().getName());
        assertThat("piste").isEqualTo(trainingFieldEntity.getActivity().getDetail());
    }

    /*
     * elimino una attività tramite l'id passato in path
     * */
    @Test
    public void deleteById_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createTrainingFields();
        mockMvc.perform(delete("/api/training-fields/1")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isNoContent())
                .andReturn();

        TrainingFieldEntity trainingFieldEntity = dbUtil.getTrainingFieldEntities().get(0);
        assertThat("Campo 1").isEqualTo(trainingFieldEntity.getName());
        assertThat("campo multi attività").isEqualTo(trainingFieldEntity.getDetail());
    }
}
