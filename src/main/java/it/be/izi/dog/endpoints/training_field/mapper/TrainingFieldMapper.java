package it.be.izi.dog.endpoints.training_field.mapper;

import it.be.izi.dog.entities.TrainingFieldEntity;
import it.be.izi.dog.endpoints.training_field.dto.TrainingFieldDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingFieldMapper {
    List<TrainingFieldDto> mapTrainingFieldEntities2TrainingFieldDtos(List<TrainingFieldEntity> trainingFieldEntity);
    @Mapping(source = "activity", target = "activityDto")
    TrainingFieldDto mapTrainingFieldEntity2TrainingFieldDto(TrainingFieldEntity trainingFieldEntity);
    @Mapping(source = "activityDto", target = "activity")
    TrainingFieldEntity mapTrainingFieldDto2TrainingFieldEntity(TrainingFieldDto trainingFieldDto);
}
