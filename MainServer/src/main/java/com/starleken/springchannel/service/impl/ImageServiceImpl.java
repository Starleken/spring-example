package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.service.ImageService;
import com.starleken.springchannel.utils.ExceptionUtils;
import com.starleken.springchannel.utils.ImageNameGetterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static com.starleken.springchannel.utils.ExceptionUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RestTemplate rest;
    private final String serverUrl = "http://localhost:8081/api/v1";
    private final String apiPrefix = "/image";

    @Override
    public String uploadImage(MultipartFile file) {
        try{
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes());

            ResponseEntity<String> response = rest.exchange(serverUrl + apiPrefix,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is5xxServerError()){
                throwServerIsUnavailableException("Image");
            }

            String url = response.getBody();
            log.info("Saved image by url: " + url);
            return url;
        } catch (Exception ex){
            log.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        rest.delete(serverUrl + apiPrefix + "/" + ImageNameGetterUtils.getFromUrl(imageUrl));
    }
}
