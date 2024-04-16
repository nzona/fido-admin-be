package it.be.izi.dog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.izi.dog.endpoints.auth.payload.request.LoginRequest;
import it.be.izi.dog.endpoints.auth.payload.response.JwtResponse;
import it.be.izi.dog.enumerations.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TokenUtil {
    @Autowired
    DbUtil dbUtil;

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectMapper mapper = new ObjectMapper();

    public String obtainAccessTokenAdminRole(MockMvc mockMvc) throws Exception {

        String criptedPassword = "$2a$10$TXArbLQiuXlNiQ4C2Wu0vOg245RgUc.YRCJ.vsK5l4AtelJvp4eGa";
        String clearPassword = "password";
        String username = "mrossi";
        dbUtil.createUserWithRole("mrossi", "mrossi@gmail.com", criptedPassword, ERole.ROLE_ADMIN);
        LoginRequest loginRequest = createLoginRequest(username, clearPassword);
        String jsonLoginRequest = objectWriter.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andReturn();
        JwtResponse jwtResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
        return jwtResponse.getToken();
    }

    public String obtainAccessTokenEmployeeRole(MockMvc mockMvc) throws Exception {

        String criptedPassword = "$2a$10$TXArbLQiuXlNiQ4C2Wu0vOg245RgUc.YRCJ.vsK5l4AtelJvp4eGa";
        String clearPassword = "password";
        String username = "mrossi";
        dbUtil.createUserWithRole("mrossi", "mrossi@gmail.com", criptedPassword, ERole.ROLE_EMPLOYEE);
        LoginRequest loginRequest = createLoginRequest(username, clearPassword);
        String jsonLoginRequest = objectWriter.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andReturn();
        JwtResponse jwtResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
        return jwtResponse.getToken();
    }

    public String obtainAccessTokeUserRole(MockMvc mockMvc) throws Exception {

        String criptedPassword = "$2a$10$TXArbLQiuXlNiQ4C2Wu0vOg245RgUc.YRCJ.vsK5l4AtelJvp4eGa";
        String clearPassword = "password";
        String username = "mrossi";
        dbUtil.createUserWithRole("mrossi", "mrossi@gmail.com", criptedPassword, ERole.ROLE_USER);
        LoginRequest loginRequest = createLoginRequest(username, clearPassword);
        String jsonLoginRequest = objectWriter.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andReturn();
        JwtResponse jwtResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
        return jwtResponse.getToken();
    }

    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }
}
