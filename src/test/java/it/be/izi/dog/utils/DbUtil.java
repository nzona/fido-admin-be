package it.be.izi.dog.utils;

import it.be.izi.dog.entities.ActivityEntity;
import it.be.izi.dog.entities.RoleEntity;
import it.be.izi.dog.entities.TrainingFieldEntity;
import it.be.izi.dog.entities.UserEntity;
import it.be.izi.dog.enumerations.ERole;
import it.be.izi.dog.repositories.ActivityRepository;
import it.be.izi.dog.repositories.RoleRepository;
import it.be.izi.dog.repositories.TrainingFieldRepository;
import it.be.izi.dog.repositories.UserRepository;
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
    @Autowired
    private TrainingFieldRepository trainingFieldRepository;

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
    public ActivityEntity createActivity(String name, String detail){
        return new ActivityEntity(name, detail);
    }

    @Transactional
    public TrainingFieldEntity createTrainingFieldEntity(String name, String detail, ActivityEntity activityEntity){
        return new TrainingFieldEntity(name, detail, activityEntity);
    }

    @Transactional
    public List<ActivityEntity> createActivities() {
        List<ActivityEntity> activityEntities = new ArrayList<>();
        ActivityEntity activityEntity = createActivity("agility", "agility");
        ActivityEntity activityEntity2 = createActivity("rally", "rally");
        activityEntities.add(activityEntity);
        activityEntities.add(activityEntity2);
        return activityRepository.saveAll(activityEntities);
    }

    @Transactional
    public List<TrainingFieldEntity> createTrainingFields(){
        ActivityEntity activityPiscina = createActivity("piscina", null);
        List<TrainingFieldEntity> trainingFieldEntities = new ArrayList<>();
        TrainingFieldEntity trainingFieldPiscina = createTrainingFieldEntity("Campo piscina", "campo da piscina", activityPiscina);
        TrainingFieldEntity trainingFieldGeneric = createTrainingFieldEntity("Campo 1", "campo multi attività", null);
        trainingFieldEntities.add(trainingFieldPiscina);
        trainingFieldEntities.add(trainingFieldGeneric);
        return trainingFieldRepository.saveAll(trainingFieldEntities);
    }

    @Transactional
    public List<ActivityEntity> getActivities() {
        return activityRepository.findAll();
    }

    @Transactional
    public List<TrainingFieldEntity> getTrainingFieldEntities() {
        return trainingFieldRepository.findAll();
    }
}
