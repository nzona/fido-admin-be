package it.be.fido.admin.auth.dto;

import it.be.fido.admin.enumerations.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Integer id;
    private ERole name;
}
