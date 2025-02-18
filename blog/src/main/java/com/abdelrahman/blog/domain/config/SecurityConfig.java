package com.abdelrahman.blog.domain.config;

import com.abdelrahman.blog.domain.constants.ApplicationConstants;
import com.abdelrahman.blog.domain.filter.CsrfCookieFilter;
import com.abdelrahman.blog.domain.filter.JWTTokenValidationFilter;
import com.abdelrahman.blog.domain.handlers.CustomAccessDeniedHandler;
import com.abdelrahman.blog.domain.handlers.Oauth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements ApplicationConstants {
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .anonymous(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> {
            CorsConfiguration corsConfiguration=new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            corsConfiguration.setExposedHeaders(List.of("Authorization"));
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        }))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/v1/auth/**","/error"))
                .sessionManagement(scm->scm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTTokenValidationFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth-> auth
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/**","/error").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/categories").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/categories").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/categories/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/v1/tags").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/tags").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/tags/**").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").authenticated()
                                .requestMatchers(HttpMethod.GET,"/api/v1/posts/drafts").hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/posts/like/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/posts/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/posts/**").authenticated()
                                .requestMatchers(HttpMethod.GET,"/api/v1/comments/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/comments/**").authenticated()
                                .requestMatchers(HttpMethod.PUT,"/api/v1/posts/**").hasAuthority("USER")
                                .requestMatchers("/error").permitAll()
                ).oauth2Login(oauth2->oauth2.successHandler(oauth2LoginSuccessHandler))
                .requiresChannel(channelRequestMatcherRegistry ->channelRequestMatcherRegistry
                                .anyRequest()
                                .requiresSecure());
        http.exceptionHandling(ehc->ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        BlogUsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider =
                new BlogUsernamePasswordAuthenticationProvider(userDetailsService,passwordEncoder);
        return new ProviderManager(usernamePasswordAuthenticationProvider);
    }
    @Bean
    CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
    @Bean
    ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration google=googleCLientRegistration();
        return new InMemoryClientRegistrationRepository(google);
    }

    private ClientRegistration googleCLientRegistration(){
       return CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
               .build();
    }
}
