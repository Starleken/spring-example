package com.starleken.springchannel.dto.channel;

import com.starleken.springchannel.entity.ChannelEntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChannelUpdateDto {

    @Schema(example = "1", description = "Channel id to update")
    @NotNull(message = "id must be")
    private Long id;

    @Schema(example = "Starleken", description = "Channel name")
    @NotBlank(message = "Name must be")
    private String name;

    private MultipartFile image;

    @Schema(example = "PROGRAMMING", description = "Channel type")
    @NotNull(message = "Type must be")
    private ChannelEntityType type;
}
