package com.blog.app.services;

import com.blog.app.entities.Category;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CategoryDto createCategoryDto(int id, String title, String description) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(id);
        dto.setCategoryTitle(title);
        dto.setCategoryDescription(description);
        return dto;
    }

    private Category createCategory(int id, String title, String description) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryTitle(title);
        category.setCategoryDescription(description);
        return category;
    }

    @Test
    public void testGetAllCategory_Success() {
        List<Category> categories = List.of(
                createCategory(1, "Category 1", "Description 1"),
                createCategory(2, "Category 2", "Description 2")
        );
        List<CategoryDto> categoryDtos = List.of(
                createCategoryDto(1, "Category 1", "Description 1"),
                createCategoryDto(2, "Category 2", "Description 2")
        );

        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(categories.get(0), CategoryDto.class)).thenReturn(categoryDtos.get(0));
        when(modelMapper.map(categories.get(1), CategoryDto.class)).thenReturn(categoryDtos.get(1));

        List<CategoryDto> result = categoryService.getAllCategory();

        assertEquals(categoryDtos, result);
        verify(categoryRepository).findAll();
        verify(modelMapper, times(2)).map(any(Category.class), eq(CategoryDto.class));
    }

    @Test
    public void testGetCategoryById_Success() {
        Category category = createCategory(1, "Title", "Description");
        CategoryDto categoryDto = createCategoryDto(1, "Title", "Description");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(1);

        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1);
        verify(modelMapper).map(category, CategoryDto.class);
    }

    @Test
    public void testAddCategory_Success() {
        CategoryDto dto = createCategoryDto(1, "New Category", "New Description");
        Category category = createCategory(1, "New Category", "New Description");
        Category savedCategory = createCategory(1, "New Category", "New Description");

        when(modelMapper.map(dto, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(modelMapper.map(savedCategory, CategoryDto.class)).thenReturn(dto);

        CategoryDto result = categoryService.addCategory(dto);

        assertEquals(dto, result);
        verify(modelMapper).map(dto, Category.class);
        verify(categoryRepository).save(category);
        verify(modelMapper).map(savedCategory, CategoryDto.class);
    }

    @Test
    public void testUpdateCategory_Success() {
        int categoryId = 1;
        CategoryDto dto = createCategoryDto(categoryId, "Updated Title", "Updated Description");
        Category existingCategory = createCategory(categoryId, "Old Title", "Old Description");
        Category updatedCategory = createCategory(categoryId, "Updated Title", "Updated Description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(modelMapper.map(dto, Category.class)).thenReturn(updatedCategory); // Ensure this is mocked correctly
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(modelMapper.map(updatedCategory, CategoryDto.class)).thenReturn(dto); // Ensure this is mocked correctly

        CategoryDto result = categoryService.updateCategory(dto, categoryId);

        System.out.println("Result from service: " + result); // Debug print

        assertEquals(dto, result);
        verify(categoryRepository).findById(categoryId);
        verify(modelMapper).map(dto, Category.class); // Verifies mapping of CategoryDto to Category
        verify(categoryRepository).save(updatedCategory);
        verify(modelMapper).map(updatedCategory, CategoryDto.class); // Verifies mapping of Category to CategoryDto
    }

    @Test
    public void testDeleteCategory_Success() {
        Category category = createCategory(1, "Title", "Description");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1);

        verify(categoryRepository).findById(1);
        verify(categoryRepository).delete(category);
    }
}
