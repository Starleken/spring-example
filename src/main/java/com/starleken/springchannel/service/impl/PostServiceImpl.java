package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.mapper.PostMapper;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.repository.PostRepository;
import com.starleken.springchannel.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starleken.springchannel.utils.ExceptionUtils.throwEntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ChannelRepository channelRepository;
    private final PostMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PostFullDto> findAll() {
        List<PostEntity> entities = postRepository.findAll();

        List<PostFullDto> dtos = entities.stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());

        log.info("PostServiceImpl -> findAll: found {} posts", dtos.size());

        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public PostFullDto findById(Long id) {
        Optional<PostEntity> findedPost = postRepository.findById(id);

        if (findedPost.isEmpty()){
            log.info("PostServiceImpl -> findById: post is not found by id: {}", id);
            throwEntityNotFoundException(PostEntity.class, id);
        }

        PostFullDto postFullDto = mapper.mapToDto(findedPost.get());

        log.info("PostServiceImpl -> findById: " + postFullDto);
        return postFullDto;
    }

    @Override
    @Transactional
    public PostFullDto create(PostCreateDto dto) {
        PostEntity postToSave = mapper.mapToEntity(dto);
        Optional<ChannelEntity> findedChannel = channelRepository
                .findById(dto.getChannelId());

        if (findedChannel.isEmpty()){
            throwEntityNotFoundException(ChannelEntity.class, dto.getChannelId());
        }

        postToSave.setChannel(findedChannel.get());
        PostEntity savedPost = postRepository.save(postToSave);

        PostFullDto postFullDto = mapper.mapToDto(savedPost);

        log.info("PostServiceImpl -> create: " + postFullDto);
        return postFullDto;
    }

    @Override
    @Transactional
    public PostFullDto update(PostUpdateDto dto) {
        Optional<PostEntity> findedPost = postRepository.findById(dto.getId());

        if (findedPost.isEmpty()){
            log.info("PostServiceImpl -> update: post is not found by id: {}", dto.getId());
            throwEntityNotFoundException(PostEntity.class, dto.getId());
        }
        PostEntity postToUpdate = findedPost.get();
        postToUpdate.setTitle(dto.getTitle());
        postToUpdate.setContent(dto.getContent());

        PostEntity updatedPost = postRepository.save(postToUpdate);

        PostFullDto postFullDto = mapper.mapToDto(updatedPost);

        log.info("PostServiceImpl -> update: " + postFullDto);
        return postFullDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<PostEntity> findedPost = postRepository.findById(id);

        if (findedPost.isEmpty()){
            log.info("PostServiceImpl -> deleteById: post is not found by id: {}", id);
            throwEntityNotFoundException(PostEntity.class, id);
        }

        postRepository.deleteById(id);
        log.info("PostServiceImpl -> deleteById: post deleted by id " + id);
    }
}
