package com.starleken.springchannel.mapper;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.entity.ChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PostMapper.class)
public interface ChannelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    ChannelEntity mapToEntity(ChannelCreateDto dto);

    ChannelFullDto mapToFullDto(ChannelEntity channelEntity);

    ChannelPreviewDto mapToPreviewDto(ChannelEntity channelEntity);
}
