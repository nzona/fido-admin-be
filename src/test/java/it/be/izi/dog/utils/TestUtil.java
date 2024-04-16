package it.be.izi.dog.utils;

import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import it.be.izi.dog.endpoints.auth.payload.request.LoginRequest;
import it.be.izi.dog.endpoints.auth.payload.request.SignupRequest;
import it.be.izi.dog.common.payload.response.MessageResponse;
import it.be.izi.dog.endpoints.training_field.dto.TrainingFieldDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class TestUtil {
    public static MessageResponse getMessageREsponse(String message, int statusCode) {
        return new MessageResponse(message, statusCode);
    }
    public static SignupRequest createSignupRequest(Set<String> role) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("mrossi@gmail.com");
        signupRequest.setRole(role);
        signupRequest.setPassword("123456");
        signupRequest.setUsername("mario-rossi");
        return signupRequest;
    }

    public static LoginRequest createLoginRequest(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }

    public static ActivityDto createActivityDto(String name, String detail){
        return new ActivityDto(1L, name, detail);
    }

    public static List<ActivityDto> createActivityDtos() {
        List<ActivityDto> activityDtos = new ArrayList<>();
        ActivityDto activityDto = new ActivityDto(1L, "agility", "agility");
        ActivityDto activityDto2 = new ActivityDto(2L, "rally", "rally");
        activityDtos.add(activityDto);
        activityDtos.add(activityDto2);
        return activityDtos;
    }

    public static List<TrainingFieldDto> createTrainingFieldDtosResponse(){
        ActivityDto activityPiscina = new ActivityDto(1L, "piscina", null);
        List<TrainingFieldDto> trainingFieldDtos = new ArrayList<>();
        TrainingFieldDto trainingFieldPiscina = new TrainingFieldDto(1L, "Campo piscina", "campo da piscina", activityPiscina);
        TrainingFieldDto trainingFieldGeneric = new TrainingFieldDto(2L, "Campo 1", "campo multi attività", null);
        trainingFieldDtos.add(trainingFieldPiscina);
        trainingFieldDtos.add(trainingFieldGeneric);
        return trainingFieldDtos;
    }
}
