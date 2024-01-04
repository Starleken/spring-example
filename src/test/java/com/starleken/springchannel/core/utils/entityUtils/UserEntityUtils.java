package com.starleken.springchannel.core.utils.entityUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.entity.UserEntity;
import org.apache.catalina.User;
import org.mapstruct.control.MappingControl;

public abstract class UserEntityUtils {

    public static UserEntity generateUser(){
        Faker faker = FakerUtils.FAKER;

        UserEntity user = new UserEntity();
        user.setLogin(faker.name().username());
        user.setPassword(faker.internet().password());
        user.setImageURL(faker.internet().image());
        user.setEmail(faker.internet().emailAddress());

        return user;
    }

    public static UserEntity generateUserWithId(){
        Faker faker = FakerUtils.FAKER;

        UserEntity user = generateUser();
        user.setId(faker.number().randomNumber());

        return user;
    }
}
