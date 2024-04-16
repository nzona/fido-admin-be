package it.be.izi.dog.endpoints.training_field.dto;

import it.be.izi.dog.endpoints.activity.dto.ActivityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingFieldDto {
    private Long id;
    private String name;
    private String detail;
    private ActivityDto activityDto;

    public TrainingFieldDto(Long id, String name, String detail, ActivityDto activityDto) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.activityDto = activityDto;
    }
}
