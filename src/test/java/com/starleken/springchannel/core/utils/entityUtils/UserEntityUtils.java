package com.starleken.springchannel.core.utils.entityUtils;

import com.starleken.springchannel.entity.UserEntity;

public class UserEntityUtils {

    public static UserEntity generateUser(){
        UserEntity user = new UserEntity();
        user.setLogin("Login#" +  Math.random() * 100);
        user.setPassword("Password#" +  Math.random() * 100);
        user.setImageURL("Image#" +  Math.random() * 100);
        user.setEmail("starleken"+ Math.random() * 100 +"@mail.ru");

        return user;
    }
}
