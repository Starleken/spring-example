package com.starleken.authorizationserver.service;

import com.starleken.authorizationserver.entity.UserEntity;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(UserEntity user);
    String generateRefreshToken(UserEntity user);

    boolean validateAccessToken(String token);
    boolean validateRefreshToken(String token);

    Claims getRefreshClaims(String token);
}
