package it.be.fido.admin.auth.mapper;

import it.be.fido.admin.auth.dto.RoleDto;
import it.be.fido.admin.auth.dto.UserDto;
import it.be.fido.admin.entities.RoleEntity;
import it.be.fido.admin.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    //ignoro l'id perchè in fase di creazione del dto non lo conosco ancora. l'id verrà creato in automatico durante l'inserimento nel db
    @Mapping(target = "id", ignore = true)
    UserDto createUserDto(String username, String email, String password, Set<RoleDto> roleDtos);

    RoleDto mapRoleEntity2RoleDto(RoleEntity roleEntity);

    @Mapping(source = "roleDtos", target = "roleEntities")
    UserEntity mapUserDto2UserEntity(UserDto userDto);
}
