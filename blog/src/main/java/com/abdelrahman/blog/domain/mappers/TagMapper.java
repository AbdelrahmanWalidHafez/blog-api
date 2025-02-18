package com.abdelrahman.blog.domain.mappers;

import com.abdelrahman.blog.domain.DTOs.TagResponse;
import com.abdelrahman.blog.domain.PostStatus;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.Tag;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import java.util.Set;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount",source = "posts",qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(Tag tag);
    @Named("calculatePostCount")
    default  Integer calculatePostCount(Set<Post> posts){
        if (null==posts){
            return 0;
        }
        return (int)posts
                .stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
