package com.abdelrahman.blog.domain.repos;

import com.abdelrahman.blog.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Set<Comment> findCommentsByPostIdOrderByCreatedAtDesc(UUID id);
}
