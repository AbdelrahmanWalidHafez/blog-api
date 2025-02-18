package com.abdelrahman.blog.domain.services;

import com.abdelrahman.blog.domain.DTOs.CreateUserDTO;
import com.abdelrahman.blog.domain.model.User;


public interface AuthService {
    String authenticate(String email, String password);
    User registerUser(CreateUserDTO createUserDTO);
}
