package com.blog.app.services;

import com.blog.app.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {

    //get
    List<CategoryDto> getAllCategory();

    //getAll
    CategoryDto getCategoryById(Integer categoryId);

    //create
    CategoryDto addCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);

    //delete
    void deleteCategory(Integer categoryId);
}
