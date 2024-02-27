package com.starleken.springchannel.dto.channel;

import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.entity.ChannelEntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ChannelFullDto {

    @Schema(example = "1", description = "Channel id")
    private Long id;

    @Schema(example = "Starleken", description = "Channel name")
    private String name;

    @Schema(example = "PROGRAMMING", description = "Channel type")
    private ChannelEntityType type;

    @Schema(example = "http://image.com", description = "URL to image")
    private String imageUrl;

    @Schema(description = "Сhannel posts")
    private List<PostFullDto> posts;
}
