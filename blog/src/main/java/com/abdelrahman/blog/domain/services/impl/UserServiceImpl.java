package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.repos.UserRepository;
import com.abdelrahman.blog.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find a user with email: " + email));
    }
}
