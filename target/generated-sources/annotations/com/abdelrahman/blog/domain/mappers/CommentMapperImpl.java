package com.abdelrahman.blog.domain.mappers;

import com.abdelrahman.blog.domain.DTOs.CommentDTO;
import com.abdelrahman.blog.domain.model.Comment;
import com.abdelrahman.blog.domain.model.Post;
import com.abdelrahman.blog.domain.model.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-14T00:30:11+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDTO toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDTO.CommentDTOBuilder commentDTO = CommentDTO.builder();

        commentDTO.postId( commentPostId( comment ) );
        commentDTO.userId( commentUserId( comment ) );
        commentDTO.content( comment.getContent() );
        commentDTO.id( comment.getId() );
        commentDTO.createdAt( comment.getCreatedAt() );
        commentDTO.updatedAt( comment.getUpdatedAt() );

        return commentDTO.build();
    }

    private UUID commentPostId(Comment comment) {
        Post post = comment.getPost();
        if ( post == null ) {
            return null;
        }
        return post.getId();
    }

    private UUID commentUserId(Comment comment) {
        User user = comment.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
