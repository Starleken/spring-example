package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.exception.entityCredentials.ChannelCredentialsAreTakenException;
import com.starleken.springchannel.exception.entityNotFound.ChannelIsNotFoundException;
import com.starleken.springchannel.mapper.ChannelMapper;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starleken.springchannel.constants.exceptionConstants.ChannelExceptionConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ChannelPreviewDto> findAll() {
        List<ChannelEntity> foundedChannels = channelRepository.findAll();

        List<ChannelPreviewDto> dtoList = foundedChannels.stream()
                .map(mapper::mapToPreviewDto)
                .collect(Collectors.toList());

        log.info("ChannelServiceImpl -> findAll: found {} channels", dtoList.size());
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelFullDto findById(Long id) {
        Optional<ChannelEntity> channel = channelRepository.findById(id);

        if (channel.isEmpty()){
            log.info("ChannelServiceImpl -> findById: channel not found by id: {}", id);
            throw new ChannelIsNotFoundException(getNotFoundText(id));
        }

        log.info("ChannelServiceImpl -> findById: found {} by id {}", channel.get(), id);
        return mapper.mapToFullDto(channel.get());
    }

    @Override
    @Transactional
    public ChannelFullDto create(ChannelCreateDto dto) {
        ChannelEntity channelToSave = mapper.mapToEntity(dto);

        checkIfNameChannelIsExists(channelToSave.getName(), channelToSave.getId());

        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        log.info("ChannelServiceImpl -> create: channel created: {}", savedChannel);
        return mapper.mapToFullDto(savedChannel);
    }

    @Override
    @Transactional
    public ChannelFullDto update(ChannelUpdateDto dto) {
        Optional<ChannelEntity> findedChannel = channelRepository.findById(dto.getId());

        if (findedChannel.isEmpty()){
            log.info("ChannelServiceImpl -> update: channel is not found by id: {}", dto.getId());
            throw new ChannelIsNotFoundException(getNotFoundText(dto.getId()));
        }

        checkIfNameChannelIsExists(dto.getName(), dto.getId());

        ChannelEntity channelToUpdate = findedChannel.get();
        channelToUpdate.setName(dto.getName());
        channelToUpdate.setType(dto.getType());

        ChannelEntity updatedChannel = channelRepository.save(channelToUpdate);

        log.info("ChannelServiceImpl -> update: channel updated: {}", updatedChannel);
        return mapper.mapToFullDto(updatedChannel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ChannelEntity> findedChannel = channelRepository.findById(id);

        if (findedChannel.isEmpty()){
            log.info("ChannelServiceImpl -> deleteById: channel is not found by id: {}", id);
            throw new ChannelIsNotFoundException(getNotFoundText(id));
        }

        log.info("ChannelServiceImpl -> deleteById: channel deleted by id: {}", id);
        channelRepository.deleteById(id);
    }

    private void checkIfNameChannelIsExists(String name, Long id){
        ChannelEntity findedChannel = channelRepository.findOneByName(name);

        if (findedChannel != null && !findedChannel.getId().equals(id)){
            throw new ChannelCredentialsAreTakenException(
                    getNameCredentialsText());
        }
    }
}
