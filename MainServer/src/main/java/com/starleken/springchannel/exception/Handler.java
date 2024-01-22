package com.starleken.springchannel.exception;

import com.starleken.springchannel.exception.entityCredentials.EntityCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityField.EntityFieldIsTakenException;
import com.starleken.springchannel.exception.entityNotFound.EntityIsNotFoundException;
import com.starleken.springchannel.exception.server.ServerIsUnavailableException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class Handler {

    @ExceptionHandler(EntityIsNotFoundException.class)
    public ResponseEntity<Object> handler(EntityIsNotFoundException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityCredentialsAreTakenException.class)
    public ResponseEntity<Object> handler(EntityCredentialsAreTakenException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityFieldIsTakenException.class)
    public ResponseEntity<Object> handler(EntityFieldIsTakenException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handler(IncorrectPasswordException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handler(ConstraintViolationException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerIsUnavailableException.class)
    public ResponseEntity<Object> handler(ServerIsUnavailableException ex, WebRequest request){
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
