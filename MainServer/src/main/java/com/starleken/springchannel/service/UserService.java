package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    List<UserFullDto> findAll();

    UserFullDto findById(Long id);

    UserFullDto findByLogin(String login);

    UserFullDto create(UserCreateDto dto);

     UserFullDto update(UserUpdateDto dto);

     UserFullDto changePassword(ChangePasswordDto dto);

     void deleteById(Long id);
}
