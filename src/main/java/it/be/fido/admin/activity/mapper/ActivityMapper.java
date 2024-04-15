package it.be.fido.admin.activity.mapper;

import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.entities.ActivityEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    List<ActivityDto> mapActivityEntities2ActivityDtos(List<ActivityEntity> activityEntities);

    ActivityDto mapActivityEntity2ActivityDto(ActivityEntity activityEntity);

    ActivityEntity mapActivityDto2ActivityEntity(ActivityDto activityDto);
}
