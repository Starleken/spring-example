package com.starleken.springchannel.repository;

import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.generateChannel;
import static com.starleken.springchannel.EntityGenerationUtils.generatePosts;

@SpringBootTest
public class ChannelRepositoryTest {
    private ChannelRepository channelRepository;

    @Autowired
    public ChannelRepositoryTest(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity channelToSave1 = generateChannel();
        List<PostEntity> posts = generatePosts();

        for (PostEntity post : posts){
            post.setChannel(channelToSave);
        }
        channelToSave.setPosts(posts);

        channelRepository.save(channelToSave);
        channelRepository.save(channelToSave1);

        //when
        List<ChannelEntity> findedChannels = channelRepository.findAll();

        //then
        Assertions.assertNotNull(findedChannels);
        Assertions.assertEquals(2, findedChannels.size());
        Assertions.assertEquals(channelToSave.getName(), findedChannels.get(0).getName());
        Assertions.assertEquals(channelToSave1.getName(), findedChannels.get(1).getName());
    }

    @Test
    void findById_happyPath() {
        ChannelEntity channelToSave = generateChannel();
        List<PostEntity> posts = generatePosts();

        for (PostEntity post : posts){
            post.setChannel(channelToSave);
        }
        channelToSave.setPosts(posts);

        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        Optional<ChannelEntity> findedChannel = channelRepository.findById(savedChannel.getId());

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
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);
        savedChannel.setName("bcxbcxc");

        //when
        ChannelEntity updatedChannel = channelRepository.save(savedChannel);

        //then
        Assertions.assertNotNull(updatedChannel);
        Assertions.assertEquals(savedChannel.getName(), updatedChannel.getName());
    }

    @Test
    void delete_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        //when
        channelRepository.deleteById(savedChannel.getId());
        Optional<ChannelEntity> channelToCheck = channelRepository.findById(savedChannel.getId());

        //then
        Assertions.assertEquals(true, channelToCheck.isEmpty());
    }
}
