package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    public List<UserFullDto> findAll();

    public UserFullDto findById(Long id);

    public UserFullDto findByLogin(String login);

    public UserFullDto create(UserCreateDto dto);

    public UserFullDto update(UserUpdateDto dto);

    public UserFullDto changePassword(ChangePasswordDto dto);

    public void deleteById(Long id);
}
