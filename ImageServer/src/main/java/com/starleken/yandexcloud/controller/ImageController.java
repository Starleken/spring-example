package com.starleken.yandexcloud.controller;

import com.starleken.yandexcloud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    public String SaveImage(@RequestBody byte[] imageBytes){
        try{
            return imageService.saveImage(imageBytes);
        } catch (Exception ex){
            return null;
        }
    }

    @DeleteMapping("/{imageUrl}")
    public void deleteImage(@PathVariable String imageUrl){
        imageService.deleteImage(imageUrl);
    }
}
