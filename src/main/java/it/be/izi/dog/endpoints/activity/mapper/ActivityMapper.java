package it.be.izi.dog.endpoints.activity.mapper;

import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import it.be.izi.dog.entities.ActivityEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    List<ActivityDto> mapActivityEntities2ActivityDtos(List<ActivityEntity> activityEntities);

    ActivityDto mapActivityEntity2ActivityDto(ActivityEntity activityEntity);

    ActivityEntity mapActivityDto2ActivityEntity(ActivityDto activityDto);
}
