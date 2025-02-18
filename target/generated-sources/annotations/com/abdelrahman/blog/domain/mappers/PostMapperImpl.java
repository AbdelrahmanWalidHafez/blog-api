package com.abdelrahman.blog.domain.mappers;

import com.abdelrahman.blog.domain.CreatePostRequest;
import com.abdelrahman.blog.domain.DTOs.CategoryDTO;
import com.abdelrahman.blog.domain.DTOs.CreatePostRequestDTO;
import com.abdelrahman.blog.domain.DTOs.PostDTO;
import com.abdelrahman.blog.domain.DTOs.TagResponse;
import com.abdelrahman.blog.domain.DTOs.UpdatePostRequestDTO;
import com.abdelrahman.blog.domain.UpdatePostRequest;
import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.model.Comment;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.Tag;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-14T00:30:11+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDTO toDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO.PostDTOBuilder postDTO = PostDTO.builder();

        postDTO.author( mapAuthor( post.getAuthor() ) );
        postDTO.categoryDTO( categoryToCategoryDTO( post.getCategory() ) );
        postDTO.tagResponseSet( tagSetToTagResponseSet( post.getTags() ) );
        postDTO.id( post.getId() );
        postDTO.title( post.getTitle() );
        postDTO.readingTime( post.getReadingTime() );
        Set<Comment> set1 = post.getComments();
        if ( set1 != null ) {
            postDTO.comments( new LinkedHashSet<Comment>( set1 ) );
        }
        postDTO.createdAt( post.getCreatedAt() );
        postDTO.updatedAt( post.getUpdatedAt() );
        postDTO.status( post.getStatus() );

        return postDTO.build();
    }

    @Override
    public CreatePostRequest toCreatePostRequest(CreatePostRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CreatePostRequest.CreatePostRequestBuilder createPostRequest = CreatePostRequest.builder();

        createPostRequest.categoryId( dto.getCategoryId() );
        createPostRequest.title( dto.getTitle() );
        createPostRequest.content( dto.getContent() );
        Set<UUID> set = dto.getTagsIds();
        if ( set != null ) {
            createPostRequest.tagsIds( new LinkedHashSet<UUID>( set ) );
        }
        createPostRequest.postStatus( dto.getPostStatus() );

        return createPostRequest.build();
    }

    @Override
    public UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UpdatePostRequest.UpdatePostRequestBuilder updatePostRequest = UpdatePostRequest.builder();

        updatePostRequest.title( dto.getTitle() );
        updatePostRequest.content( dto.getContent() );
        updatePostRequest.categoryId( dto.getCategoryId() );
        Set<UUID> set = dto.getTagsIds();
        if ( set != null ) {
            updatePostRequest.tagsIds( new LinkedHashSet<UUID>( set ) );
        }
        updatePostRequest.status( dto.getStatus() );

        return updatePostRequest.build();
    }

    protected CategoryDTO categoryToCategoryDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO.CategoryDTOBuilder categoryDTO = CategoryDTO.builder();

        categoryDTO.id( category.getId() );
        categoryDTO.name( category.getName() );

        return categoryDTO.build();
    }

    protected TagResponse tagToTagResponse(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagResponse.TagResponseBuilder tagResponse = TagResponse.builder();

        tagResponse.id( tag.getId() );
        tagResponse.name( tag.getName() );

        return tagResponse.build();
    }

    protected Set<TagResponse> tagSetToTagResponseSet(Set<Tag> set) {
        if ( set == null ) {
            return null;
        }

        Set<TagResponse> set1 = LinkedHashSet.newLinkedHashSet( set.size() );
        for ( Tag tag : set ) {
            set1.add( tagToTagResponse( tag ) );
        }

        return set1;
    }
}
