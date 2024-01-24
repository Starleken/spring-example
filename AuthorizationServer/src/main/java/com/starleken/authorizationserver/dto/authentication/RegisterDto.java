package com.starleken.authorizationserver.dto.authentication;

import lombok.Data;

@Data
public class RegisterDto {

    private String login;
    private String password;
}
