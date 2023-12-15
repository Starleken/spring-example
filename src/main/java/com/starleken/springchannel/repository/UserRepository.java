package com.starleken.springchannel.repository;

import com.starleken.springchannel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByLogin(String login);

    public UserEntity findByEmail(String email);


}
