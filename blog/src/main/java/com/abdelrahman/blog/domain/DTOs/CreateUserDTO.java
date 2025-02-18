package com.abdelrahman.blog.domain.DTOs;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    @Email
    private String email;
    @NotBlank(message = "the name is required")
    private String name;
    @Pattern( regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one digit, and one special character.")
    private String password;
    @Min(value=13,message = "you must to be older than 13 year to register")
    private int age;
}
