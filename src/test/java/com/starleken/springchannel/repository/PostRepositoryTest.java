package com.starleken.springchannel.repository;

import com.starleken.springchannel.core.db.PostDbHelper;
import com.starleken.springchannel.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.starleken.springchannel.core.utils.entityUtils.PostEntityUtils.*;

@SpringBootTest
public class PostRepositoryTest {

    private PostRepository postRepository;
    private PostDbHelper helper;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, PostDbHelper helper) {
        this.postRepository = postRepository;
        this.helper = helper;
    }

    @BeforeEach
    void setUp() {
        helper.clearDB();
    }

    @Test
    void findAll_happyPath() {
        //given
        PostEntity savedPost1 = helper.savePost();
        PostEntity savedPost2 = helper.savePost();

        //when
        List<PostEntity> findedPosts = postRepository.findAll();

        //then
        Assertions.assertNotNull(findedPosts);
        Assertions.assertEquals(2, findedPosts.size());
        Assertions.assertEquals(savedPost1.getTitle(), findedPosts.get(0).getTitle());
        Assertions.assertEquals(savedPost2.getTitle(), findedPosts.get(1).getTitle());
    }

    @Test
    void findById_happyPath() {
        //given
        PostEntity savedPost = helper.savePost();

        //when
        Optional<PostEntity> findedPost = postRepository.findById(savedPost.getId());

        //then
        Assertions.assertTrue(findedPost.isPresent());
        Assertions.assertEquals(savedPost.getTitle(), findedPost.get().getTitle());
    }

    @Test
    void create_happyPath() {
        //given
        PostEntity postToCreate = generatePostWithoutChannel();
        postToCreate.setChannel(helper.saveChannel());

        //when
        PostEntity savedPost = postRepository.save(postToCreate);

        //then
        Assertions.assertNotNull(savedPost);
        Assertions.assertEquals(postToCreate.getTitle(), savedPost.getTitle());
    }

    @Test
    void update_happyPath() {
        //given
        PostEntity savedPost = helper.savePost();
        savedPost.setTitle("new title");

        //when
        PostEntity updatedPost = postRepository.save(savedPost);

        //then
        Assertions.assertNotNull(updatedPost);
        Assertions.assertEquals(savedPost.getTitle(), updatedPost.getTitle());
    }

    @Test
    void delete_happyPath() {
        //given
        PostEntity savedPost = helper.savePost();

        //when
        postRepository.deleteById(savedPost.getId());
        Optional<PostEntity> postToCheck = postRepository.findById(savedPost.getId());

        //then
        Assertions.assertTrue(postToCheck.isEmpty());
    }
}
