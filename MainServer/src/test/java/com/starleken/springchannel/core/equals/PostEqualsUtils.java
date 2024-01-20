package com.starleken.springchannel.core.equals;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.PostEntity;
import org.junit.jupiter.api.Assertions;

public abstract class PostEqualsUtils {

    public static void EqualEntityAndFullDto(PostEntity entity, PostFullDto dto){
        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getTitle(), dto.getTitle());
        Assertions.assertEquals(entity.getContent(), dto.getContent());
    }

    public static void EqualEntityAndCreateDto(PostEntity entity, PostCreateDto dto){
        Assertions.assertEquals(dto.getTitle(), entity.getTitle());
        Assertions.assertEquals(dto.getContent(), entity.getContent());
    }

    public static void EqualFullDtoAndCreateDto(PostFullDto fullDto, PostCreateDto dto){
        Assertions.assertEquals(dto.getTitle(), fullDto.getTitle());
        Assertions.assertEquals(dto.getContent(), fullDto.getContent());
    }

    public static void EqualFullDtoAndUpdateDto(PostFullDto fullDto, PostUpdateDto dto){
        Assertions.assertEquals(dto.getId(), fullDto.getId());
        Assertions.assertEquals(dto.getTitle(), fullDto.getTitle());
        Assertions.assertEquals(dto.getContent(), fullDto.getContent());
    }
}
