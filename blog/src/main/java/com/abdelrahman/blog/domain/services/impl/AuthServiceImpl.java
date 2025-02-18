package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.DTOs.CreateUserDTO;
import com.abdelrahman.blog.domain.constants.ApplicationConstants;
import com.abdelrahman.blog.domain.model.Authority;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.repos.UserRepository;
import com.abdelrahman.blog.domain.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService , ApplicationConstants {
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public String authenticate(String email, String password) {
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(email, password);
            Authentication authenticationResponse = authenticationManager.authenticate(authentication);
            if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
                if (null != env) {
                    String secret = env.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    return Jwts
                            .builder()
                            .issuer("Blog").subject("JWT Token")
                            .claim("email", authenticationResponse.getName())
                            .claim("authorities", authenticationResponse.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                            .issuedAt(new java.util.Date())
                            .expiration(new java.util.Date((new java.util.Date()).getTime() + 432_000_000))
                            .signWith(secretKey).compact();
                }
            }
            throw new BadCredentialsException("Invalid email or password");
    }

    @Override
    @Transactional
    public User registerUser(CreateUserDTO createUserDTO) {
        String authorityName="ROLE_USER";
        String hashedPwd= passwordEncoder.encode(createUserDTO.getPassword());
        Authority authority=new Authority(authorityName);
        User newuser= User
                .builder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .age(createUserDTO.getAge())
                .password(hashedPwd)
                .authorities(Set.of(authority)).build();
        return userRepository.save(newuser);
    }

}

