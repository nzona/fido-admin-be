package it.be.fido.admin.services;

import it.be.fido.admin.models.ERole;
import it.be.fido.admin.models.Role;
import it.be.fido.admin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> getRoleByName(ERole erole) {
        return Optional.ofNullable(roleRepository.findByName(erole).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
    }
}
