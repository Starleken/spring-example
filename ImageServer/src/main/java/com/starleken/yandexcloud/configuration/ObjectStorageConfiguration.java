package com.starleken.yandexcloud.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectStorageConfiguration {

    private static final String accessKeyId = "YCAJEgPfPYYgZh-aeJmTJCpS2";
    private static final String secretAccessKey = "YCO6aMVzXHhwpkmwYJ_mRuAtZdhxRdxg8yMz8Mpe";

    @Bean
    public AmazonS3 objectStorage(){
        AmazonS3 build = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "https://storage.yandexcloud.net",
                                "ru-central1"
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();

        return build;
    }
}
