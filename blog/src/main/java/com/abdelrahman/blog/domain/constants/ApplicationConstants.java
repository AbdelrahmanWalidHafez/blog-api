package com.abdelrahman.blog.domain.constants;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationConstants {
    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    @Value("spring.jwt")
    public static String JWT_SECRET_DEFAULT_VALUE;
    public static final String JWT_HEADER = "Authorization";
    @Value("spring.security.oauth2.client.registration.google.client-id")
    public static String CLIENT_ID;
    @Value("spring.security.oauth2.client.registration.google.client-secret")
    public static String CLIENT_SECRET;
}
