package com.starleken.springchannel.controller;

import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.dto.post.PostUpdateDto;
import com.starleken.springchannel.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Posts")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Get all posts", description = "Return all posts")
    @ApiResponse(responseCode = "200", description = "posts are found")
    @GetMapping()
    public List<PostFullDto> findAll(){
        return postService.findAll();
    }

    @Operation(summary = "find a post by id", description = "return a post by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "post is found"),
            @ApiResponse(responseCode = "404", description = "post is not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostFullDto> findById(@Parameter(required = true, example = "1") @PathVariable Long id){
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post is created"),
            @ApiResponse(responseCode = "404", description = "Channel is not found")
    })
    @PostMapping()
    public ResponseEntity<PostFullDto> create(@RequestBody PostCreateDto dto){
        return new ResponseEntity<>(postService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post is updated"),
            @ApiResponse(responseCode = "404", description = "Post is not found")
    })
    @PutMapping
    public ResponseEntity<PostFullDto> update(@RequestBody PostUpdateDto dto){
        return new ResponseEntity<>(postService.update(dto), HttpStatus.OK);
    }

    @Operation(summary = "Delete a post by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post is deleted"),
            @ApiResponse(responseCode = "404", description = "Post is not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        postService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
