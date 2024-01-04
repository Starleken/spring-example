package com.starleken.springchannel.utils;

import com.starleken.springchannel.exception.entityField.EntityFieldIsTakenException;
import com.starleken.springchannel.exception.entityCredentials.EntityCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.EntityIsNotFoundException;
import jakarta.persistence.EntityNotFoundException;

public abstract class ExceptionUtils {

    public static void throwEntityNotFoundException(Class clazz, Long id){
        String text = clazz.getSimpleName() + " by id: " + id + " is not found";
        throw new EntityIsNotFoundException(text);
    }

    public static void throwEntityNotFoundExceptionLogin(Class clazz, String login){
        String text = clazz.getSimpleName() + " by login: " + login + " is not found";
        throw new EntityIsNotFoundException(text);
    }

    public static void throwNameIsTakenException(String name){
        String text = "Name: " + name + " is taken";
        throw new EntityFieldIsTakenException(text);
    }

    public static void throwLoginCredentialsException(String login){
        String text = "Login: " + login + " is taken";
        throw new EntityCredentialsAreTakenException(text);
    }

    public static void throwEmailCredentialsException(String email){
        String text = "Email: " + email + " is taken";
        throw new EntityCredentialsAreTakenException(text);
    }
}
