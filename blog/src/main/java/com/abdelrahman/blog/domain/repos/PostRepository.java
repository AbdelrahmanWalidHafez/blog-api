package com.abdelrahman.blog.domain.repos;

import com.abdelrahman.blog.domain.PostStatus;
import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.Tag;
import com.abdelrahman.blog.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag, Pageable pageable);
    Page<Post> findAllByStatusAndCategory(PostStatus status, Category category,Pageable pageable);
    Page<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag, Pageable pageable);
    Page<Post> findAllByStatus(PostStatus status,Pageable pageable);
    List<Post>findAllByAuthorAndStatus(User user,PostStatus postStatus);
    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "EXISTS (SELECT t FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND t MEMBER OF p.tags)")
    List<Post> search(String keyWord);
}
