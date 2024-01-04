package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.exception.entityNotFound.EntityIsNotFoundException;
import com.starleken.springchannel.mapper.PostMapper;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.repository.PostRepository;
import com.starleken.springchannel.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.core.utils.dtoUtils.PostDtoUtils.*;
import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.generateChannel;
import static com.starleken.springchannel.core.utils.entityUtils.PostEntityUtils.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Spy
    private PostMapper mapper = Mappers.getMapper(PostMapper.class);

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void findAll_happyPath() {
        //given
        PostEntity savedPost1 = generatePostWithId();
        PostEntity savedPost2 = generatePostWithId();

        when(postRepository.findAll()).thenReturn(List.of(savedPost1, savedPost2));

        //when
        List<PostFullDto> findedPosts = postService.findAll();

        //then
        Assertions.assertNotNull(findedPosts);
        Assertions.assertEquals(2, findedPosts.size());
    }

    @Test
    void findById_happyPath() {
        //given
        PostEntity savedPost = generatePostWithId();

        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));

        //when
        PostFullDto findedPost = postService.findById(savedPost.getId());

        //then
        Assertions.assertNotNull(findedPost);
        Assertions.assertEquals(savedPost.getTitle(), findedPost.getTitle());
    }

    @Test
    void findById_whenNotFound() {
        //given
        PostEntity postToSearch = generatePostWithId();

        when(postRepository.findById(postToSearch.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> postService.findById(postToSearch.getId()));

        //then
    }

    @Test
    void create_happyPath(){
        //given
        ChannelEntity postChannel = generateChannel();
        PostCreateDto createDto = generateCreateDto(postChannel.getId());

        when(postRepository.save(Mockito.any(PostEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(channelRepository.findById(createDto.getChannelId())).thenReturn(Optional.of(postChannel));

        //when
        PostFullDto postFullDto = postService.create(createDto);

        //then
        Assertions.assertNotNull(postFullDto);
        Assertions.assertEquals(createDto.getTitle(), postFullDto.getTitle());
        Assertions.assertEquals(createDto.getChannelId(), postFullDto.getChannelId());
    }

    @Test
    void create_whenChannelIsNotFound() {
        //given
        PostCreateDto createDto = generateCreateDto(1L);

        when(channelRepository.findById(createDto.getChannelId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> postService.create(createDto));

        //then
    }

    @Test
    void update_happyPath() {
        //given
        PostEntity post = generatePostWithId();
        PostUpdateDto updateDto = generateUpdateDto(post.getId());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(Mockito.any(PostEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        PostFullDto fullDto = postService.update(updateDto);

        //then
        Assertions.assertNotNull(fullDto);
        Assertions.assertEquals(updateDto.getId(), fullDto.getId());
        Assertions.assertEquals(updateDto.getTitle(), fullDto.getTitle());
    }

    @Test
    void update_whenNotFound(){
        //given
        PostEntity postToSearch = generatePostWithId();
        PostUpdateDto updateDto = generateUpdateDto(postToSearch.getId());

        when(postRepository.findById(updateDto.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> postService.update(updateDto));

        //then
    }

    @Test
    void deleteById_happyPath() {
        //given
        PostEntity postToDelete = generatePostWithId();

        when(postRepository.findById(postToDelete.getId())).thenReturn(Optional.of(postToDelete));

        //when
        postService.deleteById(postToDelete.getId());

        //then
        verify(postRepository).deleteById(postToDelete.getId());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        long idToSearch = 5L;

        when(postRepository.findById(idToSearch)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(EntityIsNotFoundException.class,
                () -> postService.deleteById(idToSearch));

        //then
    }
}
