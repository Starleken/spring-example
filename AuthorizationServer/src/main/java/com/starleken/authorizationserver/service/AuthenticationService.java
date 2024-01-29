package com.starleken.authorizationserver.service;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.UserEntity;

public interface AuthenticationService {

    JwtResponse authenticate(LoginDto loginDto);

    UserEntity signup(RegisterDto registerDto);

    JwtResponse refresh(String refreshToken);
}
