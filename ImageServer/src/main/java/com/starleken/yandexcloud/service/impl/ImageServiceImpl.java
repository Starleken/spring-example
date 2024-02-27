package com.starleken.yandexcloud.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.starleken.yandexcloud.exception.CloudStorageIsUnavailableException;
import com.starleken.yandexcloud.exception.ImageIsNullException;
import com.starleken.yandexcloud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 objectStorage;

    private String bucketName = "cloud-image-storage";

    @Override
    public String saveImage(byte[] imageBytes) {

        if (imageBytes == null){
            throw new ImageIsNullException("Image is null");
        }

        String uuid = UUID.randomUUID().toString() + ".jpg";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);

        ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);

        try{
            objectStorage.putObject(bucketName, uuid, stream, metadata);

            return objectStorage.getUrl(bucketName, uuid).toExternalForm();
        } catch(Exception ex){
            throw new CloudStorageIsUnavailableException("Cloud storage is unavailable");
        }
    }

    @Override
    public void deleteImage(String imageURL) {
        objectStorage.deleteObject(bucketName, imageURL);
    }
}
