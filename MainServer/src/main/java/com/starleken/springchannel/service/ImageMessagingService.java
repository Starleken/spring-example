package com.starleken.springchannel.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageMessagingService {

    String uploadImage(MultipartFile file);

    void deleteImage(String imageUrl);
}
