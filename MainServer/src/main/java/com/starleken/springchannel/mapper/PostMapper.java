package com.starleken.springchannel.mapper;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    PostEntity mapToEntity(PostCreateDto dto);

    @Mapping(target = "channelId", source = "channel.id")
    PostFullDto mapToDto(PostEntity postEntity);
}
