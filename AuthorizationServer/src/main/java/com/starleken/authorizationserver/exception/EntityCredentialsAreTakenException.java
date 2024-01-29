package com.starleken.authorizationserver.exception;

public class EntityCredentialsAreTakenException extends RuntimeException {

    public EntityCredentialsAreTakenException(String message) {
        super(message);
    }
}
