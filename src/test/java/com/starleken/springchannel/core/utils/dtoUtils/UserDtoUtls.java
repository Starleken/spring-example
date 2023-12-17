package com.starleken.springchannel.core.utils.dtoUtils;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import org.apache.catalina.User;

public abstract class UserDtoUtls {

    public static UserCreateDto generateUserCreateDto(){
        UserCreateDto dto = new UserCreateDto();

        dto.setLogin("login: "+ Math.random() * 1000);
        dto.setEmail("starleken@mail.ru");
        dto.setPassword("1234");
        dto.setImageURL("http://image.jpg");

        return dto;
    }

    public static UserCreateDto generateInvalidUserCreateDto(){
        UserCreateDto dto = new UserCreateDto();

        dto.setLogin("login: "+ Math.random() * 1000);
        dto.setEmail("starlekenmail.ru");
        dto.setPassword("1234");
        dto.setImageURL("http://image.jpg");

        return dto;
    }

    public static UserUpdateDto generateUserUpdateDto(Long id){
        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(id);
        dto.setEmail("starleken@mail.ru");
        dto.setImageUrl("http://image.jpg");

        return dto;
    }

    public static UserUpdateDto generateInvalidUserUpdateDto(Long id){
        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(id);
        dto.setEmail("starlekenmail.ru");
        dto.setImageUrl("http://image.jpg");

        return dto;
    }

    public static ChangePasswordDto generateChangePasswordDto(Long userId, String oldPassword){
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(userId);
        dto.setOldPassword(oldPassword);
        dto.setNewPassword("starleken1");

        return dto;
    }

}
