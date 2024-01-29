package com.starleken.authorizationserver.mapper;

import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity mapToEntity(RegisterDto registerDto);
}
