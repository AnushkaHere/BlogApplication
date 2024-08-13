package com.blog.app.controllers;

import com.blog.app.payloads.CategoryDto;
import com.blog.app.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void getAllCategoriesTest() throws Exception {
        // Create a list of CategoryDto objects to return from the service
        List<CategoryDto> categoryDtos = List.of(
                new CategoryDto(1, "Tech", "Tech-related blogs"),
                new CategoryDto(2, "Travel", "Travel-related blogs")
        );

        // Mock the service call
        when(categoryService.getAllCategory()).thenReturn(categoryDtos);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/categories/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryTitle").value("Tech"))
                .andExpect(jsonPath("$[0].categoryDescription").value("Tech-related blogs"))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andExpect(jsonPath("$[1].categoryTitle").value("Travel"))
                .andExpect(jsonPath("$[1].categoryDescription").value("Travel-related blogs"));
    }


    @Test
    public void getCategoryByIdTest() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1, "Tech", "Tech-related blogs");
        when(categoryService.getCategoryById(1)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryTitle").value("Tech"));
    }


    @Test
    public void createCategoryTest() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1, "Tech", "Tech-related blogs");
        when(categoryService.addCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/api/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryTitle\": \"Tech\", \"categoryDescription\": \"Tech-related blogs\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryTitle").value("Tech"));
    }

    @Test
    public void updateCategoryTest() throws Exception {
        CategoryDto updatedCategory = new CategoryDto(1, "Tech Updated", "Updated description");
        when(categoryService.updateCategory(any(CategoryDto.class), any(Integer.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryTitle\": \"Tech Updated\", \"categoryDescription\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryTitle").value("Tech Updated"));
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
