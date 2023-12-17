package com.starleken.springchannel.core.db;

import com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils.*;

@Component
public class UserDbHelper {

    private UserRepository userRepository;

    @Autowired
    public UserDbHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void clearDB(){
        userRepository.deleteAll();
    }

    public UserEntity saveUser(){
        return userRepository.save(generateUser());
    }

    public UserEntity saveUserByEmail(String email){
        UserEntity userToSave = generateUser();
        userToSave.setEmail(email);
        return userRepository.save(userToSave);
    }

    public Optional<UserEntity> findUserById(Long id){
        return userRepository.findById(id);
    }
}
