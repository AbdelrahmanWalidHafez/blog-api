package com.abdelrahman.blog.domain.services;

import com.abdelrahman.blog.domain.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category createdCategory);
    void deleteCategory(UUID id);
    Category getCategoryById(UUID id);
}
