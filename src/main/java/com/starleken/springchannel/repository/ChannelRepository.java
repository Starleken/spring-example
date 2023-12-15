package com.starleken.springchannel.repository;

import com.starleken.springchannel.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {
    ChannelEntity findOneByName(String name);
}
