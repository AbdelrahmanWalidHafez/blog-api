package com.abdelrahman.blog.domain.mappers;
import com.abdelrahman.blog.domain.DTOs.*;
import com.abdelrahman.blog.domain.model.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "content", target = "content")
    CommentDTO toDto(Comment comment);
}
