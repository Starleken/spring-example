package com.starleken.authorizationserver.utils;

import com.starleken.authorizationserver.exception.AuthException;
import com.starleken.authorizationserver.exception.EntityCredentialsAreTakenException;
import com.starleken.authorizationserver.exception.token.RefreshTokenIsIncorrectException;

public abstract class ExceptionUtils {

    public static void throwEntityCredentialsException(Class clacc, String credentialName, String credentialValue){
        String exceptionText = clacc.getSimpleName() + credentialName +
                " is taken by value: " + credentialValue;
        throw new EntityCredentialsAreTakenException(exceptionText);
    }

    public static void throwAuthException(){
        String exceptionText = "Login or Password is taken";
        throw new AuthException(exceptionText);
    }

    public static void throwRefreshTokenIsIncorrectException(){
        String exceptionText = "Refresh token is incorrect";
        throw new RefreshTokenIsIncorrectException(exceptionText);
    }
}
