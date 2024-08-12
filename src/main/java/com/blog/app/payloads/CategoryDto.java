package com.blog.app.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {

    private int categoryId;

    @NotEmpty
    @Size(min=3, max=30, message = "The title must be between 3 to 30 characters")
    private String categoryTitle;

    @Size(max=100, message = "The title must less than 100 characters")
    private String categoryDescription;

}
