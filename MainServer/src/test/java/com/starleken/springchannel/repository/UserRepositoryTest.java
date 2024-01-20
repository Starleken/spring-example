package com.starleken.springchannel.repository;

import com.starleken.springchannel.core.db.UserDbHelper;
import com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls.*;
import static com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils.generateUser;

@SpringBootTest
public class UserRepositoryTest {
    private UserRepository userRepository;

    private UserDbHelper helper;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, UserDbHelper helper) {
        this.userRepository = userRepository;
        this.helper = helper;
    }

    @BeforeEach
    void setUp() {
        helper.clearDB();
    }

    @Test
    void findAll_happyPath() {
        //given
        UserEntity savedUser1 = helper.saveUser();
        UserEntity savedUser2 = helper.saveUser();

        //when
        List<UserEntity> findedUsers = userRepository.findAll();

        //then
        Assertions.assertNotNull(findedUsers);
        Assertions.assertEquals(2, findedUsers.size());
        Assertions.assertEquals(savedUser1.getLogin(), findedUsers.get(0).getLogin());
        Assertions.assertEquals(savedUser2.getLogin(), findedUsers.get(1).getLogin());
    }

    @Test
    void findByLogin_happyPath() {
        //given
        UserEntity savedUser = helper.saveUser();

        //when
        UserEntity findedUser = userRepository.findByLogin(savedUser.getLogin());

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(savedUser.getId(), findedUser.getId());
    }

    @Test
    void create_happyPath(){
        //given
        UserEntity userToSave = generateUser();

        //when
        UserEntity savedUser = userRepository.save(userToSave);

        //then
        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    void update_happyPath() {
        //given
        UserEntity savedUser = helper.saveUser();
        savedUser.setEmail("new email");

        //when
        UserEntity updatedUser = userRepository.save(savedUser);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(savedUser.getImageURL(), updatedUser.getImageURL());
    }

    @Test
    void delete_happyPath() {
        //given
        UserEntity savedUser = helper.saveUser();

        //when
        userRepository.deleteById(savedUser.getId());
        Optional<UserEntity> userToCheck = userRepository.findById(savedUser.getId());

        //then
        Assertions.assertTrue(userToCheck.isEmpty());
    }
}
