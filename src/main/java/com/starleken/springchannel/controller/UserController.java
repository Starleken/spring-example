package com.starleken.springchannel.controller;

import com.starleken.springchannel.dto.user.ChangePasswordDto;
import com.starleken.springchannel.dto.user.UserCreateDto;
import com.starleken.springchannel.dto.user.UserFullDto;
import com.starleken.springchannel.dto.user.UserUpdateDto;
import com.starleken.springchannel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User")
@RestController
@Validated
@RequestMapping("/users")
public class UserController{

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users are found")
    @GetMapping
    public List<UserFullDto> findAll(){
        return userService.findAll();
    }

    @Operation(summary = "Get a user by his id", description = "Return user by his id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is found"),
            @ApiResponse(responseCode = "404", description = "User is not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserFullDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get a user by his login", description = "Return user by his unique login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is found"),
            @ApiResponse(responseCode = "404", description = "User is not found")
    })
    @GetMapping("/login")
    public ResponseEntity<UserFullDto> findByLogin(@Parameter(required = true, example = "starleken") @RequestParam String login){
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @Operation(summary = "Create a user by UserCreateDto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User is created"),
            @ApiResponse(responseCode = "400", description = "User is not created")
    })
    @PostMapping()
    public ResponseEntity<UserFullDto> create(@Valid @RequestBody UserCreateDto dto){
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user by UserUpdateDto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User is not found")
    })
    @PutMapping()
    public ResponseEntity<UserFullDto> update(@Valid @RequestBody UserUpdateDto dto){
        return new ResponseEntity<>(userService.update(dto), HttpStatus.OK);
    }

    @Operation(summary = "Change password", description = "Change password for user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password is changed"),
            @ApiResponse(responseCode = "404", description = "User is not found"),
            @ApiResponse(responseCode = "400", description = "Incorrect password")
    })
    @PutMapping("/password")
    public ResponseEntity<UserFullDto> changePassword(@Valid @RequestBody ChangePasswordDto dto){
        return new ResponseEntity<>(userService.changePassword(dto), HttpStatus.OK);
    }

    @Operation(summary = "Delete user by his id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is deleted"),
            @ApiResponse(responseCode = "404", description = "User is not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByid(@PathVariable Long id){
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
