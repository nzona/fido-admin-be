package it.be.izi.dog.services;

import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import it.be.izi.dog.entities.ActivityEntity;
import it.be.izi.dog.entities.TrainingFieldEntity;
import it.be.izi.dog.repositories.TrainingFieldRepository;
import it.be.izi.dog.endpoints.training_field.dto.TrainingFieldDto;
import it.be.izi.dog.endpoints.training_field.mapper.TrainingFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingFieldService {
    @Autowired
    TrainingFieldRepository trainingFieldRepository;
    @Autowired
    TrainingFieldMapper trainingFieldMapper;

    @Transactional(readOnly = true)
    public List<TrainingFieldDto> getTrainingFieldDtos() throws Exception {
        try {
            List<TrainingFieldEntity> trainingFieldEntities = trainingFieldRepository.findAll();
            return trainingFieldMapper.mapTrainingFieldEntities2TrainingFieldDtos(trainingFieldEntities);
        } catch (Exception e) {
            String errorMessage = String.format("Error while searching the training fields");
            throw new Exception(errorMessage);
        }
    }

    @Transactional(readOnly = true)
    public TrainingFieldDto getTrainingFieldDto(Long id) throws Exception {
        try {
            String errorMessage = String.format("TrainingFieldEntity with id: %s not founded.", id);
            Optional<TrainingFieldEntity> trainingFieldEntity = Optional.ofNullable(trainingFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(errorMessage)));
            return trainingFieldMapper.mapTrainingFieldEntity2TrainingFieldDto(trainingFieldEntity.get());
        } catch (Exception e) {
            String errorMessage = String.format("Error while searching the training field with id %s", id);
            throw new Exception(errorMessage);
        }
    }

    @Transactional
    public TrainingFieldDto createOrUpdateTrainingFieldEntity(TrainingFieldDto trainingFieldDtoRequest) throws Exception {
        try {
            TrainingFieldEntity trainingFieldEntity = trainingFieldMapper.mapTrainingFieldDto2TrainingFieldEntity(trainingFieldDtoRequest);
            TrainingFieldEntity response = trainingFieldRepository.save(trainingFieldEntity);
            return trainingFieldMapper.mapTrainingFieldEntity2TrainingFieldDto(response);
        } catch (Exception e) {
            String errorMessage = String.format("Error while creating the activity %s", trainingFieldDtoRequest.getName());
            throw new Exception(errorMessage);
        }
    }

    @Transactional
    public void deleteTrainingFieldEntity(Long id) throws Exception {
        try {
            String errorMessage = String.format("Activity with id: %s not founded.", id);
            Optional<TrainingFieldEntity> trainingFieldEntity = Optional.ofNullable(trainingFieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(errorMessage)));
            trainingFieldRepository.deleteById(trainingFieldEntity.get().getId());
        } catch (Exception e) {
            String errorMessage = String.format("Error while deleting the training field with id %s", id);
            throw new Exception(errorMessage);
        }
    }
}
