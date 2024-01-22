package com.starleken.yandexcloud.controller;

import com.starleken.yandexcloud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<String> SaveImage(@RequestBody byte[] imageBytes){
        return new ResponseEntity<>(imageService.saveImage(imageBytes), HttpStatus.OK);
    }

    @DeleteMapping("/{imageUrl}")
    public void deleteImage(@PathVariable String imageUrl){
        imageService.deleteImage(imageUrl);
    }
}
