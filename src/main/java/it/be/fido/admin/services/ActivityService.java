package it.be.fido.admin.services;

import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.activity.mapper.ActivityMapper;
import it.be.fido.admin.entities.ActivityEntity;
import it.be.fido.admin.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    ActivityMapper activityMapper;

    @Transactional(readOnly = true)
    public List<ActivityDto> getActivityDtos() throws Exception {
        try {
            List<ActivityEntity> activityEntities = activityRepository.findAll();
            return activityMapper.mapActivityEntities2ActivityDtos(activityEntities);
        } catch (Exception e) {
            String errorMessage = String.format("Error while searching the activities");
            throw new Exception(errorMessage);
        }
    }

    @Transactional(readOnly = true)
    public ActivityDto getActivityDto(Long id) throws Exception {
        try {
            String errorMessage = String.format("Activity with id: %s not founded.", id);
            Optional<ActivityEntity> activityEntity = Optional.ofNullable(activityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(errorMessage)));
            return activityMapper.mapActivityEntity2ActivityDto(activityEntity.get());
        } catch (Exception e) {
            String errorMessage = String.format("Error while searching the activity with id %s", id);
            throw new Exception(errorMessage);
        }
    }

    @Transactional
    public ActivityDto createOrUpdateActivityEntity(ActivityDto activityDtoRequest) throws Exception {
        try {
            ActivityEntity activityEntity = activityMapper.mapActivityDto2ActivityEntity(activityDtoRequest);
            ActivityEntity response = activityRepository.save(activityEntity);
            return activityMapper.mapActivityEntity2ActivityDto(response);
        } catch (Exception e) {
            String errorMessage = String.format("Error while creating the activity %s", activityDtoRequest.getName());
            throw new Exception(errorMessage);
        }
    }

    @Transactional
    public void deleteActivityEntity(Long id) throws Exception {
        try {
            String errorMessage = String.format("Activity with id: %s not founded.", id);
            Optional<ActivityEntity> activityEntity = Optional.ofNullable(activityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(errorMessage)));
            activityRepository.deleteById(activityEntity.get().getId());
        } catch (Exception e) {
            String errorMessage = String.format("Error while deleting the activity with id %s", id);
            throw new Exception(errorMessage);
        }
    }
}
