package com.starleken.authorizationserver.service.impl;

import com.starleken.authorizationserver.entity.UserEntity;
import com.starleken.authorizationserver.repository.UserRepository;
import com.starleken.authorizationserver.service.JwtService;
import com.starleken.authorizationserver.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.starleken.authorizationserver.utils.ExceptionUtils.throwAuthException;
import static com.starleken.authorizationserver.utils.ExceptionUtils.throwEntityNotFoundException;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretAccessKey;
    private final SecretKey secretRefreshKey;

    private final UserRepository userRepository;

    public JwtServiceImpl(
            @Value("${jwt.secret.access}") String secretAccessKey,
            @Value("${jwt.secret.refresh}") String secretRefreshKey,
            @Autowired UserRepository userRepository
    ) {
        this.secretAccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
        this.secretRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
        this.userRepository = userRepository;
    }

    @Override
    public String generateAccessToken(UserEntity user) {
        Date expirationTime = DateUtils.getCurrentDateWithAdditionalMinutes(5);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expirationTime)
                .signWith(secretAccessKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserEntity user) {
        Date expirationTime = DateUtils.getCurrentDateWithAdditionalDays(30);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expirationTime)
                .signWith(secretRefreshKey)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, secretAccessKey);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, secretRefreshKey);
    }

    @Override
    public UserEntity getRefreshTokenSubject(String token) {
        Claims claims = getClaims(token, secretRefreshKey);
        String login = claims.getSubject();

        Optional<UserEntity> findedUser = userRepository.findByLogin(login);
        if (findedUser.isEmpty()){
            throwEntityNotFoundException(UserEntity.class, login);
        }

        return findedUser.get();
    }

    @Override
    public Claims getRefreshClaims(String token) {
        return getClaims(token, secretRefreshKey);
    }

    private Claims getClaims(String token, Key secret){
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private boolean validateToken(String token, SecretKey secretKey){
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJwt(token);

        return true;
    }
}
