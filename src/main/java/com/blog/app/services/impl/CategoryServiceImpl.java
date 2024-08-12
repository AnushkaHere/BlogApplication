package com.blog.app.services.impl;

import com.blog.app.entities.Category;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories=this.categoryRepository.findAll();
        List<CategoryDto> categoryDtos=categories.stream().map(this::categoryToDto).collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category=this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "Category Id", categoryId));

        return this.categoryToDto(category);
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category=this.dtoToCategory(categoryDto);
        Category savedCategory=this.categoryRepository.save(category);
        return this.categoryToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category=this.categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", categoryId));

        category.setCategoryTitle(categoryDto.getCategoryDescription());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCategory=this.categoryRepository.save(category);

        return this.categoryToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category=this.categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "Id",categoryId));
        this.categoryRepository.delete(category);
    }

    public CategoryDto categoryToDto(Category category){
        CategoryDto categoryDto=this.modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }

    public Category dtoToCategory(CategoryDto categoryDto){
        Category category=this.modelMapper.map(categoryDto,Category.class);
        return category;
    }

}
