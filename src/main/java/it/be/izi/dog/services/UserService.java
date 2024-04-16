package it.be.izi.dog.services;

import it.be.izi.dog.endpoints.auth.dto.UserDto;
import it.be.izi.dog.endpoints.auth.mapper.AuthMapper;
import it.be.izi.dog.entities.UserEntity;
import it.be.izi.dog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthMapper authMapper;

    @Transactional(readOnly = true)
    public Boolean isExistentUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Boolean isExistentEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void saveUser(UserDto userDto) {
        UserEntity userEntity = authMapper.mapUserDto2UserEntity(userDto);
        userRepository.save(userEntity);
    }

}
