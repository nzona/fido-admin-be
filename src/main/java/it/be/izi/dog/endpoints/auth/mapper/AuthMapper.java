package it.be.izi.dog.endpoints.auth.mapper;

import it.be.izi.dog.endpoints.auth.dto.RoleDto;
import it.be.izi.dog.endpoints.auth.dto.UserDto;
import it.be.izi.dog.entities.RoleEntity;
import it.be.izi.dog.entities.UserEntity;
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
