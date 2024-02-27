package com.starleken.springchannel.repository;

import com.starleken.springchannel.core.db.ChannelDbHelper;
import com.starleken.springchannel.entity.ChannelEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.generateChannel;

@SpringBootTest
public class ChannelRepositoryTest {
    private ChannelRepository channelRepository;
    private ChannelDbHelper helper;

    @Autowired
    public ChannelRepositoryTest(ChannelRepository channelRepository, ChannelDbHelper helper) {
        this.channelRepository = channelRepository;
        this.helper = helper;
    }

    @BeforeEach
    void setUp() {
        helper.clearDB();
    }

    @Test
    void findAll_happyPath() {
        //given
        ChannelEntity savedChannel = helper.saveChannel();
        ChannelEntity savedChannel1 = helper.saveChannel();

        //when
        List<ChannelEntity> findedChannels = channelRepository.findAll();

        //then
        Assertions.assertNotNull(findedChannels);
        Assertions.assertEquals(2, findedChannels.size());
        Assertions.assertEquals(savedChannel.getName(), findedChannels.get(0).getName());
        Assertions.assertEquals(savedChannel1.getName(), findedChannels.get(1).getName());
    }

    @Test
    void findById_happyPath() {
        //given
        ChannelEntity savedChannel = helper.saveChannel();

        //when
        Optional<ChannelEntity> findedChannel = channelRepository.findById(savedChannel.getId());

        //then
        Assertions.assertTrue(findedChannel.isPresent());
        Assertions.assertEquals(savedChannel.getId(), findedChannel.get().getId());
    }

    @Test
    void create_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();

        //when
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        //then
        Assertions.assertNotNull(savedChannel);
        Assertions.assertEquals(channelToSave.getName(), savedChannel.getName());
    }

    @Test
    void update_happyPath() {
        //given
        String nameForCheck = "new name";

        ChannelEntity savedChannel = helper.saveChannel();
        savedChannel.setName(nameForCheck);

        //when
        ChannelEntity updatedChannel = channelRepository.save(savedChannel);

        //then
        Assertions.assertNotNull(updatedChannel);
        Assertions.assertEquals(savedChannel.getName(), updatedChannel.getName());
    }

    @Test
    void delete_happyPath() {
        //given
        ChannelEntity savedChannel = helper.saveChannel();

        //when
        channelRepository.deleteById(savedChannel.getId());
        Optional<ChannelEntity> channelToCheck = channelRepository.findById(savedChannel.getId());

        //then
        Assertions.assertTrue(channelToCheck.isEmpty());
    }
}
