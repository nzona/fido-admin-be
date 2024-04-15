package it.be.fido.admin.services;

import it.be.fido.admin.auth.dto.RoleDto;
import it.be.fido.admin.auth.mapper.AuthMapper;
import it.be.fido.admin.entities.RoleEntity;
import it.be.fido.admin.enumerations.ERole;
import it.be.fido.admin.repositories.RoleRepository;
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
