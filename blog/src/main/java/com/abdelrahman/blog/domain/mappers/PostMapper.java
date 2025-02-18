package com.abdelrahman.blog.domain.mappers;

import com.abdelrahman.blog.domain.CreatePostRequest;
import com.abdelrahman.blog.domain.DTOs.*;
import com.abdelrahman.blog.domain.UpdatePostRequest;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author", qualifiedByName = "mapAuthor")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "tagResponseSet", source = "tags")
    PostDTO toDto(Post post);

    @Named("mapAuthor")
    default AuthorDTO mapAuthor(User author) {
        if (author == null) return null;
        return new AuthorDTO(author.getId(), author.getName());
    }
    @Mapping(target = "categoryId", source = "categoryId")
    CreatePostRequest  toCreatePostRequest(CreatePostRequestDTO dto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDTO dto);
}
