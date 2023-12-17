package com.starleken.springchannel.core.utils.entityUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.entity.UserEntity;

public abstract class UserEntityUtils {

    public static UserEntity generateUser(){
        Faker faker = new Faker();

        UserEntity user = new UserEntity();
        user.setLogin(faker.name().username());
        user.setPassword(faker.internet().password());
        user.setImageURL(faker.internet().image());
        user.setEmail(faker.internet().emailAddress());

        return user;
    }
}
