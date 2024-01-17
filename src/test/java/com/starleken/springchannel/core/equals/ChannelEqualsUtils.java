package com.starleken.springchannel.core.equals;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import org.junit.jupiter.api.Assertions;

public abstract class ChannelEqualsUtils {

    public static void EqualEntityAndFullDto(ChannelEntity entity, ChannelFullDto dto){
        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getName(), dto.getName());
        Assertions.assertEquals(entity.getType(), dto.getType());
    }

    public static void EqualEntityAndCreateDto(ChannelEntity entity, ChannelCreateDto dto){
        Assertions.assertEquals(dto.getName(), entity.getName());
        Assertions.assertEquals(dto.getType(), entity.getType());
    }

    public static void EqualEntityAndPreviewDto(ChannelEntity entity, ChannelPreviewDto dto){
        Assertions.assertEquals(entity.getName(), dto.getName());
        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getType(), dto.getType());
    }

    public static void EqualFullDtoAndCreateDto(ChannelFullDto fullDto, ChannelCreateDto dto){
        Assertions.assertEquals(dto.getName(), fullDto.getName());
        Assertions.assertEquals(dto.getType(), fullDto.getType());
    }

    public static void EqualFullDtoAndUpdateDto(ChannelFullDto fullDto, ChannelUpdateDto dto){
        Assertions.assertEquals(dto.getId(), fullDto.getId());
        Assertions.assertEquals(dto.getName(), fullDto.getName());
        Assertions.assertEquals(dto.getType(), fullDto.getType());
    }
}
