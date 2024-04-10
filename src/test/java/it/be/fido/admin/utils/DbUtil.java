package it.be.fido.admin.utils;

import it.be.fido.admin.models.ERole;
import it.be.fido.admin.models.Role;
import it.be.fido.admin.models.User;
import it.be.fido.admin.repository.RoleRepository;
import it.be.fido.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DbUtil {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createRoles() {
        List<Role> roles = new ArrayList<>();
        Role roleUser = new Role(ERole.ROLE_USER);
        Role roleEmployee = new Role(ERole.ROLE_EMPLOYEE);
        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roles.add(roleUser);
        roles.add(roleEmployee);
        roles.add(roleAdmin);
        roleRepository.saveAll(roles);
    }

    @Transactional
    public void clean() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    public User createUserWithNoRole(String username, String email, String password) {
        User user = new User(username, email, password);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User createUserWithRole(String username, String email, String password, ERole erole) {
        Role role = new Role();
        role.setName(erole);
        roleRepository.save(role);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        User user = new User(username, email, password, roleSet);
        userRepository.save(user);
        return user;
    }
}
