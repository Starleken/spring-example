package com.starleken.springchannel.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostCreateDto {

    @Schema(example = "Clean code benefits", description = "Post title")
    @NotNull(message = "Title must be")
    private String title;

    @Schema(example = "I advise everyone to read it", description = "Post content")
    @NotBlank(message = "Content must be")
    private String content;

    @Schema(example = "1", description = "Channel id where post is created")
    @NotNull(message = "channel must be")
    private Long channelId;
}
