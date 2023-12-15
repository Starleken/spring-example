package com.starleken.springchannel;

import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.entity.UserEntity;
import com.starleken.springchannel.entity.ChannelEntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityGenerationUtils {
    public static ChannelEntity generateChannel(){
        ChannelEntity channel = new ChannelEntity();
        channel.setName("Name#" + Math.random() * 100);
        channel.setType(ChannelEntityType.PROGRAMMING);

        return channel;
    }

    public static PostEntity generatePost(){
        PostEntity post = new PostEntity();
        post.setTitle("title " + Math.random() * 100);
        post.setContent("content " + Math.random() * 100);

        return post;
    }

    public static List<PostEntity> generatePosts(){
        List<PostEntity> posts = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            PostEntity post = new PostEntity();
            post.setTitle("title " + i);
            post.setContent("content " + i);

            posts.add(post);
        }

        return posts;
    }

    public static UserEntity generateUser(){
        UserEntity user = new UserEntity();
        user.setLogin("Login#" +  Math.random() * 100);
        user.setPassword("Password#" +  Math.random() * 100);
        user.setImageURL("Image#" +  Math.random() * 100);

        return user;
    }
}
