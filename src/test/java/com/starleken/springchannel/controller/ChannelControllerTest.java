package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.ChannelEntityType;
import com.starleken.springchannel.repository.ChannelRepository;
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

import static com.starleken.springchannel.EntityGenerationUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ChannelControllerTest {

    private ChannelRepository channelRepository;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ChannelControllerTest(ObjectMapper objectMapper, ChannelRepository channelRepository, MockMvc mockMvc) {
        this.channelRepository = channelRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() throws Exception{
        //given
        ChannelEntity channel1 = generateChannel();
        ChannelEntity channel2 = generateChannel();
        channelRepository.save(channel1);
        channelRepository.save(channel2);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channel"))
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
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channel/{id}",
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
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/channel/{id}",
                        idForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        ChannelCreateDto createDto = new ChannelCreateDto();
        createDto.setName("Name");
        createDto.setType(ChannelEntityType.EDUCATION);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/channel")
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
        ChannelEntity channelToSave = generateChannel();
        channelToSave.setName("starleken");
        channelRepository.save(channelToSave);

        ChannelCreateDto createDto = new ChannelCreateDto();
        createDto.setName("starleken");
        createDto.setType(ChannelEntityType.EDUCATION);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void update_happyPath() throws Exception{
        //given
        ChannelEntity channelToSave = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToSave);

        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(savedChannel.getId());
        updateDto.setType(savedChannel.getType());
        updateDto.setName("new name");

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channel")
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
        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(5L);
        updateDto.setType(ChannelEntityType.PROGRAMMING);
        updateDto.setName("new name");

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_whenNameIsExists() throws Exception{
        //given
        String nameToCheck = "starleken";

        ChannelEntity channelToSave = generateChannel();
        channelToSave.setName(nameToCheck);
        channelRepository.save(channelToSave);

        ChannelEntity channelToUpdate = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channelToUpdate);

        ChannelUpdateDto updateDto = new ChannelUpdateDto();
        updateDto.setId(savedChannel.getId());
        updateDto.setType(savedChannel.getType());
        updateDto.setName(nameToCheck);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/channel/{id}",
                        savedChannel.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<ChannelEntity> findedChannel = channelRepository
                .findById(savedChannel.getId());

        //then
        Assertions.assertTrue(findedChannel.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() throws Exception{
        //given
        long idForDelete = 5L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/channel/{id}",
                        idForDelete))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }
}
