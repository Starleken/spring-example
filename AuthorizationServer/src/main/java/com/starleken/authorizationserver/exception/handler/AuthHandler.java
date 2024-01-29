package com.starleken.authorizationserver.exception.handler;

import com.starleken.authorizationserver.exception.AuthException;
import com.starleken.authorizationserver.exception.token.RefreshTokenIsIncorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AuthHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handler(AuthException ex, WebRequest webRequest){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenIsIncorrectException.class)
    public ResponseEntity<Object> handler(RefreshTokenIsIncorrectException ex, WebRequest webRequest){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
