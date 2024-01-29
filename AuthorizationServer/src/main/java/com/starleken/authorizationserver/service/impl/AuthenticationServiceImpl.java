package com.starleken.authorizationserver.service.impl;

import com.starleken.authorizationserver.dto.authentication.LoginDto;
import com.starleken.authorizationserver.dto.authentication.RegisterDto;
import com.starleken.authorizationserver.dto.jwt.JwtResponse;
import com.starleken.authorizationserver.entity.UserEntity;
import com.starleken.authorizationserver.exception.AuthException;
import com.starleken.authorizationserver.exception.EntityCredentialsAreTakenException;
import com.starleken.authorizationserver.mapper.UserMapper;
import com.starleken.authorizationserver.repository.UserRepository;
import com.starleken.authorizationserver.service.AuthenticationService;
import com.starleken.authorizationserver.service.JwtService;
import com.starleken.authorizationserver.utils.ExceptionUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.starleken.authorizationserver.utils.ExceptionUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final UserMapper userMapper;

    @Override
    public JwtResponse authenticate(LoginDto loginDto) {
        Optional<UserEntity> finded = userRepository.findByLoginAndPassword(
                loginDto.getLogin(), loginDto.getPassword());
        if (finded.isEmpty()){
            throwAuthException();
        }

        String accessToken = jwtService.generateAccessToken(finded.get());
        String refreshToken = jwtService.generateRefreshToken(finded.get());
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public UserEntity signup(RegisterDto registerDto) {
        checkIfLoginIsTaken(registerDto.getLogin());

        UserEntity userEntity = userMapper.mapToEntity(registerDto);
        userRepository.save(userEntity);

        //TODO FullDto
        return userEntity;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)){
            throwRefreshTokenIsIncorrectException();
        }

        Claims claims = jwtService.getRefreshClaims(refreshToken);
        String login = claims.getSubject();

        Optional<UserEntity> findedUser = userRepository.findByLogin(login);
        if (findedUser.isEmpty()){
            throwAuthException(); //TODO new exception
        }

        String accessToken = jwtService.generateAccessToken(findedUser.get());
        String newRefreshToken = jwtService.generateRefreshToken(findedUser.get());
        return new JwtResponse(accessToken, newRefreshToken);
    }

    private void checkIfLoginIsTaken(String login){
        Optional<UserEntity> finded = userRepository.findByLogin(login);
        if (finded.isPresent()){
            throwEntityCredentialsException(UserEntity.class, "login", login);
        }
    }
}
