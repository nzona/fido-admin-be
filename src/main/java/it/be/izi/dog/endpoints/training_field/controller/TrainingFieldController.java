package it.be.izi.dog.endpoints.training_field.controller;

import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import it.be.izi.dog.endpoints.training_field.dto.TrainingFieldDto;
import it.be.izi.dog.services.TrainingFieldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        List<TrainingFieldDto> trainingFieldDtos = trainingFieldService.getTrainingFieldDtos();
        return ResponseEntity.ok(trainingFieldDtos);
    }

    @GetMapping("/training-fields/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingFieldDto> getTrainingField(@Valid @PathVariable("id") Long id) throws Exception {
        TrainingFieldDto trainingFieldDto = trainingFieldService.getTrainingFieldDto(id);
        return ResponseEntity.ok(trainingFieldDto);
    }

    @PostMapping("/training-fields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingFieldDto> createTrainingField(@Valid @RequestBody TrainingFieldDto trainingFieldDtoRequest) throws Exception {
        TrainingFieldDto response = trainingFieldService.createOrUpdateTrainingFieldEntity(trainingFieldDtoRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/training-fields")
    public ResponseEntity<TrainingFieldDto> updateTrainingField(@Valid @RequestBody TrainingFieldDto trainingFieldDtoRequest) throws Exception {
        TrainingFieldDto response = trainingFieldService.createOrUpdateTrainingFieldEntity(trainingFieldDtoRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/training-fields/{id}")
    public ResponseEntity<HttpStatus> deleteTrainingField(@PathVariable("id") Long id) throws Exception {
        trainingFieldService.deleteTrainingFieldEntity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
