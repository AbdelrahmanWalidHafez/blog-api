package com.abdelrahman.blog.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private List<FieldError>errors;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class FieldError{
        String field;
        String message;
    }
}
