package com.starleken.springchannel.core.utils.entityUtils;

import com.github.javafaker.Faker;
import com.starleken.springchannel.core.utils.FakerUtils;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.ChannelEntityType;

public abstract class ChannelEntityUtils {

    public static ChannelEntity generateChannel(){
        Faker faker = FakerUtils.FAKER;

        ChannelEntity channel = new ChannelEntity();
        channel.setName(faker.name().title());
        channel.setType(ChannelEntityType.PROGRAMMING);

        return channel;
    }

    public static ChannelEntity generateChannelWithId(){
        Faker faker = FakerUtils.FAKER;

        ChannelEntity channel = generateChannel();
        channel.setId(faker.number().randomNumber());

        return channel;
    }
}
