package com.abdelrahman.blog.domain.controllers;

import com.abdelrahman.blog.domain.CreatePostRequest;
import com.abdelrahman.blog.domain.DTOs.CreatePostRequestDTO;
import com.abdelrahman.blog.domain.DTOs.PostDTO;
import com.abdelrahman.blog.domain.DTOs.UpdatePostRequestDTO;
import com.abdelrahman.blog.domain.UpdatePostRequest;
import com.abdelrahman.blog.domain.mappers.PostMapper;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.services.PostService;
import com.abdelrahman.blog.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "sortField", defaultValue = "UpdatedAt") String sortField) {
        Page<Post> posts = postService.getAllPosts(categoryId, tagId, pageNum, sortDir, sortField);
        List<Post> postPage = posts.getContent();
        List<PostDTO> postDTOS = postPage
                .stream()
                .filter(Objects::nonNull)
                .map(postMapper::toDto).
                toList();
        return ResponseEntity.ok(postDTOS);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDTO>> getDrafts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        List<Post> userPosts = postService.getDraftPosts(loggedInUser);
        List<PostDTO> postDTOS = userPosts
                .stream()
                .filter(Objects::nonNull)
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(postDTOS);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid CreatePostRequestDTO createPostRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDTO);
        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        PostDTO createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDTO>updatePost(@PathVariable UUID id, @RequestBody @Valid UpdatePostRequestDTO updatePostRequestDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        UpdatePostRequest updatePostRequest=postMapper.toUpdatePostRequest(updatePostRequestDTO);
        Post updatedPost=postService.updatePost(id,updatePostRequest,loggedInUser);
        PostDTO updatedPostDTO=postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatedPostDTO);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDTO>getPost(@PathVariable UUID id){
        Post post=postService.getPost(id);
        PostDTO postDTO=postMapper.toDto(post);
        return ResponseEntity.ok(postDTO);
    }
    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void>deletePost(@PathVariable UUID id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        postService.deletePost(id,loggedInUser);
        return ResponseEntity.noContent().build();
    }
   @PutMapping(path = "/like/{id}")
    public ResponseEntity<String>toggleLike(@PathVariable UUID id){
        postService.toggleLike(id);
        return ResponseEntity.ok("post liked successfully");
   }
   @GetMapping(path="/search")
    public ResponseEntity<List<PostDTO>>search(@RequestParam String keyWord){
        List<PostDTO>posts=postService.search(keyWord).stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(posts);
   }

}
