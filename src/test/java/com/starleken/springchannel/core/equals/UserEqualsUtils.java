package com.starleken.springchannel.core.equals;

import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import org.junit.jupiter.api.Assertions;

public abstract class UserEqualsUtils {

    public static void EqualEntityAndFullDto(UserEntity entity, UserFullDto dto){
        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getLogin(), dto.getLogin());
        Assertions.assertEquals(entity.getEmail(), dto.getEmail());
        Assertions.assertEquals(entity.getImageURL(), dto.getImageURL());
    }

    public static void EqualEntityAndCreateDto(UserEntity entity, UserCreateDto dto){
        Assertions.assertEquals(dto.getLogin(), entity.getLogin());
        Assertions.assertEquals(dto.getPassword(), entity.getPassword());
        Assertions.assertEquals(dto.getEmail(), entity.getEmail());
        Assertions.assertEquals(dto.getImageURL(), entity.getImageURL());
    }

    public static void EqualFullDtoAndCreateDto(UserFullDto fullDto, UserCreateDto createDto) {
        Assertions.assertEquals(fullDto.getLogin(), createDto.getLogin());
        Assertions.assertEquals(fullDto.getEmail(), createDto.getEmail());
        Assertions.assertEquals(fullDto.getImageURL(), createDto.getImageURL());
    }

    public static void EqualFullDtoAndUpdateDto(UserFullDto fullDto, UserUpdateDto updateDto) {
        Assertions.assertEquals(fullDto.getId(), updateDto.getId());
        Assertions.assertEquals(fullDto.getEmail(), updateDto.getEmail());
        Assertions.assertEquals(fullDto.getImageURL(), updateDto.getImageUrl());
    }
}
