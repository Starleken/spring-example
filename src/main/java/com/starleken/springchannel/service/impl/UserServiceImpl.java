package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.constants.exceptionConstants.UserExceptionConstants;
import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.exception.IncorrectPasswordException;
import com.starleken.springchannel.exception.entityCredentials.UserCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.UserIsNotFoundException;
import com.starleken.springchannel.mapper.UserMapper;
import com.starleken.springchannel.repository.UserRepository;
import com.starleken.springchannel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starleken.springchannel.constants.exceptionConstants.UserExceptionConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserFullDto> findAll() {
        List<UserEntity> foundedUsers = userRepository.findAll();

        List<UserFullDto> dtoList = foundedUsers.stream()
                .map(userMapper::mapToFullDto)
                .collect(Collectors.toList());

        log.info("UserServiceImpl -> found {} users", dtoList.size());
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDto findById(Long id) {
        Optional<UserEntity> findedUser = userRepository.findById(id);

        if (findedUser.isEmpty()){
            log.info("UserServiceImpl -> findById: user is not found by id: {}", id);
            throw new UserIsNotFoundException(getNotFoundText(id));
        }

        log.info("UserServiceImpl -> findById: found user {} by id: {}", findedUser.get(), id);
        return userMapper.mapToFullDto(findedUser.get());
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDto findByLogin(String login) {
        UserEntity findedUser = userRepository.findByLogin(login);

        if (findedUser == null){
            log.info("UserServiceImpl -> findByLogin: user is not found by login: {}", login);
            throw new UserIsNotFoundException(getNotFoundTextByLogin(login));
        }

        log.info("UserServiceImpl -> findByLogin: user {} found by login: {}",findedUser, login);
        return userMapper.mapToFullDto(findedUser);
    }

    @Override
    @Transactional
    public UserFullDto create(UserCreateDto dto) {
        UserEntity userToSave = userMapper.mapToEntity(dto);

        checkIfLoginExists(dto.getLogin(), userToSave.getId());
        checkIfEmailExists(dto.getEmail(), userToSave.getId());

        UserEntity savedUser = userRepository.save(userToSave);

        log.info("UserServiceImpl -> create: user saved: {}", savedUser);
        return userMapper.mapToFullDto(savedUser);
    }

    @Override
    @Transactional
    public UserFullDto update(UserUpdateDto dto) {
        Optional<UserEntity> findedUser = userRepository.findById(dto.getId());

        if (findedUser.isEmpty()){
            throw new UserIsNotFoundException(getNotFoundText(dto.getId()));
        }

        checkIfEmailExists(dto.getEmail(), dto.getId());

        UserEntity entityToUpdate = findedUser.get();
        entityToUpdate.setImageURL(dto.getImageUrl());
        entityToUpdate.setEmail(dto.getEmail());

        UserEntity updatedUser = userRepository.save(entityToUpdate);

        log.info("UserServiceImpl -> update: user updated: {}", updatedUser);
        return userMapper.mapToFullDto(updatedUser);
    }

    @Override
    public UserFullDto changePassword(ChangePasswordDto dto) {
        Optional<UserEntity> findedUser = userRepository.findById(dto.getId());

        if (findedUser.isEmpty()){
            log.info("UserServiceImpl -> changePassword: User with id:{} not found", dto.getId());
            throw new UserIsNotFoundException(UserExceptionConstants.getNotFoundText(dto.getId()));
        }

        UserEntity userToUpdate = findedUser.get();
        if (!userToUpdate.getPassword().equals(dto.getOldPassword())){
            log.info("UserServiceImpl -> changePassword: Incorrect password for user with id: {}", dto.getId());
            throw new IncorrectPasswordException("Incorrect password!");
        }

        userToUpdate.setPassword(dto.getNewPassword());

        UserEntity updatedUser = userRepository.save(userToUpdate);
        log.info("UserServiceImpl -> changePassword: password for id:{} changed", updatedUser.getId());
        return userMapper.mapToFullDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)){
            log.info("UserServiceImpl -> deleteById: user is not found by id: {}", id);
            throw new UserIsNotFoundException(getNotFoundText(id));
        }

        userRepository.deleteById(id);
        log.info("UserServiceImpl -> deleteById: user deleted by id: {}", id);
    }

    private void checkIfLoginExists(String login, Long id){
        UserEntity foundUserByLogin = userRepository.findByLogin(login);
        if (foundUserByLogin != null && !foundUserByLogin.getId().equals(id)){
            throw new UserCredentialsAreTakenException(getLoginCredentialsText());
        }
    }

    private void checkIfEmailExists(String email, Long id){
        UserEntity foundUserByEmail = userRepository.findByEmail(email);
        if (foundUserByEmail != null && !foundUserByEmail.getId().equals(id)){
            throw new UserCredentialsAreTakenException(getEmailCredentialsText());
        }
    }
}
