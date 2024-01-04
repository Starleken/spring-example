package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starleken.springchannel.core.db.ChannelDbHelper;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import org.junit.jupiter.api.*;
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

import static com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils.generateChannelCreateDto;
import static com.starleken.springchannel.core.utils.dtoUtils.ChannelDtoUtils.generateChannelUpdateDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ChannelControllerTest {

    private ChannelDbHelper helper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ChannelControllerTest(ChannelDbHelper helper, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.helper = helper;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        helper.clearDB();
    }

    @Test
    void findAll_happyPath() throws Exception{
        //given
        helper.saveChannel();
        helper.saveChannel();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channels"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        List<ChannelPreviewDto> channelPreviewDtos = objectMapper.readValue(
                bytes, new TypeReference<List<ChannelPreviewDto>>() {
        });

        //then
        Assertions.assertEquals(2, channelPreviewDtos.size());
    }

    @Test
    void findById_happyPath() throws Exception{
        //given
        ChannelEntity savedChannel = helper.saveChannel();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channels/{id}",
                        savedChannel.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        ChannelFullDto channelFullDto = objectMapper.readValue(bytes, ChannelFullDto.class);

        //then
        Assertions.assertEquals(savedChannel.getId(), channelFullDto.getId());
        Assertions.assertEquals(savedChannel.getName(), channelFullDto.getName());
    }

    @Test
    void findById_whenNotFound() throws Exception{
        //given
        long idForSearch = 5L;

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channels/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        ChannelCreateDto createDto = generateChannelCreateDto();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        ChannelFullDto channelFullDto = objectMapper.readValue(bytes, ChannelFullDto.class);

        //then
        Assertions.assertNotNull(channelFullDto);
        Assertions.assertEquals(createDto.getName(), channelFullDto.getName());
        Assertions.assertEquals(createDto.getType(), channelFullDto.getType());
    }

    @Test
    void create_whenNameIsTaken() throws Exception{
        //given
        ChannelEntity savedChannel = helper.saveChannel();

        ChannelCreateDto createDto = generateChannelCreateDto();
        createDto.setName(savedChannel.getName());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void update_happyPath() throws Exception{
        //given
        ChannelEntity savedChannel = helper.saveChannel();
        ChannelUpdateDto updateDto = generateChannelUpdateDto(savedChannel.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        ChannelFullDto channelFullDto = objectMapper.readValue(bytes, ChannelFullDto.class);

        //then
        Assertions.assertNotNull(channelFullDto);
        Assertions.assertEquals(updateDto.getName(), channelFullDto.getName());
        Assertions.assertEquals(updateDto.getType(), channelFullDto.getType());
    }

    @Test
    void update_whenNotFound() throws Exception{
        //given
        ChannelUpdateDto updateDto = generateChannelUpdateDto(1L);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_whenNameIsExists() throws Exception{
        //given
        ChannelEntity savedChannel = helper.saveChannel();
        ChannelEntity channelToUpdate = helper.saveChannel();

        ChannelUpdateDto updateDto = generateChannelUpdateDto(channelToUpdate.getId());
        updateDto.setName(savedChannel.getName());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        ChannelEntity savedChannel = helper.saveChannel();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/channels/{id}",
                        savedChannel.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<ChannelEntity> findedChannel = helper.
                findChannelById(savedChannel.getId());

        //then
        Assertions.assertTrue(findedChannel.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() throws Exception{
        //given
        long idForDelete = 5L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/channels/{id}",
                        idForDelete))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }
}
