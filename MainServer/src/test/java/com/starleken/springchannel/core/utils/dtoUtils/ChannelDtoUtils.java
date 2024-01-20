package com.starleken.springchannel.core.utils.dtoUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntityType;

public abstract class ChannelDtoUtils {

    public static ChannelCreateDto generateChannelCreateDto(){
        Faker faker = FakerUtils.FAKER;

        ChannelCreateDto dto = new ChannelCreateDto();

        dto.setName(faker.name().title());
        dto.setType(ChannelEntityType.PROGRAMMING);

        return dto;
    }

    public static ChannelCreateDto generateInvalidCreateDto(){
        Faker faker = FakerUtils.FAKER;

        ChannelCreateDto dto = generateChannelCreateDto();
        dto.setName("");

        return dto;
    }

    public static ChannelUpdateDto generateChannelUpdateDto(long id){
        Faker faker = FakerUtils.FAKER;

        ChannelUpdateDto dto = new ChannelUpdateDto();

        dto.setId(id);
        dto.setName(faker.name().title());
        dto.setType(ChannelEntityType.PROGRAMMING);

        return dto;
    }

    public static ChannelUpdateDto generateInvalidChannelUpdateDto(long id){
        Faker faker = FakerUtils.FAKER;

        ChannelUpdateDto dto = generateChannelUpdateDto(id);

        dto.setName("");

        return dto;
    }

    public static ChannelUpdateDto generateChannelUpdateDtoWithoutId(){
        Faker faker = FakerUtils.FAKER;

        ChannelUpdateDto dto = new ChannelUpdateDto();

        dto.setName(faker.name().title());
        dto.setType(ChannelEntityType.PROGRAMMING);

        return dto;
    }
}
