package com.starleken.springchannel.core.db;

import com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils;
import com.starleken.springchannel.entity.ChannelEntity;
import com.starleken.springchannel.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.starleken.springchannel.core.utils.entityUtils.ChannelEntityUtils.*;

@Component
public class ChannelDbHelper {

    private ChannelRepository channelRepository;

    @Autowired
    public ChannelDbHelper(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public void clearDB(){
        channelRepository.deleteAll();
    }

    public ChannelEntity saveChannel(){
        return channelRepository.save(generateChannel());
    }

    public Optional<ChannelEntity> findChannelById(long id){
        return channelRepository.findById(id);
    }
}
