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
            log.info("AuthenticationService -> user is not found by login: " + loginDto.getLogin());
            throwAuthException();
        }

        log.info("Authenticated user by login: " + loginDto.getLogin());
        String accessToken = jwtService.generateAccessToken(finded.get());
        String refreshToken = jwtService.generateRefreshToken(finded.get());
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse getNewAccessToken(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)){
            log.info("AuthenticationService -> refresh token is invalid: " + refreshToken);
            throwRefreshTokenIsIncorrectException();
        }

        UserEntity findedUser = jwtService.getRefreshTokenSubject(refreshToken);

        String accessToken = jwtService.generateAccessToken(findedUser);

        log.info("Authentication service -> user by login:{} received new access token", findedUser.getLogin());
        return new JwtResponse(accessToken, null);
    }

    @Override
    public UserEntity signup(RegisterDto registerDto) {
        checkIfLoginIsTaken(registerDto.getLogin());

        UserEntity userEntity = userMapper.mapToEntity(registerDto);
        userRepository.save(userEntity);

        log.info("Authentication service -> new user signup by login: " + registerDto.getLogin());
        //TODO FullDto
        return userEntity;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)){
            log.info("AuthenticationService -> refresh token is invalid: " + refreshToken);
            throwRefreshTokenIsIncorrectException();
        }

        UserEntity findedUser = jwtService.getRefreshTokenSubject(refreshToken);

        String accessToken = jwtService.generateAccessToken(findedUser);
        String newRefreshToken = jwtService.generateRefreshToken(findedUser);

        log.info("AuthenticationService -> user by login:{} has updated tokens ", findedUser.getLogin());
        return new JwtResponse(accessToken, newRefreshToken);
    }

    private void checkIfLoginIsTaken(String login){
        Optional<UserEntity> finded = userRepository.findByLogin(login);
        if (finded.isPresent()){
            log.info("AuthenticationService -> login is taken: " + login);
            throwEntityCredentialsException(UserEntity.class, "login", login);
        }
    }
}
