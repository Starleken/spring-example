package com.starleken.springchannel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Schema(example = "1", description = "User id")
    @NotNull(message = "id must be")
    private Long id;

    @Schema(example = "starleken@mail.ru", description = "must be unique")
    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email must be")
    private String email;

    @Schema(example = "http://image.url", description = "Link to the image file")
    private String imageUrl;
}
