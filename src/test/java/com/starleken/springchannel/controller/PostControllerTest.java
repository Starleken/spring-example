package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starleken.springchannel.core.db.PostDbHelper;
import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
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

import static com.starleken.springchannel.core.utils.dtoUtils.PostDtoUtils.generateCreateDto;
import static com.starleken.springchannel.core.utils.dtoUtils.PostDtoUtils.generateUpdateDto;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private PostDbHelper dbHelper;

    @Autowired
    public PostControllerTest(PostDbHelper helper,MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.dbHelper = helper;
    }

    @BeforeEach
    void setUp() {
        dbHelper.clearDB();
    }

    @Test
    void findAll_happyPath() throws Exception{
        //given
        dbHelper.savePost();
        dbHelper.savePost();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        List<PostFullDto> postFullDtos = objectMapper.readValue(bytes,
                new TypeReference<List<PostFullDto>>() {
        });

        //then
        Assertions.assertNotNull(postFullDtos);
        Assertions.assertEquals(2, postFullDtos.size());
    }

    @Test
    void findById_happyPath() throws Exception {
        //given
        PostEntity savedPost = dbHelper.savePost();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}",
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
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        ChannelEntity savedChannel = dbHelper.saveChannel();
        PostCreateDto createDto = generateCreateDto(savedChannel.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/posts")
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
        PostCreateDto createDto = generateCreateDto(1L);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_happyPath() throws Exception{
        //given
        PostEntity savedPost = dbHelper.savePost();
        PostUpdateDto updateDto = generateUpdateDto(savedPost.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/posts")
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
        PostUpdateDto updateDto = generateUpdateDto(1L);

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        PostEntity savedPost = dbHelper.savePost();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{id}",
                        savedPost.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<PostEntity> findedPost = dbHelper.findById(savedPost.getId());

        //then
        Assertions.assertTrue(findedPost.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() throws Exception {
        //given
        long idForSearch = 10L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }
}
