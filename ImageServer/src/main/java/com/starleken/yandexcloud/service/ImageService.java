package com.starleken.yandexcloud.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public String saveImage(byte[] imageBytes);

    public void deleteImage(String imageName);
}
