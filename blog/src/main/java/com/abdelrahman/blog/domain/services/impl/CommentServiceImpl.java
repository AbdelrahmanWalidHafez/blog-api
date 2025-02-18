package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.DTOs.CreateCommentRequestDTO;
import com.abdelrahman.blog.domain.DTOs.UpdateCommentRequestDTO;
import com.abdelrahman.blog.domain.model.Comment;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.repos.CommentRepository;
import com.abdelrahman.blog.domain.repos.PostRepository;
import com.abdelrahman.blog.domain.services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(User user, UUID postId, CreateCommentRequestDTO createCommentRequestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
        Comment newComment = new Comment();
        newComment.setContent(createCommentRequestDTO.getContent());
        newComment.setUser(user);
        newComment.setPost(post);
        return commentRepository.save(newComment);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Set<Comment> listComments(UUID id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with ID: " + id);
        }
        return commentRepository.findCommentsByPostIdOrderByCreatedAtDesc(id);
    }

    @Override
    @Transactional
    public Comment updateComment(UUID id, User user, UpdateCommentRequestDTO updateCommentRequestDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new AccessDeniedException("you can not edit a comment that is not yours");
        }
        comment.setContent(updateCommentRequestDTO.getContent());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(UUID id, User user) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));
        if (!user.getId().equals(comment.getUser().getId())
                && user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals("ADMIN")) // Not an admin
                && !comment.getPost().getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to edit this comment.");
        }
        commentRepository.deleteById(id);
    }
}
