package com.starleken.springchannel.dto.channel;

import com.starleken.springchannel.entity.ChannelEntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChannelCreateDto {

    @Schema(example = "Starleken", description = "Channel name")
    @NotBlank(message = "Name must be")
    private String name;

    @Schema(example = "PROGRAMMING", description = "Channel type")
    @NotBlank(message = "Type must be")
    private ChannelEntityType type;
}
