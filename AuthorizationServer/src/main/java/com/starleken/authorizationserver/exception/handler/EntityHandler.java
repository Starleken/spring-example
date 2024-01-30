package com.starleken.authorizationserver.exception.handler;

import com.starleken.authorizationserver.exception.EntityCredentialsAreTakenException;
import com.starleken.authorizationserver.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class EntityHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handler(EntityNotFoundException ex, WebRequest webRequest){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityCredentialsAreTakenException.class)
    public ResponseEntity<Object> handler(EntityCredentialsAreTakenException ex, WebRequest webRequest){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
