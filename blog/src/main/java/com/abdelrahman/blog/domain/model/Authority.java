package com.abdelrahman.blog.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "authorities")
public class Authority{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Authority(String name) {
        this.name = name;
    }
}
