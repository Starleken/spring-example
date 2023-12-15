package com.starleken.springchannel.controller;
import com.starleken.springchannel.dto.channel.ChannelCreateDto;
import com.starleken.springchannel.dto.channel.ChannelFullDto;
import com.starleken.springchannel.dto.channel.ChannelPreviewDto;
import com.starleken.springchannel.dto.channel.ChannelUpdateDto;
import com.starleken.springchannel.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Channel")
@RestController
@RequestMapping("/channel")
public class ChannelController {

    private ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Operation(summary = "Get all channels", description = "Return all channels")
    @ApiResponse(responseCode = "200", description = "Channels are found")
    @GetMapping()
    public List<ChannelPreviewDto> findAll(){
        return channelService.findAll();
    }

    @Operation(summary = "Get a channel by its id", description = "Return a channel by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Channel is found"),
            @ApiResponse(responseCode = "404", description = "Channel is not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<ChannelFullDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(channelService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a channel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Channel is created"),
            @ApiResponse(responseCode = "400", description = "Name is taken")
    })
    @PostMapping()
    public ResponseEntity<ChannelFullDto> create(@Valid @RequestBody ChannelCreateDto dto){
        return new ResponseEntity<>(channelService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a channel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Channel is updated"),
            @ApiResponse(responseCode = "404", description = "Channel is not found"),
            @ApiResponse(responseCode = "400", description = "Name is taken")
    })
    @PutMapping()
    public ResponseEntity<ChannelFullDto> update(@Valid @RequestBody ChannelUpdateDto dto){
        return new ResponseEntity<>(channelService.update(dto), HttpStatus.OK);
    }

    @Operation(summary = "Delete a channel by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Channel is deleted"),
            @ApiResponse(responseCode = "404", description = "Channel is not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        channelService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
