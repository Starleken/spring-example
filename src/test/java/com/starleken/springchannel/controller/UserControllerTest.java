package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starleken.springchannel.core.db.UserDbHelper;
import com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls;
import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.repository.UserRepository;
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

import static com.starleken.springchannel.core.utils.dtoUtils.UserDtoUtls.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserDbHelper helper;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserDbHelper helper, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.helper = helper;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        helper.clearDB();
    }

    @Test
    void findAll_happyPath() throws Exception {
        //given
        helper.saveUser();
        helper.saveUser();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();

        List<UserFullDto> dtos = objectMapper.readValue(bytes, new TypeReference<List<UserFullDto>>() {
        });

        //then
        Assertions.assertNotNull(dtos);
        Assertions.assertEquals(2, dtos.size());
    }

    @Test
    void findById_happyPath() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}",
                        savedUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        UserFullDto findedUser = objectMapper.readValue(bytes, UserFullDto.class);

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(savedUser.getId(), findedUser.getId());
    }

    @Test
    void findById_whenNotFound() throws Exception{
        //given
        long idToSearch = 5L;

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}",
                        idToSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void findByLogin_happyPath() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/login?login={login}",
                        savedUser.getLogin()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        UserFullDto findedUser = objectMapper.readValue(bytes, UserFullDto.class);

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(savedUser.getLogin(), findedUser.getLogin());
    }

    @Test
    void findByLogin_whenNotFound() throws Exception {
        //given
        String loginToSearch = "starleken";

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login?login={login}",
                        loginToSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        UserCreateDto createDto = generateUserCreateDto();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        UserFullDto savedUser = objectMapper.readValue(bytes, UserFullDto.class);

        //then
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(createDto.getLogin(), savedUser.getLogin());
    }

    @Test
    void create_whenEmailIsInvalid() throws Exception{
        //given
        UserCreateDto createDto = generateInvalidUserCreateDto();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void create_whenLoginIsExists() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();

        UserCreateDto createDto = generateUserCreateDto();
        createDto.setLogin(savedUser.getLogin());

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void create_whenEmailIsExists() throws Exception {
        //given
        UserEntity savedUser = helper.saveUser();

        UserCreateDto createDto = generateUserCreateDto();
        createDto.setEmail(savedUser.getEmail());

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void update_happyPath() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();
        UserUpdateDto updateDto = generateUserUpdateDto(savedUser.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/user").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        UserFullDto dto = objectMapper.readValue(bytes, UserFullDto.class);

        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(updateDto.getId(), dto.getId());
        Assertions.assertEquals(updateDto.getImageUrl(), dto.getImageURL());
    }

    @Test
    void update_whenNotFound() throws Exception{
        //given
        UserUpdateDto updateDto = generateUserUpdateDto(1L);

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_whenEmailIsTaken() throws Exception {
        //given
        UserEntity savedUser = helper.saveUser();

        UserEntity userToUpdate = helper.saveUser();

        UserUpdateDto updateDto = generateUserUpdateDto(userToUpdate.getId());
        updateDto.setEmail(savedUser.getEmail());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/user").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void update_whenEmailIsInvalid() throws Exception {
        //given
        helper.saveUser();

        UserEntity userToUpdate = helper.saveUser();

        UserUpdateDto updateDto = generateInvalidUserUpdateDto(userToUpdate.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/user").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void changePassword_happyPath() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();
        ChangePasswordDto changeDto = generateChangePasswordDto(savedUser.getId(), savedUser.getPassword());

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<UserEntity> findedUser = helper.findUserById(savedUser.getId());

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(changeDto.getNewPassword(), findedUser.get().getPassword());
    }

    @Test
    void changePassword_whenNotFound() throws Exception{
        //given
        ChangePasswordDto dto = generateChangePasswordDto(5L, "starleken");

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void changePassword_whenIncorrectPassword() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();
        ChangePasswordDto changeDto = generateChangePasswordDto(
                savedUser.getId(), savedUser.getPassword() + "1241");

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        UserEntity savedUser = helper.saveUser();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}",
                savedUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<UserEntity> findedUser = helper.findUserById(savedUser.getId());

        //then
        Assertions.assertTrue(findedUser.isEmpty());
    }

    @Test
    void deleteById_whenNotFound() throws Exception {
        //given
        long idForDelete = 5L;

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}",
                        idForDelete))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }
}
