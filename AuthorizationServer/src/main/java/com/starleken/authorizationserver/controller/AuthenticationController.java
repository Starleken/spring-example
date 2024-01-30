package com.starleken.authorizationserver.controller;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.UserEntity;
import com.starleken.authorizationserver.service.AuthenticationService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/auth")
    public ResponseEntity<JwtResponse> authenticate(LoginDto loginDto){
        return new ResponseEntity<>(authenticationService
                .authenticate(loginDto), HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(String refreshToken){
        return new ResponseEntity<>(authenticationService
                .getNewAccessToken(refreshToken), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(String refreshToken){
        return new ResponseEntity<>(authenticationService
                .refresh(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> signup(RegisterDto registerDto){
        return new ResponseEntity<>(authenticationService
                .signup(registerDto), HttpStatus.CREATED);
    }
}
