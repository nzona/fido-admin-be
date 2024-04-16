package it.be.izi.dog.services;

import it.be.izi.dog.endpoints.auth.dto.RoleDto;
import it.be.izi.dog.endpoints.auth.mapper.AuthMapper;
import it.be.izi.dog.entities.RoleEntity;
import it.be.izi.dog.enumerations.ERole;
import it.be.izi.dog.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthMapper authMapper;

    @Transactional(readOnly = true)
    public RoleDto getRoleByName(ERole erole) {
        Optional<RoleEntity> roleEntity = Optional.ofNullable(roleRepository.findByName(erole).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        return authMapper.mapRoleEntity2RoleDto(roleEntity.get());
    }
}
