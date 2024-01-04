package com.starleken.springchannel.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostFullDto {

    @Schema(example = "1", description = "Post id")
    private Long id;

    @Schema(example = "Clean code benefits", description = "Post title")
    private String title;

    @Schema(example = "I advise everyone to read it", description = "Post content")
    private String content;

    @Schema(example = "1", description = "Must be")
    private Long channelId;
}
