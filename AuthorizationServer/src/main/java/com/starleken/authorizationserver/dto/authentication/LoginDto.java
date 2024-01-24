package com.starleken.authorizationserver.dto.authentication;

import lombok.Data;

@Data
public class LoginDto {

    private String login;
    private String password;
}
