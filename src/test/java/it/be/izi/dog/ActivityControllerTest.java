package it.be.izi.dog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import it.be.izi.dog.common.payload.response.MessageResponse;
import it.be.izi.dog.entities.ActivityEntity;
import it.be.izi.dog.utils.DbUtil;
import it.be.izi.dog.utils.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static it.be.izi.dog.utils.TestUtil.createActivityDtos;
import static it.be.izi.dog.utils.TestUtil.getMessageREsponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActivityControllerTest extends IzidogControllerTest {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    DbUtil dbUtil;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    private MockMvc mockMvc;

    /*
     * recupero di tutte le attività presenti nel db
     * */
    @Test
    public void getAllActivities_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createActivities();
        MvcResult mvcResult = mockMvc.perform(get("/api/activities")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        List<ActivityDto> activityDtoList = createActivityDtos();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(activityDtoList));
    }

    /*
     * provo a recuperare tutte le attività presenti nel db con utenza non admin
     * */
    @Test
    public void getAllActivities_roleEmployee_ko() throws Exception {
        String token = tokenUtil.obtainAccessTokenEmployeeRole(mockMvc);
        MvcResult mvcResult = mockMvc.perform(get("/api/activities")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isUnauthorized())
                .andReturn();

        MessageResponse messageResponseExpected = getMessageREsponse("The user mrossi is not authorized to access the resources of the uri /api/activities", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * provo a recuperare tutte le attività presenti nel db con utenza non admin
     * */
    @Test
    public void getAllActivities_roleUser_ko() throws Exception {
        String token = tokenUtil.obtainAccessTokeUserRole(mockMvc);
        dbUtil.createActivities();
        MvcResult mvcResult = mockMvc.perform(get("/api/activities")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isUnauthorized())
                .andReturn();

        MessageResponse messageResponseExpected = getMessageREsponse("The user mrossi is not authorized to access the resources of the uri /api/activities", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * recupero una attività tramite l'id passato in path
     * */
    @Test
    public void getActivitiesById_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createActivities();
        MvcResult mvcResult = mockMvc.perform(get("/api/activities/1")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        ActivityDto activityDto = createActivityDtos().get(0);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(activityDto));
    }

    /*
     * creazione di una nuova attività
     * */
    @Test
    public void createActivity_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        ActivityDto activityDto = createActivityDtos().get(0);
        mockMvc.perform(post("/api/activities")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(activityDto)))
                .andReturn();

        ActivityEntity activityEntity = dbUtil.getActivities().get(0);
        assertThat(activityDto.getName()).isEqualTo(activityEntity.getName());
        assertThat(activityDto.getDetail()).isEqualTo(activityEntity.getDetail());
    }

    /*
     * modifica di una attività esistente
     * */
    @Test
    public void updateActivity_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createActivities();
        ActivityDto activityDto = createActivityDtos().get(1);
        activityDto.setDetail("new");
        activityDto.setName("new");
        mockMvc.perform(put("/api/activities")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(activityDto)))
                .andReturn();

        ActivityEntity activityEntity = dbUtil.getActivities().get(1);
        assertThat(activityDto.getName()).isEqualTo(activityEntity.getName());
        assertThat(activityDto.getDetail()).isEqualTo(activityEntity.getDetail());
    }

    /*
     * elimino una attività tramite l'id passato in path
     * */
    @Test
    public void deleteById_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        dbUtil.createActivities();
        mockMvc.perform(delete("/api/activities/1")
                        .header("authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isNoContent())
                .andReturn();

        ActivityEntity activityEntity = dbUtil.getActivities().get(0);
        assertThat("rally").isEqualTo(activityEntity.getName());
        assertThat("rally").isEqualTo(activityEntity.getDetail());
    }
}
