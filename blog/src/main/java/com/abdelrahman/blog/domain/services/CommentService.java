package com.abdelrahman.blog.domain.services;

import com.abdelrahman.blog.domain.DTOs.CreateCommentRequestDTO;
import com.abdelrahman.blog.domain.DTOs.UpdateCommentRequestDTO;
import com.abdelrahman.blog.domain.model.Comment;

import com.abdelrahman.blog.domain.model.User;


import java.util.Set;
import java.util.UUID;

public interface CommentService {
    Comment createComment(User user, UUID postId, CreateCommentRequestDTO createCommentRequestDTO);
    Set<Comment> listComments(UUID id);
    Comment updateComment(UUID id, User user, UpdateCommentRequestDTO updateCommentRequestDTO);
    void deleteComment(UUID id,User user);
}
