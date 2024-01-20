package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;

import java.util.List;

public interface ChannelService {

    List<ChannelPreviewDto> findAll();

    ChannelFullDto findById(Long id);

    ChannelFullDto create(ChannelCreateDto dto);

    ChannelFullDto update(ChannelUpdateDto dto);

    void deleteById(Long id);
}
