package com.abdelrahman.blog.domain.DTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private UUID id;
    private String content;
    private UUID postId;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}