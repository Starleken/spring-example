package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.exception.IncorrectPasswordException;
import com.starleken.springchannel.exception.entityCredentials.UserCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.UserIsNotFoundException;
import com.starleken.springchannel.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.generateUser;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    private void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        UserEntity user1 = generateUser();
        UserEntity user2 = generateUser();

        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<UserFullDto> findedUsers = userService.findAll();

        //then
        Assertions.assertNotNull(findedUsers);
        Assertions.assertEquals(2, findedUsers.size());
    }

    @Test
    void findById_happyPath(){
        //given
        UserEntity user = generateUser();
        UserEntity savedUser = userRepository.save(user);

        //when
        UserFullDto findedUserDto = userService.findById(savedUser.getId());

        //then
        Assertions.assertNotNull(findedUserDto);
        Assertions.assertEquals(savedUser.getId(), findedUserDto.getId());
    }

    @Test
    void findById_whenNotFound() {
        //given
        boolean isNotFound = false;

        //when
        try{
            UserFullDto findedUser = userService.findById(1L);
        } catch(UserIsNotFoundException ex) {
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }

    @Test
    void findByLogin_happyPath() {
        //given
        UserEntity user = generateUser();
        UserEntity savedUser = userRepository.save(user);

        //when
        UserFullDto findedDto = userService.findByLogin(savedUser.getLogin());

        //then
        Assertions.assertNotNull(findedDto);
        Assertions.assertEquals(savedUser.getLogin(), findedDto.getLogin());
    }

    @Test
    void findByLogin_whenNotFound() {
        //given
        boolean isNotFound = false;

        //when
        try{
            UserFullDto findedUser = userService.findByLogin("Starleken");
        } catch(UserIsNotFoundException ex) {
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }

    @Test
    void create_happyPath() {
        //given
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("vzvzx");
        dto.setLogin("czx");
        dto.setPassword("bzzqa");
        dto.setImageURL("qweqw1");

        //when
        UserFullDto userFullDto = userService.create(dto);

        //then
        Assertions.assertNotNull(userFullDto);
        Assertions.assertNotNull(userFullDto.getId());
    }

    @Test
    void create_whenLoginExists() {
        //given
        boolean isSaved = true;

        UserCreateDto dto1 = new UserCreateDto();
        dto1.setEmail("dto1");
        dto1.setLogin("starleken");
        dto1.setPassword("dto1");
        dto1.setImageURL("dto1");

        UserCreateDto dto2 = new UserCreateDto();
        dto2.setEmail("dto2");
        dto2.setLogin("starleken");
        dto2.setPassword("dto2");
        dto2.setImageURL("dto2");

        userService.create(dto1);

        //when
        try{
            userService.create(dto2);
        } catch (UserCredentialsAreTakenException ex){
            isSaved = false;
        }

        Assertions.assertFalse(isSaved);
    }

    @Test
    void create_whenEmailExists() {
        //given
        boolean isSaved = true;

        UserCreateDto dto1 = new UserCreateDto();
        dto1.setEmail("starleken");
        dto1.setLogin("dto1");
        dto1.setPassword("dto1");
        dto1.setImageURL("dto1");

        UserCreateDto dto2 = new UserCreateDto();
        dto2.setEmail("starleken");
        dto2.setLogin("dto2");
        dto2.setPassword("dto2");
        dto2.setImageURL("dto2");

        userService.create(dto1);

        //when
        try{
            userService.create(dto2);
        } catch (UserCredentialsAreTakenException ex){
            isSaved = false;
        }

        Assertions.assertFalse(isSaved);
    }

    @Test
    void update_happyPath() {
        //given
        String newEmail = "starleken@mail.ru";
        String newImage = "http://newImage.jpg";

        UserEntity user = generateUser();
        UserEntity savedUser = userRepository.save(user);

        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(savedUser.getId());
        dto.setEmail(newEmail);
        dto.setImageUrl(newImage);

        //when
        UserFullDto updatedUser = userService.update(dto);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(newEmail, updatedUser.getEmail());
        Assertions.assertEquals(newImage, updatedUser.getImageURL());
    }

    @Test
    void update_whenEmailExists() {
        //given
        String newEmail = "starleken@mail.ru";
        String newImage = "http://newImage.jpg";

        UserEntity userToSave = generateUser();
        userToSave.setEmail("starleken");
        userRepository.save(userToSave);

        UserEntity user = generateUser();
        UserEntity savedUser = userRepository.save(user);

        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(savedUser.getId());
        dto.setEmail("starleken");
        dto.setImageUrl(newImage);

        boolean isUpdated = true;

        //when
        try{
            UserFullDto updatedUser = userService.update(dto);
        } catch(UserCredentialsAreTakenException ex){
            isUpdated = false;
        }

        //then
        Assertions.assertFalse(isUpdated);
    }

    @Test
    void changePassword_happyPath() {
        //given
        String oldPassword = "old";
        String newPassword = "new";

        UserEntity user = generateUser();
        user.setPassword(oldPassword);
        UserEntity savedUser = userRepository.save(user);

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(savedUser.getId());
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);

        //when
        userService.changePassword(dto);
        Optional<UserEntity> findedUser = userRepository.findById(dto.getId());
        
        //then
        Assertions.assertEquals(newPassword, findedUser.get().getPassword());
    }

    @Test
    void changePassword_whenNotFound() {
        //given
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(5L);
        dto.setOldPassword("123");
        dto.setNewPassword("152");

        boolean isNotFound = false;

        //when
        try{
            userService.changePassword(dto);
        } catch(UserIsNotFoundException ex){
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }

    @Test
    void changePassword_whenPasswordIsIncorrect() {
        //given
        String oldPassword = "old";
        String newPassword = "new";

        UserEntity user = generateUser();
        user.setPassword(oldPassword);
        UserEntity savedUser = userRepository.save(user);

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(savedUser.getId());
        dto.setOldPassword(oldPassword + " incorrect");
        dto.setNewPassword(newPassword);

        boolean isIncorrect = false;

        //when
        try{
            userService.changePassword(dto);
        } catch(IncorrectPasswordException ex){
            isIncorrect = true;
        }

        //then
        Assertions.assertTrue(isIncorrect);
    }

    @Test
    void deleteById_happyPath() {
        //given
        UserEntity user = generateUser();
        UserEntity savedUser = userRepository.save(user);

        //when
        userService.deleteById(savedUser.getId());

        Optional<UserEntity> findedUser = userRepository.findById(savedUser.getId());

        //then
        Assertions.assertTrue(findedUser.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        boolean isNotFound = false;

        //when
        try{
            userService.deleteById(1L);
        } catch(UserIsNotFoundException ex){
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }
}
