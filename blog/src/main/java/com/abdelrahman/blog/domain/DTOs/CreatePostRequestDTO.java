package com.abdelrahman.blog.domain.DTOs;

import com.abdelrahman.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 200,message = "Title must be between {min} and {max} characters")
    private String title;
    @NotBlank
    @Size(min = 10,max = 5000,message = "Title must be between{min} and {max} characters")
    private String content;
    @NotNull(message = "Category id is required")
    private UUID categoryId;
    @Builder.Default
    @Size(max = 10,message = "Maximum {max} tags allowed")
    private Set<UUID> tagsIds=new HashSet<>();
    @NotNull(message ="Status is required" )
    private PostStatus postStatus;
}
