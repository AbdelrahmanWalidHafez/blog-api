package com.abdelrahman.blog.domain.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentRequestDTO {
    @NotNull
    UUID id;
    @NotBlank
    @Size(min = 2,max = 2000,message = "comment must be between {min} and {max} characters")
    private String content;
}
