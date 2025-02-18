package com.abdelrahman.blog.domain.handlers;

import com.abdelrahman.blog.domain.constants.ApplicationConstants;
import com.abdelrahman.blog.domain.model.Authority;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.repos.UserRepository;
import com.abdelrahman.blog.domain.services.EmailSenderService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements ApplicationConstants {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Environment env;

    private final EmailSenderService emailSenderService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Authority authority = new Authority("ROLE_USER");
            String hashedPwd = passwordEncoder.encode(generatePwd());

            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .password(hashedPwd)
                    .authorities(Set.of(authority))
                    .build();

            return userRepository.save(newUser);
        });
        if (null != env) {
            String secret = env.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            String jwt=Jwts
                    .builder()
                    .issuer("Blog").subject("JWT Token")
                    .claim("email", email)
                    .claim("authorities",user.getAuthorities().stream()
                            .map(Authority::getName)
                            .collect(Collectors.toList()))
                    .issuedAt(new java.util.Date())
                    .expiration(new java.util.Date((new java.util.Date()).getTime() + 432_000_000))
                    .signWith(secretKey)
                    .compact();
            Cookie cookie = new Cookie("JWT-TOKEN", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            emailSenderService.sendEmail(email);
        }
    }
    private String generatePwd(){
         String ALL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
        SecureRandom RANDOM = new SecureRandom();
         int PASSWORD_LENGTH = 12;
        return IntStream.range(0, PASSWORD_LENGTH)
                .mapToObj(i -> String.valueOf(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length()))))
                .collect(Collectors.joining());
    }
}

