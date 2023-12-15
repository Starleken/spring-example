package com.starleken.springchannel.service;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.exception.entityNotFound.ChannelIsNotFoundException;
import com.starleken.springchannel.exception.entityNotFound.PostIsNotFoundException;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.generateChannel;
import static com.starleken.springchannel.EntityGenerationUtils.generatePost;

@SpringBootTest
public class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;
    private ChannelRepository channelRepository;

    @Autowired
    public PostServiceTest(ChannelRepository channelRepository, PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.channelRepository = channelRepository;
    }

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        ChannelEntity channel = generateChannel();
        PostEntity post1 = generatePost();
        PostEntity post2 = generatePost();
        ChannelEntity savedChannel = channelRepository.save(channel);
        post1.setChannel(channel);
        post2.setChannel(channel);

        postRepository.save(post1);
        postRepository.save(post2);

        //when
        List<PostFullDto> findedPosts = postService.findAll();

        //then
        Assertions.assertNotNull(findedPosts);
        Assertions.assertEquals(2, findedPosts.size());
    }

    @Test
    void findById_happyPath() {
        //given
        ChannelEntity channel = generateChannel();
        PostEntity post1 = generatePost();
        ChannelEntity savedChannel = channelRepository.save(channel);
        post1.setChannel(channel);
        PostEntity savedPost = postRepository.save(post1);

        //when
        PostFullDto findedPost = postService.findById(savedPost.getId());

        //then
        Assertions.assertNotNull(findedPost);
        Assertions.assertEquals(savedPost.getTitle(), findedPost.getTitle());
    }

    @Test
    void findById_whenNotFound() {
        //given
        Long idForSearch = 1L;
        boolean isNotFound = false;

        //when
        try{
            postService.findById(idForSearch);
        } catch(PostIsNotFoundException ex){
            isNotFound = true;
        }

        //then
        Assertions.assertTrue(isNotFound);
    }

    @Test
    void create_happyPath(){
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Title");
        dto.setContent("Content");
        dto.setChannelId(savedChannel.getId());

        //when
        PostFullDto postFullDto = postService.create(dto);

        //then
        Assertions.assertNotNull(postFullDto);
        Assertions.assertEquals(dto.getTitle(), postFullDto.getTitle());
        Assertions.assertEquals(dto.getChannelId(), postFullDto.getChannelId());
    }

    @Test
    void create_whenChannelIsNotFound() {
        //given
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("1");
        dto.setContent("124");
        dto.setChannelId(5L);

        boolean channelNotIsFound = false;

        //when
        try{
            postService.create(dto);
        } catch (ChannelIsNotFoundException ex){
            channelNotIsFound = true;
        }

        //then
        Assertions.assertTrue(channelNotIsFound);
    }

    @Test
    void update_happyPath() {
        //given
        String newTitle = "New title";

        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);
        PostEntity postToSave = generatePost();
        postToSave.setChannel(savedChannel);
        PostEntity savedPost = postRepository.save(postToSave);

        PostUpdateDto updateDto = new PostUpdateDto();
        updateDto.setId(savedPost.getId());
        updateDto.setTitle(newTitle);
        updateDto.setContent(savedPost.getContent());

        //when
        PostFullDto fullDto = postService.update(updateDto);

        //then
        Assertions.assertNotNull(fullDto);
        Assertions.assertEquals(updateDto.getId(), fullDto.getId());
        Assertions.assertEquals(newTitle, fullDto.getTitle());
    }

    @Test
    void update_whenNotFound(){
        //given
        PostUpdateDto dto = new PostUpdateDto();
        dto.setId(1L);
        dto.setTitle("Title");
        dto.setContent("Content");

        boolean isNotFound = false;

        //when
        try{
            postService.update(dto);
        } catch (PostIsNotFoundException ex){
            isNotFound = true;
        }

        Assertions.assertTrue(isNotFound);
    }

    @Test
    void deleteById_happyPath() {
        //given
        ChannelEntity channel = generateChannel();
        PostEntity post = generatePost();
        ChannelEntity savedChannel = channelRepository.save(channel);
        post.setChannel(channel);
        PostEntity savedPost = postRepository.save(post);

        //when
        postService.deleteById(savedPost.getId());
        Optional<PostEntity> findedPost = postRepository.findById(savedPost.getId());

        //then
        Assertions.assertTrue(findedPost.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() {
        //given
        long idForSeach = 5L;

        boolean isNotFound = false;

        //when
        try{
            postService.deleteById(idForSeach);
        } catch (PostIsNotFoundException ex){
            isNotFound = true;
        }

        Assertions.assertTrue(isNotFound);
    }
}
