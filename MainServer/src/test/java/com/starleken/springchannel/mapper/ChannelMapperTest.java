package com.starleken.springchannel.mapper;

import com.starleken.springchannel.core.equals.ChannelEqualsUtils;
import com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils;
import com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.entity.ChannelEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.starleken.springchannel.core.equals.ChannelEqualsUtils.*;
import static com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils.*;
import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.generateChannelWithId;

@ExtendWith(MockitoExtension.class)
public class ChannelMapperTest {

    private ChannelMapper mapper = Mappers.getMapper(ChannelMapper.class);

    @Test
    void mapToFullDto_happyPath() {
        //given
        ChannelEntity toMap = generateChannelWithId();

        //when
        ChannelFullDto mapped = mapper.mapToFullDto(toMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndFullDto(toMap, mapped);
    }

    @Test
    void mapToEntityFromCreateDto_happyPath() {
        //given
        ChannelCreateDto toMap = generateChannelCreateDto();

        //when
        ChannelEntity mapped = mapper.mapToEntity(toMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndCreateDto(mapped, toMap);
    }

    @Test
    void mapToPreviewDto_happyPath() {
        //given
        ChannelEntity toMap = generateChannelWithId();

        //when
        ChannelPreviewDto mapped = mapper.mapToPreviewDto(toMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndPreviewDto(toMap, mapped);
    }
}
