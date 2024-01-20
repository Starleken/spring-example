package com.starleken.springchannel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "channel")
public class ChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private ChannelEntityType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostEntity> posts = new ArrayList<>();
}
