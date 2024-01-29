package com.starleken.authorizationserver.exception.token;

public class RefreshTokenIsIncorrectException extends RuntimeException{

    public RefreshTokenIsIncorrectException(String message) {
        super(message);
    }
}
