package com.abdelrahman.blog.domain.mappers;
import com.abdelrahman.blog.domain.DTOs.UserDTO;

import com.abdelrahman.blog.domain.model.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toUserDTO(User user);
}
