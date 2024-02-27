package com.starleken.springchannel.core.utils.entityUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.entity.PostEntity;

import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.*;

public class PostEntityUtils {

    public static PostEntity generatePostWithoutChannel(){
        Faker faker = FakerUtils.FAKER;

        PostEntity post = new PostEntity();
        post.setTitle(faker.name().title());
        post.setContent(faker.name().nameWithMiddle());

        return post;
    }

    public static PostEntity generatePostWithId(){
        Faker faker = FakerUtils.FAKER;

        PostEntity post = generatePostWithoutChannel();
        post.setChannel(generateChannelWithId());
        post.setId(faker.number().randomNumber());

        return post;
    }

    public static PostEntity generatePostWithoutId(){
        Faker faker = new Faker();

        PostEntity post = generatePostWithoutChannel();
        post.setChannel(generateChannelWithId());

        return post;
    }
}
