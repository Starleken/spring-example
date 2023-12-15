package com.starleken.springchannel.repository;

import com.starleken.springchannel.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.generateUser;

@SpringBootTest
public class UserRepositoryTest {
    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        UserEntity userEntity1 = generateUser();
        UserEntity userEntity2 = generateUser();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        //when
        List<UserEntity> findedUsers = userRepository.findAll();

        //then
        Assertions.assertNotNull(findedUsers);
        Assertions.assertEquals(findedUsers.size(), 2);
        Assertions.assertEquals(findedUsers.get(0).getLogin(), userEntity1.getLogin());
        Assertions.assertEquals(findedUsers.get(1).getLogin(), userEntity2.getLogin());
    }

    @Test
    void findByLogin_happyPath() {
        //given
        UserEntity userToSave = generateUser();

        UserEntity savedUser = userRepository.save(userToSave);

        //when
        UserEntity findedUser = userRepository.findByLogin(savedUser.getLogin());

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(savedUser.getId(), findedUser.getId());
    }

    @Test
    void create_happyPath(){
        //given
        UserEntity userEntity = generateUser();

        //when
        UserEntity savedUser = userRepository.save(userEntity);

        //then
        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    void update_happyPath() {
        //given
        UserEntity userToSave = generateUser();

        UserEntity savedUser = userRepository.save(userToSave);
        savedUser.setImageURL("xcasfas");

        //when
        UserEntity updatedUser = userRepository.save(savedUser);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(savedUser.getImageURL(), updatedUser.getImageURL());
    }

    @Test
    void delete_happyPath() {
        //given
        UserEntity userToSave = generateUser();
        UserEntity userToDelete = userRepository.save(userToSave);

        //when
        userRepository.deleteById(userToDelete.getId());
        Optional<UserEntity> userToCheck = userRepository.findById(userToDelete.getId());

        //then
        Assertions.assertTrue(userToCheck.isEmpty());
    }
}
