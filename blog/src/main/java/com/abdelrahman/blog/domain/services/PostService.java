package com.abdelrahman.blog.domain.services;

import com.abdelrahman.blog.domain.CreatePostRequest;
import com.abdelrahman.blog.domain.UpdatePostRequest;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Page<Post> getAllPosts(UUID categoryId, UUID tagId, int pageNum, String sortDir,String sortField);
    public List<Post>getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest,User user);
    Post getPost(UUID id);
    void deletePost(UUID id,User user);
    void toggleLike(UUID id);
    List<Post> search(String keyWord);
}
