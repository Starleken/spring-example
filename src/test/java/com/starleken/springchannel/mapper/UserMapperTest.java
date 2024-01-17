package com.starleken.springchannel.mapper;

import com.starleken.springchannel.core.equals.UserEqualsUtils;
import com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls;
import com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.starleken.springchannel.core.equals.UserEqualsUtils.*;
import static com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls.generateUserCreateDto;
import static com.starleken.springchannel.core.utils.entityUtils.UserEntityUtils.generateUserWithId;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapToFullDto_happyPath() {
        //given
        UserEntity userToMap = generateUserWithId();

        //when
        UserFullDto mapped = userMapper.mapToFullDto(userToMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndFullDto(userToMap, mapped);
    }

    @Test
    void mapToEntityFromCreateDto_happyPath() {
        //given
        UserCreateDto createDto = generateUserCreateDto();

        //when
        UserEntity mapped = userMapper.mapToEntity(createDto);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndCreateDto(mapped, createDto);
    }
}
