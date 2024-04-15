package it.be.fido.admin.activity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto {
    private Long id;
    private String name;
    private String detail;

    public ActivityDto(Long id, String name, String detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
    }
}
