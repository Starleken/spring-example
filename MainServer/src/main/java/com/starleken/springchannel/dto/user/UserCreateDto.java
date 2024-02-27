package com.starleken.springchannel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateDto {
    @Schema(example = "Starleken", description = "must be unique")
    @NotBlank(message = "Login must be")
    private String login;

    @Schema(example = "12345")
    @NotBlank(message = "Password must be")
    private String password;

    @Schema(example = "http://image.jpg", description = "Link to the image file")
    private String imageURL;

    @Schema(example = "starleken@mail.ru", description = "must be unique")
    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email must be")
    private String email;
}
