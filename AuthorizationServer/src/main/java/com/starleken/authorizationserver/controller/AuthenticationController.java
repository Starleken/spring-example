package com.starleken.authorizationserver.controller;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.User;
import com.starleken.authorizationserver.service.AuthenticationService;
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

    private AuthenticationService authenticationService;

    @GetMapping("/auth")
    public ResponseEntity<JwtResponse> authenticate(LoginDto loginDto){
        return new ResponseEntity<>(authenticationService
                .authenticate(loginDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> signup(RegisterDto registerDto){
        return new ResponseEntity<>(authenticationService
                .signup(registerDto), HttpStatus.CREATED);
    }
}
