package com.starleken.authorizationserver.service.impl;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.User;
import com.starleken.authorizationserver.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public JwtResponse authenticate(LoginDto loginDto) {
        return null;
    }

    @Override
    public User signup(RegisterDto registerDto) {
        return null;
    }
}
