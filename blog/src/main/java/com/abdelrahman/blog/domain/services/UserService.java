package com.abdelrahman.blog.domain.services;

import com.abdelrahman.blog.domain.model.User;



public interface UserService {
    User getUserByEmail(String email);
}
