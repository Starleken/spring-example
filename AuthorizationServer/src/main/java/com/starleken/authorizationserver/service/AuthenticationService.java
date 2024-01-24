package com.starleken.authorizationserver.service;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.User;

public interface AuthenticationService {

    JwtResponse authenticate(LoginDto loginDto);

    User signup(RegisterDto registerDto);
}
