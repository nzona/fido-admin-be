package it.be.fido.admin.services;

import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.entities.ActivityEntity;
import it.be.fido.admin.entities.TrainingFieldEntity;
import it.be.fido.admin.training_field.dto.TrainingFieldDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingFieldService {
//    @Transactional(readOnly = true)
//    public List<TrainingFieldDto> getTrainingFieldDtos() throws Exception {
//        try {
//            List<TrainingFieldEntity> trainingFieldEntities = activityRepository.findAll();
//            return activityMapper.mapActivityEntities2ActivityDtos(activityEntities);
//        } catch (Exception e) {
//            String errorMessage = String.format("Error while searching the activities");
//            throw new Exception(errorMessage);
//        }
//    }
}
