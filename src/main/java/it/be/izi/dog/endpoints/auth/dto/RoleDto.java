package it.be.izi.dog.endpoints.auth.dto;

import it.be.izi.dog.enumerations.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Integer id;
    private ERole name;
}
