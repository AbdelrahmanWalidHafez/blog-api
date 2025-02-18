package com.abdelrahman.blog.domain.controllers;

import com.abdelrahman.blog.domain.DTOs.CreateUserDTO;
import com.abdelrahman.blog.domain.DTOs.LoginRequestDTO;
import com.abdelrahman.blog.domain.DTOs.LoginResponseDTO;
import com.abdelrahman.blog.domain.DTOs.UserDTO;
import com.abdelrahman.blog.domain.constants.ApplicationConstants;
import com.abdelrahman.blog.domain.mappers.UserMapper;
import com.abdelrahman.blog.domain.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path ="/api/v1/auth" )
@RequiredArgsConstructor

public class AuthController implements ApplicationConstants {
    private final AuthService authService;
    private final UserMapper userMapper;
   @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
       String jwt= authService.authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
       Cookie cookie = new Cookie("JWT-TOKEN", jwt);
       cookie.setHttpOnly(true);
       cookie.setSecure(true);
       cookie.setPath("/");
       cookie.setMaxAge(3600);
       response.addCookie(cookie);
       LoginResponseDTO responseDTO=LoginResponseDTO
               .builder().
               status(HttpStatus.OK.getReasonPhrase())
               .message("login successful login")
               .build();
       return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
   }  @PostMapping("/register")
   public ResponseEntity<String> register(@Valid @RequestBody CreateUserDTO createUserDTO)
   {
      UserDTO savedUser=userMapper.toUserDTO(authService.registerUser(createUserDTO));
      return new ResponseEntity<>("user created !",HttpStatus.CREATED);
   }
   @PostMapping("/logout")
   public ResponseEntity<String> logout(HttpServletResponse response) {

      Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
      jwtCookie.setHttpOnly(true);
      jwtCookie.setSecure(false);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge(0);
      response.addCookie(jwtCookie);
      return ResponseEntity.ok("Logout successful");
   }

}

