package it.be.izi.dog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.izi.dog.endpoints.auth.payload.request.LoginRequest;
import it.be.izi.dog.endpoints.auth.payload.request.SignupRequest;
import it.be.izi.dog.endpoints.auth.payload.response.JwtResponse;
import it.be.izi.dog.common.payload.response.MessageResponse;
import it.be.izi.dog.enumerations.ERole;
import it.be.izi.dog.utils.DbUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Collections;
import static it.be.izi.dog.utils.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthControllerTest extends IzidogControllerTest {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    DbUtil dbUtil;
    @Autowired
    private MockMvc mockMvc;

    /*
     * creazione semplice di un utente che non richiede nessuna autorizzazione
     * */
    @Test
    public void registerUser_ok() throws Exception {
        SignupRequest signupRequest = createSignupRequest(Collections.singleton("employee"));
        String jsonSignupRequest = objectWriter.writeValueAsString(signupRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("User registered successfully!", 200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * creazione semplice di un utente con username già esistente
     * */
    @Test
    public void registerUser_withExistentUsername_ko() throws Exception {
        SignupRequest signupRequest = createSignupRequest(Collections.singleton("employee"));
        String jsonSignupRequest = objectWriter.writeValueAsString(signupRequest);
        dbUtil.createUserWithNoRole("mario-rossi", "test@gmail.com", "123456");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("Error: Username is already taken!", 400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * creazione semplice di un utente con email già esistente
     * */
    @Test
    public void registerUser_withExistentEmail_ko() throws Exception {
        SignupRequest signupRequest = createSignupRequest(Collections.singleton("employee"));
        String jsonSignupRequest = objectWriter.writeValueAsString(signupRequest);
        dbUtil.createUserWithNoRole("federico-rossi", "mrossi@gmail.com", "123456");
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("Error: Email is already in use!", 400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * creazione semplice di un utente con un ruolo non esistente
     * */
    @Test
    public void registerUser_withNonExistentRole_ko() throws Exception {
        //creo la request con un ruolo fittizio
        SignupRequest signupRequest = createSignupRequest(Collections.singleton("role"));
        String jsonSignupRequest = objectWriter.writeValueAsString(signupRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("Role must be admin, employee or user.", 400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * signin di un utente registrato correttamente
     * */
    @Test
    public void loginUser_ok() throws Exception {
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
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(jwtResponse.getEmail()).isEqualTo("mrossi@gmail.com");
        assertThat(jwtResponse.getToken()).isNotNull();
        assertThat(jwtResponse.getUsername()).isEqualTo("mrossi");
    }

    /*
     * signin di un utente registrato correttamente
     * */
    @Test
    public void loginUser_invalidUsername_ko() throws Exception {
        String criptedPassword = "$2a$10$TXArbLQiuXlNiQ4C2Wu0vOg245RgUc.YRCJ.vsK5l4AtelJvp4eGa";
        String clearPassword = "password";
        String username = "mross";
        dbUtil.createUserWithRole("mrossi", "mrossi@gmail.com", criptedPassword, ERole.ROLE_ADMIN);
        LoginRequest loginRequest = createLoginRequest(username, clearPassword);
        String jsonLoginRequest = objectWriter.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("Invalid username or password. Please retry.", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }

    /*
     * signin di un utente registrato correttamente
     * */
    @Test
    public void loginUser_invalidPassword_ko() throws Exception {
        String criptedPassword = "$2a$10$TXArbLQiuXlNiQ4C2Wu0vOg245RgUc.YRCJ.vsK5l4AtelJvp4eGa";
        String clearPassword = "wrongpassword";
        String username = "mrossi";
        dbUtil.createUserWithRole("mrossi", "mrossi@gmail.com", criptedPassword, ERole.ROLE_ADMIN);
        LoginRequest loginRequest = createLoginRequest(username, clearPassword);
        String jsonLoginRequest = objectWriter.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andReturn();
        MessageResponse messageResponseExpected = getMessageREsponse("Invalid username or password. Please retry.", 401);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(
                objectWriter.writeValueAsString(messageResponseExpected));
    }


}
