package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.exception.IncorrectPasswordException;
import com.starleken.springchannel.mapper.UserMapper;
import com.starleken.springchannel.repository.UserRepository;
import com.starleken.springchannel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starleken.springchannel.utils.ExceptionUtils.*;
import static com.starleken.springchannel.utils.ExceptionUtils.throwEmailCredentialsException;

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

        List<UserFullDto> dtos = foundedUsers.stream()
                .map(userMapper::mapToFullDto)
                .collect(Collectors.toList());

        log.info("UserServiceImpl -> found {} users", dtos.size());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDto findById(Long id) {
        Optional<UserEntity> findedUser = userRepository.findById(id);

        if (findedUser.isEmpty()){
            log.info("UserServiceImpl -> findById: user is not found by id: {}", id);
            throwEntityNotFoundException(UserEntity.class, id);
        }

        UserFullDto userFullDto = userMapper.mapToFullDto(findedUser.get());

        log.info("UserServiceImpl -> findById: found user {} by id: {}", userFullDto, id);
        return userFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDto findByLogin(String login) {
        UserEntity findedUser = userRepository.findByLogin(login);

        if (findedUser == null){
            log.info("UserServiceImpl -> findByLogin: user is not found by login: {}", login);
            throwEntityNotFoundExceptionLogin(UserEntity.class, login);
        }

        UserFullDto userFullDto = userMapper.mapToFullDto(findedUser);

        log.info("UserServiceImpl -> findByLogin: user {} found by login: {}", userFullDto, login);
        return userFullDto;
    }

    @Override
    @Transactional
    public UserFullDto create(UserCreateDto dto) {
        UserEntity userToSave = userMapper.mapToEntity(dto);

        checkIfLoginExists(dto.getLogin(), userToSave.getId());
        checkIfEmailExists(dto.getEmail(), userToSave.getId());

        UserEntity savedUser = userRepository.save(userToSave);

        UserFullDto userFullDto = userMapper.mapToFullDto(savedUser);

        log.info("UserServiceImpl -> create: user saved: {}", userFullDto);
        return userFullDto;
    }

    @Override
    @Transactional
    public UserFullDto update(UserUpdateDto dto) {
        Optional<UserEntity> findedUser = userRepository.findById(dto.getId());

        if (findedUser.isEmpty()){
            throwEntityNotFoundException(UserEntity.class, dto.getId());
        }

        checkIfEmailExists(dto.getEmail(), dto.getId());

        UserEntity entityToUpdate = findedUser.get();
        entityToUpdate.setImageURL(dto.getImageUrl());
        entityToUpdate.setEmail(dto.getEmail());

        UserEntity updatedUser = userRepository.save(entityToUpdate);

        UserFullDto userFullDto = userMapper.mapToFullDto(updatedUser);

        log.info("UserServiceImpl -> update: user updated: {}", userFullDto);
        return userFullDto;
    }

    @Override
    @Transactional
    public UserFullDto changePassword(ChangePasswordDto dto) {
        Optional<UserEntity> findedUser = userRepository.findById(dto.getId());

        if (findedUser.isEmpty()){
            log.info("UserServiceImpl -> changePassword: User with id:{} not found", dto.getId());
            throwEntityNotFoundException(UserEntity.class, dto.getId());
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
            throwEntityNotFoundException(UserEntity.class, id);
        }

        userRepository.deleteById(id);
        log.info("UserServiceImpl -> deleteById: user deleted by id: {}", id);
    }

    private void checkIfLoginExists(String login, Long id){
        UserEntity foundUserByLogin = userRepository.findByLogin(login);
        if (foundUserByLogin != null && !foundUserByLogin.getId().equals(id)){
            throwLoginCredentialsException(login);
        }
    }

    private void checkIfEmailExists(String email, Long id){
        UserEntity foundUserByEmail = userRepository.findByEmail(email);
        if (foundUserByEmail != null && !foundUserByEmail.getId().equals(id)){
            throwEmailCredentialsException(email);
        }
    }
}
