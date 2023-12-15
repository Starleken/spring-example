package com.starleken.springchannel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Schema(example = "1", description = "User id")
    @NotBlank(message = "id must be")
    private Long id;

    @Schema(example = "starleken@mail.ru", description = "must be unique")
    @Email(message = "Incorrect email format")
    private String email;

    @Schema(example = "http://image.url", description = "Link to the image file")
    private String imageUrl;
}
