package com.abdelrahman.blog.domain.mappers;

import com.abdelrahman.blog.domain.DTOs.CategoryDTO;
import com.abdelrahman.blog.domain.DTOs.CreateCategoryRequest;
import com.abdelrahman.blog.domain.PostStatus;
import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import java.util.List;
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    @Mapping(target = "postCount",source = "posts",qualifiedByName = "calculatePostCount")
    CategoryDTO toDto(Category category);

    @Named("calculatePostCount")
    default  long calculatePostCount(List<Post>posts){
      if (null==posts){
          return 0;
      }
     return posts
             .stream()
             .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
             .count();
    }

    Category toEntity(CreateCategoryRequest createCategoryRequest);
}
