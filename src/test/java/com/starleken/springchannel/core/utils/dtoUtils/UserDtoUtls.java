package com.starleken.springchannel.core.utils.dtoUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import org.apache.catalina.User;

public abstract class UserDtoUtls {

    public static UserCreateDto generateUserCreateDto(){
        Faker faker = FakerUtils.FAKER;

        UserCreateDto dto = new UserCreateDto();

        dto.setLogin(faker.name().username());
        dto.setEmail(faker.internet().emailAddress());
        dto.setPassword(faker.internet().password());
        dto.setImageURL(faker.internet().image());

        return dto;
    }

    public static UserCreateDto generateInvalidUserCreateDto(){
        UserCreateDto createDto = generateUserCreateDto();
        createDto.setEmail("starlekenmail.ru");

        return createDto;
    }

    public static UserUpdateDto generateUserUpdateDto(Long id){
        Faker faker = FakerUtils.FAKER;

        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(id);
        dto.setEmail(faker.internet().emailAddress());
        dto.setImageUrl(faker.internet().image());

        return dto;
    }

    public static UserUpdateDto generateInvalidUserUpdateDto(Long id){
        UserUpdateDto dto = generateUserUpdateDto(id);
        dto.setEmail("starlekenmail.ru");

        return dto;
    }

    public static ChangePasswordDto generateChangePasswordDto(Long userId, String oldPassword){
        Faker faker = FakerUtils.FAKER;

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(userId);
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(faker.internet().password());

        return dto;
    }

}
