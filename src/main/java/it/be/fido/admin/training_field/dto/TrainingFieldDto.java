package it.be.fido.admin.training_field.dto;

import it.be.fido.admin.activity.dto.ActivityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingFieldDto {
    private Long id;
    private String name;
    private String detail;
    private ActivityDto activityDto;
}
