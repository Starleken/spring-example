package com.starleken.springchannel.core.db;

import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.entity.PostEntity;
import com.starleken.springchannel.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;

import java.util.Optional;

import static com.starleken.springchannel.core.utils.entityUtils.PostEntityUtils.generatePostWithoutChannel;

@Component
public class PostDbHelper {

    private PostRepository postRepository;

    private ChannelDbHelper channelHelper;

    @Autowired
    public PostDbHelper(PostRepository postRepository, ChannelDbHelper channelHelper) {
        this.postRepository = postRepository;
        this.channelHelper = channelHelper;
    }

    public void clearDB(){
        postRepository.deleteAll();
        channelHelper.clearDB();
    }

    public PostEntity savePost(){
        PostEntity post = generatePostWithoutChannel();

        post.setChannel(channelHelper.saveChannel());

        return postRepository.save(post);
    }

    public ChannelEntity saveChannel(){
        return channelHelper.saveChannel();
    }

    public Optional<PostEntity> findById(long id){
        return postRepository.findById(id);
    }
}
