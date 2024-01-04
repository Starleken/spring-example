package com.starleken.springchannel.service;

import com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.ChannelEntityType;
import com.starleken.springchannel.exception.entityCredentials.EntityCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityField.EntityFieldIsTakenException;
import com.starleken.springchannel.exception.entityNotFound.EntityIsNotFoundException;
import com.starleken.springchannel.mapper.ChannelMapper;
import com.starleken.springchannel.mapper.UserMapper;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.service.impl.ChannelServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils.generateChannelCreateDto;
import static com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils.generateChannelUpdateDto;
import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.generateChannel;
import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.generateChannelWithId;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    private ChannelRepository repository;

    @Spy
    private ChannelMapper mapper = Mappers.getMapper(ChannelMapper.class);

    @InjectMocks
    private ChannelServiceImpl channelService;

    @Test
    void findAll_happyPath() {
        //given
        ChannelEntity savedChannel1 = generateChannelWithId();
        ChannelEntity savedChannel2 = generateChannelWithId();

        when(repository.findAll()).thenReturn(List.of(savedChannel1, savedChannel2));

        //when
        List<ChannelPreviewDto> findedChannels = channelService.findAll();

        //then
        Assertions.assertNotNull(findedChannels);
        Assertions.assertEquals(2, findedChannels.size());
    }

    @Test
    void findById_happyPath() {
        //given
        ChannelEntity channelToFind = generateChannelWithId();

        when(repository.findById(channelToFind.getId())).thenReturn(Optional.of(channelToFind));

        //when
        ChannelFullDto findedChannel = channelService.findById(channelToFind.getId());

        //then
        Assertions.assertNotNull(findedChannel);
        Assertions.assertEquals(channelToFind.getId(), findedChannel.getId());
        Assertions.assertEquals(channelToFind.getName(), findedChannel.getName());
    }

    @Test
    void findById_whenNotFound() {
        //given
        Long idToSearch = 5L;
        when(repository.findById(idToSearch)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> channelService.findById(idToSearch));

        //then
    }

    @Test
    void create_happyPath() {
        //given
        ChannelCreateDto createDto = generateChannelCreateDto();

        when(repository.save(Mockito.any(ChannelEntity.class))).thenAnswer(i -> i.getArguments()[0]);

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
        ChannelEntity savedChannel = generateChannelWithId();
        ChannelCreateDto createDto = generateChannelCreateDto();
        createDto.setName(savedChannel.getName());

        when(repository.findOneByName(savedChannel.getName())).thenReturn(savedChannel);

        //when
        Assertions.assertThrows(EntityFieldIsTakenException.class,
                () -> channelService.create(createDto));

        //then
    }

    @Test
    void update_happyPath() {
        //given
        ChannelEntity channelToUpdate = generateChannelWithId();
        ChannelUpdateDto updateDto = generateChannelUpdateDto(channelToUpdate.getId());

        when(repository.findById(channelToUpdate.getId())).thenReturn(Optional.of(channelToUpdate));
        when(repository.save(Mockito.any(ChannelEntity.class))).thenAnswer(i -> i.getArguments()[0]);

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
        ChannelUpdateDto updateDto = generateChannelUpdateDto(1L);

        when(repository.findById(updateDto.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> channelService.update(updateDto));

        //then
    }

    @Test
    void update_whenNameIsExists() {
        //given
        ChannelEntity savedChannel = generateChannelWithId();
        ChannelEntity channelToUpdate = generateChannelWithId();
        ChannelUpdateDto updateDto = generateChannelUpdateDto(channelToUpdate.getId());
        updateDto.setName(savedChannel.getName());

        when(repository.findById(updateDto.getId())).thenReturn(Optional.of(channelToUpdate));
        when(repository.findOneByName(updateDto.getName())).thenReturn(savedChannel);

        //when
        Assertions.assertThrows(EntityFieldIsTakenException.class,
                () -> channelService.update(updateDto));

        //then
    }

    @Test
    void deleteById_happyPath() {
        //given
        ChannelEntity savedChannel = generateChannelWithId();

        when(repository.findById(savedChannel.getId())).thenReturn(Optional.of(savedChannel));

        //when
        channelService.deleteById(savedChannel.getId());

        //then
        verify(repository).deleteById(savedChannel.getId());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        Long idToSearch = 5L;

        when(repository.findById(idToSearch)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> channelService.deleteById(idToSearch));

        //then
    }
}
