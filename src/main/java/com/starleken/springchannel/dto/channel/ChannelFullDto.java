package com.starleken.springchannel.dto.channel;

import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.entity.ChannelEntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ChannelFullDto {

    @Schema(example = "1", description = "Channel id to update")
    @NotBlank(message = "id must be")
    private Long id;

    @Schema(example = "Starleken", description = "Channel name")
    @NotBlank(message = "Name must be")
    private String name;

    @Schema(example = "PROGRAMMING", description = "Channel type")
    @NotBlank(message = "Type must be")
    private ChannelEntityType type;

    @Schema(description = "Сhannel posts")
    private List<PostFullDto> posts;
}
