package com.starleken.springchannel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @Schema(example = "1", description = "User id")
    @NotBlank(message = "id must be")
    private Long id;

    @NotBlank(message = "old password must be")
    private String oldPassword;

    @NotBlank(message = "new password must be")
    private String newPassword;
}
