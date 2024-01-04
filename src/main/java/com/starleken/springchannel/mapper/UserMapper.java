package com.starleken.springchannel.mapper;

import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserFullDto mapToFullDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    UserEntity mapToEntity(UserCreateDto dto);
}
