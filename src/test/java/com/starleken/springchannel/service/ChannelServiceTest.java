package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.ChannelEntityType;
import com.starleken.springchannel.exception.entityCredentials.ChannelCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.ChannelIsNotFoundException;
import com.starleken.springchannel.repository.ChannelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.*;

@SpringBootTest
public class ChannelServiceTest {

    private ChannelService channelService;
    private ChannelRepository channelRepository;

    @Autowired
    public ChannelServiceTest(ChannelService channelService, ChannelRepository channelRepository) {
        this.channelService = channelService;
        this.channelRepository = channelRepository;
    }

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        ChannelEntity channel1 = generateChannel();
        ChannelEntity channel2 = generateChannel();

        channelRepository.save(channel1);
        channelRepository.save(channel2);

        //when
        List<ChannelPreviewDto> findedChannels = channelService.findAll();

        //then
        Assertions.assertNotNull(findedChannels);
        Assertions.assertEquals(2, findedChannels.size());
    }

    @Test
    void findById_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        //when
        ChannelFullDto findedChannel = channelService.findById(savedChannel.getId());

        //then
        Assertions.assertNotNull(findedChannel);
        Assertions.assertEquals(savedChannel.getId(), findedChannel.getId());
        Assertions.assertEquals(savedChannel.getName(), findedChannel.getName());
    }

    @Test
    void findById_whenNotFound() {
        //given
        Long idForSearch = 5L;
        boolean isNotFound = false;

        //when
        try{
            channelService.findById(idForSearch);
        } catch (ChannelIsNotFoundException ex){
            isNotFound = true;
        }

        Assertions.assertTrue(isNotFound);
    }

    @Test
    void create_happyPath() {
        //given
        ChannelCreateDto createDto = new ChannelCreateDto();
        createDto.setName("Name");
        createDto.setType(ChannelEntityType.TOURISM);

        //when
        ChannelFullDto createdChannel = channelService.create(createDto);

        //then
        Assertions.assertNotNull(createdChannel);
        Assertions.assertEquals(createDto.getName(), createdChannel.getName());
        Assertions.assertEquals(createDto.getType(), createdChannel.getType());
    }

    @Test
    void create_whenNameIsExists() {
        //given
        ChannelEntity channel = generateChannel();
        channel.setName("Name");
        channelRepository.save(channel);

        ChannelCreateDto createDto = new ChannelCreateDto();
        createDto.setName("Name");
        createDto.setType(ChannelEntityType.TOURISM);

        boolean isNotCreated = false;

        //when
        try{
            channelService.create(createDto);
        } catch (ChannelCredentialsAreTakenException ex){
            isNotCreated = true;
        }

        //then
        Assertions.assertTrue(isNotCreated);
    }

    @Test
    void update_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);
        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(savedChannel.getId());
        updateDto.setName("Name");
        updateDto.setType(ChannelEntityType.TOURISM);

        //when
        ChannelFullDto updatedChannel = channelService.update(updateDto);

        //then
        Assertions.assertNotNull(updatedChannel);
        Assertions.assertEquals(updateDto.getName(), updatedChannel.getName());
        Assertions.assertEquals(updateDto.getType(), updatedChannel.getType());
    }

    @Test
    void update_whenNotFound() {
        //given
        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(5L);
        updateDto.setName("Name");
        updateDto.setType(ChannelEntityType.TOURISM);

        boolean isNotFound = false;

        //when
        try{
            channelService.update(updateDto);
        } catch (ChannelIsNotFoundException ex){
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);

    }

    @Test
    void update_whenNameIsExists() {
        //given
        String nameForCheck = "Name";

        ChannelEntity channel = generateChannel();
        channel.setName(nameForCheck);
        channelRepository.save(channel);

        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);
        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(savedChannel.getId());
        updateDto.setName(nameForCheck);
        updateDto.setType(ChannelEntityType.TOURISM);

        boolean isNotUpdated = false;

        //when
        try{
            channelService.update(updateDto);
        } catch (ChannelCredentialsAreTakenException ex){
            isNotUpdated = true;
        }

        //then
        Assertions.assertTrue(isNotUpdated);
    }

    @Test
    void deleteById_happyPath() {
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        //when
        channelService.deleteById(savedChannel.getId());
        Optional<ChannelEntity> findedChannel = channelRepository
                .findById(savedChannel.getId());

        //then
        Assertions.assertTrue(findedChannel.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        Long idForSearch = 5L;

        boolean isNotFound = false;

        //when
        try{
            channelService.deleteById(idForSearch);
        } catch (ChannelIsNotFoundException ex){
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }
}
