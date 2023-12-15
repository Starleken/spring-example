package com.starleken.springchannel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.starleken.springchannel.EntityGenerationUtils.generateUser;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserRepository userRepository;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() throws Exception {
        //given
        UserEntity userToSave1 = generateUser();
        UserEntity userToSave2 = generateUser();

        userRepository.save(userToSave1);
        userRepository.save(userToSave2);

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
        UserEntity userToSave = generateUser();
        UserEntity savedUser = userRepository.save(userToSave);

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
        UserEntity userToSave = generateUser();
        UserEntity savedUser = userRepository.save(userToSave);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}",
                        savedUser.getId()+1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void findByLogin_happyPath() throws Exception{
        //given
        UserEntity userToSave = generateUser();
        UserEntity savedUser = userRepository.save(userToSave);

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
        String loginForSearch = "starleken";

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login?login={login}",
                loginForSearch))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void create_happyPath() throws Exception{
        //given
        UserEntity userToSave = generateUser();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        byte[] bytes = mvcResult.getResponse().getContentAsByteArray();
        UserFullDto savedUser = objectMapper.readValue(bytes, UserFullDto.class);

        //then
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(userToSave.getLogin(), savedUser.getLogin());
    }

    @Test
    void create_whenLoginIsExists() throws Exception{
        //given
        String loginToCheck = "starleken";

        UserEntity userToSave = generateUser();
        userToSave.setLogin(loginToCheck);
        userRepository.save(userToSave);

        UserCreateDto createDto = new UserCreateDto();
        createDto.setLogin(loginToCheck);
        createDto.setPassword("password");
        createDto.setEmail("starleken@mail.ru");
        createDto.setImageURL("https://image.jpg");

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
        String emailToCheck = "starleken@mail.ru";

        UserEntity userToSave = generateUser();
        userToSave.setEmail(emailToCheck);
        userRepository.save(userToSave);

        UserCreateDto createDto = new UserCreateDto();
        createDto.setLogin("starleken");
        createDto.setPassword("password");
        createDto.setEmail(emailToCheck);
        createDto.setImageURL("https://image.jpg");

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
        String imageToCheck = "http://image.jpg";

        UserEntity userToSave = generateUser();
        UserEntity savedUser = userRepository.save(userToSave);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setId(savedUser.getId());
        updateDto.setEmail(savedUser.getEmail());
        updateDto.setImageUrl(imageToCheck);

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
        Assertions.assertEquals(savedUser.getId(), dto.getId());
        Assertions.assertEquals(imageToCheck, dto.getImageURL());
    }

    @Test
    void update_whenNotFound() throws Exception{
        //given
        UserUpdateDto dto = new UserUpdateDto();
        dto.setId(1L);
        dto.setEmail("starleken@mail.ru");
        dto.setImageUrl("http://image.jpg");

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        //then
    }

    @Test
    void update_whenEmailIsTaken() throws Exception {
        //given
        String emailToCheck = "starleken@mail.ru";

        UserEntity userToSave = generateUser();
        userToSave.setEmail(emailToCheck);
        userRepository.save(userToSave);

        UserEntity userToUpdate = generateUser();
        UserEntity savedUser = userRepository.save(userToUpdate);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setId(savedUser.getId());
        updateDto.setEmail(emailToCheck);
        updateDto.setImageUrl(savedUser.getImageURL());

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
        String oldPassword = "old";
        String newPassword = "new";

        UserEntity userToSave = generateUser();
        userToSave.setPassword(oldPassword);
        UserEntity savedUser = userRepository.save(userToSave);

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);
        dto.setId(savedUser.getId());

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<UserEntity> findedUser = userRepository
                .findById(dto.getId());

        //then
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(newPassword, findedUser.get().getPassword());
    }

    @Test
    void changePassword_whenNotFound() throws Exception{
        //given
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setId(5L);
        dto.setOldPassword("old");
        dto.setNewPassword("new");

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
        String oldPassword = "old";
        String newPassword = "new";

        UserEntity userToSave = generateUser();
        userToSave.setPassword(oldPassword);
        UserEntity savedUser = userRepository.save(userToSave);

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword(oldPassword + "incorrect");
        dto.setNewPassword(newPassword);
        dto.setId(savedUser.getId());

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        //then
    }

    @Test
    void deleteById_happyPath() throws Exception{
        //given
        UserEntity userToSave = generateUser();
        UserEntity savedUser = userRepository.save(userToSave);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}",
                savedUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Optional<UserEntity> findedUser = userRepository.findById(savedUser.getId());

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
