package com.starleken.springchannel.repository;

import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.EntityGenerationUtils.*;

@SpringBootTest
public class PostRepositoryTest {

    private PostRepository postRepository;
    private ChannelRepository channelRepository;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, ChannelRepository channelRepository) {
        this.postRepository = postRepository;
        this.channelRepository = channelRepository;
    }

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        channelRepository.deleteAll();
    }

    @Test
    void findAll_happyPath() {
        //given
        List<PostEntity> posts = generatePosts();
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);

        for (var post : posts){
            post.setChannel(savedChannel);
        }
        postRepository.saveAll(posts);

        //when
        List<PostEntity> findedPosts = postRepository.findAll();

        //then
        Assertions.assertNotNull(findedPosts);
        Assertions.assertEquals(3, findedPosts.size());
        Assertions.assertEquals(findedPosts.get(0).getTitle(), posts.get(0).getTitle());
        Assertions.assertEquals(findedPosts.get(1).getTitle(), posts.get(1).getTitle());
        Assertions.assertEquals(findedPosts.get(2).getTitle(), posts.get(2).getTitle());
    }

    @Test
    void findById_happyPath() {
        //given
        PostEntity post = generatePost();
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);

        post.setChannel(savedChannel);
        PostEntity savedPost = postRepository.save(post);

        //when
        Optional<PostEntity> findedPost = postRepository.findById(savedPost.getId());

        //then
        Assertions.assertTrue(findedPost.isPresent());
        Assertions.assertEquals(savedPost.getTitle(), findedPost.get().getTitle());
    }

    @Test
    void create_happyPath() {
        //given
        PostEntity post = generatePost();
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);

        post.setChannel(savedChannel);

        //when
        PostEntity savedPost = postRepository.save(post);

        //then
        Assertions.assertNotNull(savedPost);
        Assertions.assertEquals(post.getTitle(), savedPost.getTitle());
    }

    @Test
    void update_happyPath() {
        //given
        PostEntity post = generatePost();
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);
        post.setChannel(savedChannel);

        PostEntity savedPost = postRepository.save(post);
        savedPost.setTitle("hfdbdfb");

        //when
        PostEntity updatedPost = postRepository.save(savedPost);

        //then
        Assertions.assertNotNull(updatedPost);
        Assertions.assertEquals(savedPost.getTitle(), updatedPost.getTitle());
    }

    @Test
    void delete_happyPath() {
        //given
        PostEntity post = generatePost();
        ChannelEntity channel = generateChannel();
        ChannelEntity savedChannel = channelRepository.save(channel);
        post.setChannel(savedChannel);

        PostEntity savedPost = postRepository.save(post);

        //when
        postRepository.deleteById(savedPost.getId());
        Optional<PostEntity> postToCheck = postRepository.findById(savedPost.getId());

        //then
        Assertions.assertTrue(postToCheck.isEmpty());
    }
}
