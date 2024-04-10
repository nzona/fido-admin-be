package it.be.fido.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.be.fido.admin.models.ERole;
import it.be.fido.admin.payload.request.LoginRequest;
import it.be.fido.admin.payload.request.SignupRequest;
import it.be.fido.admin.payload.response.JwtResponse;
import it.be.fido.admin.payload.response.MessageResponse;
import it.be.fido.admin.utils.DbUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class AuthControllerTest {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    DbUtil dbUtil;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        dbUtil.clean();
        dbUtil.createRoles();
    }

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

    private MessageResponse getMessageREsponse(String message, int statusCode) {
        return new MessageResponse(message, statusCode);
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

    private SignupRequest createSignupRequest(Set<String> role) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("mrossi@gmail.com");
        signupRequest.setRole(role);
        signupRequest.setPassword("123456");
        signupRequest.setUsername("mario-rossi");
        return signupRequest;
    }

    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }
}
