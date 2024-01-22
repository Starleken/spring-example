package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.mapper.ChannelMapper;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.service.ChannelService;
import com.starleken.springchannel.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starleken.springchannel.utils.ExceptionUtils.throwEntityNotFoundException;
import static com.starleken.springchannel.utils.ExceptionUtils.throwNameIsTakenException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper mapper;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public List<ChannelPreviewDto> findAll() {
        List<ChannelEntity> foundedChannels = channelRepository.findAll();

        List<ChannelPreviewDto> dtos = foundedChannels.stream()
                .map(mapper::mapToPreviewDto)
                .collect(Collectors.toList());

        log.info("ChannelServiceImpl -> findAll: found {} channels", dtos.size());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelFullDto findById(Long id) {
        Optional<ChannelEntity> channel = channelRepository.findById(id);

        if (channel.isEmpty()){
            log.info("ChannelServiceImpl -> findById: channel not found by id: {}", id);
            throwEntityNotFoundException(ChannelEntity.class, id);
        }

        ChannelFullDto channelFullDto = mapper.mapToFullDto(channel.get());

        log.info("ChannelServiceImpl -> findById: found {} by id {}", channelFullDto, id);
        return channelFullDto;
    }

    @Override
    @Transactional
    public ChannelFullDto create(ChannelCreateDto dto) {
        ChannelEntity channelToSave = mapper.mapToEntity(dto);

        checkIfNameChannelIsExists(channelToSave.getName(), channelToSave.getId());

        String imageUrl = imageService.uploadImage(dto.getImage());
        channelToSave.setImageUrl(imageUrl);
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        ChannelFullDto channelFullDto = mapper.mapToFullDto(savedChannel);

        log.info("ChannelServiceImpl -> create: channel created: {}", channelFullDto);
        return channelFullDto;
    }

    @Override
    @Transactional
    public ChannelFullDto update(ChannelUpdateDto dto) {
        Optional<ChannelEntity> findedChannel = channelRepository.findById(dto.getId());

        if (findedChannel.isEmpty()){
            log.info("ChannelServiceImpl -> update: channel is not found by id: {}", dto.getId());
            throwEntityNotFoundException(ChannelEntity.class, dto.getId());
        }

        checkIfNameChannelIsExists(dto.getName(), dto.getId());

        ChannelEntity channelToUpdate = findedChannel.get();
        channelToUpdate.setName(dto.getName());
        channelToUpdate.setType(dto.getType());

        ChannelEntity updatedChannel = channelRepository.save(channelToUpdate);

        ChannelFullDto channelFullDto = mapper.mapToFullDto(updatedChannel);

        log.info("ChannelServiceImpl -> update: channel updated: {}", channelFullDto);
        return channelFullDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ChannelEntity> findedChannel = channelRepository.findById(id);

        if (findedChannel.isEmpty()){
            log.info("ChannelServiceImpl -> deleteById: channel is not found by id: {}", id);
            throwEntityNotFoundException(ChannelEntity.class, id);
        }

        channelRepository.deleteById(id);
        log.info("ChannelServiceImpl -> deleteById: channel deleted by id: {}", id);
    }

    private void checkIfNameChannelIsExists(String name, Long id){
        ChannelEntity findedChannel = channelRepository.findOneByName(name);

        if (findedChannel != null && !findedChannel.getId().equals(id)){
            throwNameIsTakenException(findedChannel.getName());
        }
    }
}
