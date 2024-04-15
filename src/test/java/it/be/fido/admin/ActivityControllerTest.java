package it.be.fido.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.common.payload.response.MessageResponse;
import it.be.fido.admin.entities.ActivityEntity;
import it.be.fido.admin.utils.DbUtil;
import it.be.fido.admin.utils.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static it.be.fido.admin.utils.TestUtil.getMessageREsponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActivityControllerTest {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    DbUtil dbUtil;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        dbUtil.clean();
        dbUtil.createRoles();
    }

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

        List<ActivityDto> activityDtoList = getActivityDtos();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(activityDtoList));
    }

    /*
     * provo a recuperare tutte le attività presenti nel db con utenza non admin
     * */
    @Test
    public void getAllActivities_roleEmployee_ko() throws Exception {
        String token = tokenUtil.obtainAccessTokenEmployeeRole(mockMvc);
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

        ActivityDto activityDto = getActivityDtos().get(0);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(activityDto));
    }

    /*
     * creazione di una nuova attività
     * */
    @Test
    public void createActivity_roleAdmin_ok() throws Exception {
        String token = tokenUtil.obtainAccessTokenAdminRole(mockMvc);
        ActivityDto activityDto = getActivityDtos().get(0);
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
        ActivityDto activityDto = getActivityDtos().get(1);
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

    private List<ActivityDto> getActivityDtos() {
        List<ActivityDto> activityDtos = new ArrayList<>();
        ActivityDto activityDto = new ActivityDto(1L, "agility", "agility");
        ActivityDto activityDto2 = new ActivityDto(2L, "rally", "rally");
        activityDtos.add(activityDto);
        activityDtos.add(activityDto2);
        return activityDtos;
    }
}
