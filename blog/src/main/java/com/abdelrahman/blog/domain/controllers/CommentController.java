package com.abdelrahman.blog.domain.controllers;

import com.abdelrahman.blog.domain.DTOs.CommentDTO;
import com.abdelrahman.blog.domain.DTOs.CreateCommentRequestDTO;
import com.abdelrahman.blog.domain.DTOs.UpdateCommentRequestDTO;
import com.abdelrahman.blog.domain.mappers.CommentMapper;
import com.abdelrahman.blog.domain.model.Comment;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.services.CommentService;
import com.abdelrahman.blog.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/comments")
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    @PostMapping("/{id}")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable UUID id, @Valid @RequestBody CreateCommentRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        Comment savedComment=commentService.createComment(loggedInUser,id,request);
        CommentDTO commentDTO=commentMapper.toDto(savedComment);
        return new ResponseEntity<>(commentDTO,HttpStatus.CREATED);
}
@GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>>listComments(@PathVariable UUID id){
    List<CommentDTO>comments=commentService
            .listComments(id)
            .stream()
            .filter(Objects::nonNull)
            .map(commentMapper::toDto)
            .toList();
    return ResponseEntity.ok(comments);
}
   @PutMapping("/{id}")
    public ResponseEntity<CommentDTO>updateComment(@PathVariable UUID id,@Valid @RequestBody UpdateCommentRequestDTO updateCommentRequestDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        CommentDTO commentDTO= commentMapper.toDto(commentService.updateComment(id,loggedInUser,updateCommentRequestDTO));
        return ResponseEntity.ok(commentDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteComment(@PathVariable UUID id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User loggedInUser = userService.getUserByEmail(authentication.getName());
        commentService.deleteComment(id,loggedInUser);
        return ResponseEntity.noContent().build();
    }

}
