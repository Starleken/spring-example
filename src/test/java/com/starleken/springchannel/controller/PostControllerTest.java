package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.repository.ChannelRepository;
import com.starleken.springchannel.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    private PostRepository postRepository;
    private ChannelRepository channelRepository;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public PostControllerTest(ChannelRepository channelRepository, PostRepository postRepository, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.postRepository = postRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.channelRepository = channelRepository;
    }

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        channelRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() throws Exception{
        //given
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);
        List<PostEntity> posts = generatePosts();

        for (PostEntity post : posts){
            post.setChannel(savedChannel);
        }
        postRepository.saveAll(posts);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/post"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        List<PostFullDto> postFullDtos = objectMapper.readValue(bytes,
                new TypeReference<List<PostFullDto>>() {
        });

        //then
        Assertions.assertNotNull(postFullDtos);
        Assertions.assertEquals(3, postFullDtos.size());
    }

    @Test
    void findById_happyPath() throws Exception {
        //given
        ChannelEntity channelToSave = generateChannel();
        channelRepository.save(channelToSave);

        PostEntity postToSave = generatePost();
        postToSave.setChannel(channelToSave);
        PostEntity savedPost = postRepository.save(postToSave);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/post/{id}",
                        savedPost.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        PostFullDto postFullDto = objectMapper.readValue(bytes, PostFullDto.class);

        //then
        Assertions.assertNotNull(postFullDto);
        Assertions.assertEquals(savedPost.getTitle(), postFullDto.getTitle());
        Assertions.assertEquals(savedPost.getContent(), postFullDto.getContent());
        Assertions.assertEquals(savedPost.getChannel().getId(), postFullDto.getChannelId());
    }

    @Test
    void findById_whenNotFound() throws Exception{
        //given
        long idForSearch = 5L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/post/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        PostCreateDto createDto = new PostCreateDto();
        createDto.setTitle("Title");
        createDto.setContent("Content");
        createDto.setChannelId(savedChannel.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        PostFullDto postFullDto = objectMapper.readValue(bytes, PostFullDto.class);

        //then
        Assertions.assertNotNull(postFullDto);
        Assertions.assertEquals(createDto.getTitle(), postFullDto.getTitle());
        Assertions.assertEquals(createDto.getContent(), postFullDto.getContent());
        Assertions.assertEquals(createDto.getChannelId(), postFullDto.getChannelId());
    }

    @Test
    void create_whenChannelNotFound() throws Exception{
        //given
        PostCreateDto createDto = new PostCreateDto();
        createDto.setTitle("title");
        createDto.setContent("content");
        createDto.setChannelId(1L);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_happyPath() throws Exception{
        //given
        String titleForCheck = "new title";

        ChannelEntity channel = generateChannel();
        channelRepository.save(channel);

        PostEntity postToSave = generatePost();
        postToSave.setChannel(channel);
        PostEntity savedPost = postRepository.save(postToSave);

        PostUpdateDto updateDto = new PostUpdateDto();
        updateDto.setId(savedPost.getId());
        updateDto.setTitle(titleForCheck);
        updateDto.setContent(savedPost.getContent());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        PostFullDto postFullDto = objectMapper.readValue(bytes, PostFullDto.class);

        //then
        Assertions.assertNotNull(postFullDto);
        Assertions.assertEquals(updateDto.getTitle(), postFullDto.getTitle());
        Assertions.assertEquals(updateDto.getContent(), postFullDto.getContent());
    }

    @Test
    void update_whenNotFound() throws Exception{
        //given
        PostUpdateDto updateDto = new PostUpdateDto();
        updateDto.setId(5L);
        updateDto.setTitle("Title");
        updateDto.setContent("Content");

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);

        PostEntity postToSave = generatePost();
        postToSave.setChannel(savedChannel);
        PostEntity savedPost = postRepository.save(postToSave);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{id}",
                        savedPost.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<PostEntity> findedPost = postRepository
                .findById(savedPost.getId());

        //then
        Assertions.assertTrue(findedPost.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() throws Exception {
        //given
        long idForSearch = 10L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }
}
