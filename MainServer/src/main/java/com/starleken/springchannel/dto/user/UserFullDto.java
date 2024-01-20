package com.starleken.springchannel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserFullDto {

    @Schema(example = "1", description = "User id")
    private Long id;

    @Schema(example = "Starleken", description = "must be unique")
    private String login;

    @Schema(example = "http://image.url", description = "Link to the image file")
    private String imageURL;

    @Schema(example = "starleken@mail.ru", description = "must be unique")
    private String email;
}
