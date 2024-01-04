package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.exception.IncorrectPasswordException;
import com.starleken.springchannel.exception.entityCredentials.EntityCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.EntityIsNotFoundException;
import com.starleken.springchannel.mapper.UserMapper;
import com.starleken.springchannel.repository.UserRepository;
import com.starleken.springchannel.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls.*;
import static com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils.generateUser;
import static com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils.generateUserWithId;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAll_happyPath() {
        //given
        UserEntity user1 = generateUser();
        UserEntity user2 = generateUser();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        //when
        List<UserFullDto> findedUsers = userService.findAll();

        //then
        Assertions.assertNotNull(findedUsers);
        Assertions.assertEquals(2, findedUsers.size());
    }

    @Test
    void findById_happyPath(){
        //given
        UserEntity user = generateUserWithId();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //when
        UserFullDto findedUserDto = userService.findById(user.getId());

        //then
        Assertions.assertNotNull(findedUserDto);
        Assertions.assertEquals(user.getId(), findedUserDto.getId());
    }

    @Test
    void findById_whenNotFound() {
        //given
        long idToSearch = 5L;
        when(userRepository.findById(idToSearch)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> userService.findById(idToSearch));
    }

    @Test
    void findByLogin_happyPath() {
        //given
        UserEntity user = generateUser();
        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        //when
        UserFullDto findedDto = userService.findByLogin(user.getLogin());

        //then
        Assertions.assertNotNull(findedDto);
        Assertions.assertEquals(user.getLogin(), findedDto.getLogin());
    }

    @Test
    void findByLogin_whenNotFound() {
        //given
        String loginToSearch = "Starleken";
        when(userRepository.findByLogin(loginToSearch)).thenReturn(null);

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> userService.findByLogin(loginToSearch));

        //then
    }

    @Test
    void create_happyPath() {
        //given
        UserCreateDto createDto = generateUserCreateDto();
        when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        UserFullDto userFullDto = userService.create(createDto);

        //then
        Assertions.assertNotNull(userFullDto);
    }

    @Test
    void create_whenLoginExists() {
        //given
        UserCreateDto createDto = generateUserCreateDto();
        UserEntity entityToReturn = generateUserWithId();

        when(userRepository.findByLogin(createDto.getLogin())).thenReturn(entityToReturn);

        //when
        Assertions.assertThrows(EntityCredentialsAreTakenException.class,
                () -> userService.create(createDto));

        //then
    }

    @Test
    void create_whenEmailExists() {
        //given
        UserCreateDto createDto = generateUserCreateDto();

        UserEntity userToReturn = generateUserWithId();
        userToReturn.setEmail(createDto.getEmail());

        when(userRepository.findByEmail(createDto.getEmail())).thenReturn(userToReturn);

        //when
        Assertions.assertThrows(EntityCredentialsAreTakenException.class,
                () -> userService.create(createDto));

        //then
    }

    @Test
    void update_happyPath() {
        //given
        UserEntity userToUpdate = generateUserWithId();

        when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        UserUpdateDto updateDto = generateUserUpdateDto(userToUpdate.getId());

        //when
        UserFullDto updatedUser = userService.update(updateDto);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updateDto.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(updateDto.getImageUrl(), updatedUser.getImageURL());
    }

    @Test
    void update_whenEmailExists() {
        //given
        UserEntity userToUpdate = generateUserWithId();
        UserEntity userToFindEmail = generateUserWithId();

        UserUpdateDto updateDto = generateUserUpdateDto(userToUpdate.getId());
        updateDto.setEmail(userToFindEmail.getEmail());

        when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.findByEmail(userToFindEmail.getEmail())).thenReturn(userToFindEmail);

        //when
        Assertions.assertThrows(EntityCredentialsAreTakenException.class,
                () -> userService.update(updateDto));

        //then
    }

    @Test
    void changePassword_happyPath() {
        //given
        UserEntity userToChange = generateUserWithId();

        ChangePasswordDto changePasswordDto = generateChangePasswordDto(
                userToChange.getId(), userToChange.getPassword());

        when(userRepository.findById(userToChange.getId())).thenReturn(Optional.of(userToChange));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        userService.changePassword(changePasswordDto);
        Optional<UserEntity> findedUser = userRepository.findById(changePasswordDto.getId());

        //then
        Assertions.assertEquals(changePasswordDto.getNewPassword(), findedUser.get().getPassword());
    }

    @Test
    void changePassword_whenNotFound() {
        //given
        ChangePasswordDto dto = generateChangePasswordDto(1l, "old password");

        when(userRepository.findById(dto.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> userService.changePassword(dto));

        //then
    }

    @Test
    void changePassword_whenPasswordIsIncorrect() {
        //given
        UserEntity userToReturn = generateUserWithId();
        ChangePasswordDto changePasswordDto = generateChangePasswordDto(
                userToReturn.getId(), "incorrect");

        when(userRepository.findById(userToReturn.getId())).thenReturn(Optional.of(userToReturn));

        //when
        Assertions.assertThrows(IncorrectPasswordException.class,
                () -> userService.changePassword(changePasswordDto));

        //then
    }

    @Test
    void deleteById_happyPath() {
        //given
        UserEntity userToReturn = generateUserWithId();

        when(userRepository.existsById(userToReturn.getId())).thenReturn(true);
        when(userRepository.findById(userToReturn.getId())).thenReturn(Optional.empty());

        //when
        userService.deleteById(userToReturn.getId());

        Optional<UserEntity> findedUser = userRepository.findById(userToReturn.getId());

        //then
        Assertions.assertTrue(findedUser.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        long idToDelete = 1L;

        when(userRepository.existsById(idToDelete)).thenReturn(false);

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> userService.deleteById(idToDelete));

        //then

    }
}
