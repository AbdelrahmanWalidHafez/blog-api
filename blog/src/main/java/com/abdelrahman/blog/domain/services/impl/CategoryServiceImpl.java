package com.abdelrahman.blog.domain.services.impl;

import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.repos.CategoryRepository;
import com.abdelrahman.blog.domain.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findALlWithPostCounts();
    }

    @Override
    @Transactional
    public Category createCategory(Category createdCategory) {
        if (categoryRepository.existsByNameIgnoreCase(createdCategory.getName()))
            throw new IllegalArgumentException("category already exists");
        return categoryRepository.save(createdCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Optional<Category> categoryToDelete=categoryRepository.findById(id);
        if (categoryToDelete.isPresent()){
            if (!categoryToDelete.get().getPosts().isEmpty()){
                throw new IllegalStateException("category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category Not Found with id"+id));
    }
}
