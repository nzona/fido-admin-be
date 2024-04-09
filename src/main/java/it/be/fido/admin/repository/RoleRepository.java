package it.be.fido.admin.repository;

import java.util.Optional;

import it.be.fido.admin.models.ERole;
import it.be.fido.admin.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
