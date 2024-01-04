package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;

import java.util.List;

public interface PostService {

    List<PostFullDto> findAll();

    PostFullDto findById(Long id);

    PostFullDto create(PostCreateDto dto);

    PostFullDto update(PostUpdateDto dto);

    void deleteById(Long id);
}
