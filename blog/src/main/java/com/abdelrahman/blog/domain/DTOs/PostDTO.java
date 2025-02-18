package com.abdelrahman.blog.domain.DTOs;

import com.abdelrahman.blog.domain.PostStatus;
import com.abdelrahman.blog.domain.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private UUID id;
    private String title;
    private String Content;
    private AuthorDTO author;
    private  CategoryDTO categoryDTO;
    private Set<TagResponse> tagResponseSet;
    private Integer readingTime;
    private Set<Comment>comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus status;
    private int likeCounts;
}
