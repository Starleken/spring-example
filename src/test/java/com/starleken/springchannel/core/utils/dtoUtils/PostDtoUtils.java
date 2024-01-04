package com.starleken.springchannel.core.utils.dtoUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;

public abstract class PostDtoUtils {

    public static PostCreateDto generateCreateDto(Long channelId){
        Faker faker = FakerUtils.FAKER;

        PostCreateDto dto = new PostCreateDto();
        dto.setTitle(faker.name().title());
        dto.setContent(faker.name().nameWithMiddle());
        dto.setChannelId(channelId);

        return dto;
    }

    public static PostUpdateDto generateUpdateDto(Long postId){
        Faker faker = FakerUtils.FAKER;

        PostUpdateDto dto = new PostUpdateDto();
        dto.setId(postId);
        dto.setTitle(faker.name().title());
        dto.setContent(faker.name().nameWithMiddle());

        return dto;
    }
}
