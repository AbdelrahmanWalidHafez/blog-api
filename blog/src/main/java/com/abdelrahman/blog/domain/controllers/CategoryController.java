package com.abdelrahman.blog.domain.controllers;

import com.abdelrahman.blog.domain.DTOs.CategoryDTO;
import com.abdelrahman.blog.domain.DTOs.CreateCategoryRequest;
import com.abdelrahman.blog.domain.mappers.CategoryMapper;
import com.abdelrahman.blog.domain.model.Category;
import com.abdelrahman.blog.domain.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories(){
        List<CategoryDTO> categories=categoryService
                .listCategories()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO>createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest){
        Category createdCategory=categoryMapper.toEntity(createCategoryRequest);
        Category savedCategory=categoryService.createCategory(createdCategory);
        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
