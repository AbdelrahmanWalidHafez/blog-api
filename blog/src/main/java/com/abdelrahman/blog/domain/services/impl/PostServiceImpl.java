package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.CreatePostRequest;
import com.abdelrahman.blog.domain.PostStatus;
import com.abdelrahman.blog.domain.UpdatePostRequest;
import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.Tag;
import com.abdelrahman.blog.domain.model.User;
import com.abdelrahman.blog.domain.repos.PostRepository;
import com.abdelrahman.blog.domain.services.CategoryService;
import com.abdelrahman.blog.domain.services.PostService;
import com.abdelrahman.blog.domain.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    @Override
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(UUID categoryId, UUID tagId, int pageNum, String sortDir,String sortField) {
        int pageSize=5;
        Pageable pageable= PageRequest.of
                (pageNum-1
                        ,pageSize
                        ,sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending());
        if (categoryId!=null&&tagId!=null){
            Category category=categoryService.getCategoryById(categoryId);
            Tag tag=tagService.getTagById(tagId);
           return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED,category,tag,pageable);
        }
        if (categoryId != null){
            Category category=categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED,category,pageable);
        }
        if (tagId != null){
            Tag tag=tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED,tag,pageable);
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED,pageable);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post post=new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        post.setStatus(createPostRequest.getPostStatus());
        post.setAuthor(user);
        post.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        post.setLikesCount(0);
        Category category=categoryService.getCategoryById(createPostRequest.getCategoryId());
        post.setCategory(category);
        Set<UUID>tagIds=createPostRequest.getTagsIds();
        List<Tag>tags=tagService.getTagByIds(tagIds);
        post.setTags((new HashSet<>(tags)));
        post.setComments(new HashSet<>());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest,User user)  {
        Post exisitingPost=postRepository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("post does not exist with id"+id));
        if(!exisitingPost.getAuthor().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not authorized to update this post");
        }
        exisitingPost.setTitle(updatePostRequest.getTitle());
        exisitingPost.setContent(updatePostRequest.getContent());
        exisitingPost.setStatus(updatePostRequest.getStatus());
        exisitingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));
        exisitingPost.setCategory(categoryService.getCategoryById(updatePostRequest.getCategoryId()));
        exisitingPost.setTags(new HashSet<>(tagService.getTagByIds(updatePostRequest.getTagsIds())));
        return postRepository.save(exisitingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("Post does not exist with id"+id));
    }

    @Override
    @Transactional
    public void deletePost(UUID id,User user) {
        Post exisitingPost = postRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post does not exist with id" + id));
        if (!exisitingPost.getAuthor().getId().equals(user.getId()) &&user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals("ADMIN"))) {
            throw new AccessDeniedException("you cant delete another user post");
        }
        postRepository.delete(exisitingPost);
    }

    @Override
    @Transactional
    public void toggleLike(UUID id) {
        Post exisitingPost = postRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post does not exist with id" + id));
        exisitingPost.setLikesCount(exisitingPost.getLikesCount()+1);
        postRepository.save(exisitingPost);
    }

    @Override
    public List<Post> search(String keyWord) {
        return postRepository.search(keyWord);
    }

    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        final int AVERAGE_WPM = 225;
        String[] words = content.trim().split("\\s+");
        int wordCount = words.length;
        int sentenceCount = content.split("[.!?]+").length;
        int complexWordCount = 0;
        for (String word : words) {
            if (countSyllables(word) > 3) {
                complexWordCount++;
            }
        }
        double baseMinutes = (double) wordCount / AVERAGE_WPM;
        double complexityFactor = 1 + (complexWordCount * 0.02) + (sentenceCount * 0.01);
        double adjustedTime = baseMinutes * complexityFactor;
        return (int) Math.ceil(adjustedTime * 60);
    }
    private int countSyllables(String word) {
        word = word.toLowerCase();
        String[] vowels = {"a", "e", "i", "o", "u", "y"};
        int syllableCount = 0;
        boolean lastWasVowel = false;
        for (char c : word.toCharArray()) {
            boolean isVowel = Arrays.toString(vowels).indexOf(c) != -1;
            if (isVowel && !lastWasVowel) {
                syllableCount++;
            }
            lastWasVowel = isVowel;
        }
        if (word.endsWith("e") && syllableCount > 1) {
            syllableCount--;
        }
        return Math.max(syllableCount, 1);
    }
}
