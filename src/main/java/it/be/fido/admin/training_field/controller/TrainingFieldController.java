package it.be.fido.admin.training_field.controller;

import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.services.TrainingFieldService;
import it.be.fido.admin.training_field.dto.TrainingFieldDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TrainingFieldController {
    @Autowired
    TrainingFieldService trainingFieldService;

    @GetMapping("/training-fields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainingFieldDto>> getTrainingFields() throws Exception {
        List<TrainingFieldDto> trainingFieldDtos =  new ArrayList<>();
        return ResponseEntity.ok(trainingFieldDtos);
    }
}
