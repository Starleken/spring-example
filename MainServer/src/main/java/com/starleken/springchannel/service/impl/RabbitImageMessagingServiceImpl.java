package com.starleken.springchannel.service.impl;

import com.starleken.springchannel.service.ImageMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class RabbitImageMessagingServiceImpl implements ImageMessagingService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public String uploadImage(MultipartFile file) {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        MessageProperties props = new MessageProperties();
        try{
            Message message = messageConverter.toMessage(file.getBytes(), props);
            rabbitTemplate.send("MainServer.image", message);
        } catch (Exception ex){
            log.info(ex.getMessage());
        }
        return null;
    }

    @Override
    public void deleteImage(String imageUrl) {
    }
}
