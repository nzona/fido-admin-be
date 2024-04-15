package it.be.fido.admin.utils;

import it.be.fido.admin.entities.ActivityEntity;
import it.be.fido.admin.entities.RoleEntity;
import it.be.fido.admin.entities.UserEntity;
import it.be.fido.admin.enumerations.ERole;
import it.be.fido.admin.repositories.ActivityRepository;
import it.be.fido.admin.repositories.RoleRepository;
import it.be.fido.admin.repositories.UserRepository;
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
    @Autowired
    private ActivityRepository activityRepository;

    @Transactional
    public void createRoles() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntityUser = new RoleEntity(ERole.ROLE_USER);
        RoleEntity roleEntityEmployee = new RoleEntity(ERole.ROLE_EMPLOYEE);
        RoleEntity roleEntityAdmin = new RoleEntity(ERole.ROLE_ADMIN);
        roleEntities.add(roleEntityUser);
        roleEntities.add(roleEntityEmployee);
        roleEntities.add(roleEntityAdmin);
        roleRepository.saveAll(roleEntities);
    }

    @Transactional
    public void clean() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
        activityRepository.deleteAll();
    }

    @Transactional
    public UserEntity createUserWithNoRole(String username, String email, String password) {
        UserEntity userEntity = new UserEntity(username, email, password);
        userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public UserEntity createUserWithRole(String username, String email, String password, ERole erole) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(erole);
        roleRepository.save(roleEntity);
        Set<RoleEntity> roleEntitySet = new HashSet<>();
        roleEntitySet.add(roleEntity);
        UserEntity userEntity = new UserEntity(username, email, password, roleEntitySet);
        userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public List<ActivityEntity> createActivities() {
        List<ActivityEntity> activityEntities = new ArrayList<>();
        ActivityEntity activityEntity = new ActivityEntity("agility", "agility");
        ActivityEntity activityEntity2 = new ActivityEntity("rally", "rally");
        activityEntities.add(activityEntity);
        activityEntities.add(activityEntity2);
        return activityRepository.saveAll(activityEntities);
    }

    @Transactional
    public List<ActivityEntity> getActivities() {
        return activityRepository.findAll();
    }
}
