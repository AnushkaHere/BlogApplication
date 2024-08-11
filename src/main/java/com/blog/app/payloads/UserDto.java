package com.blog.app.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private int id;

    @NotEmpty
    @Size(min = 4, message = "Name must be minimum 4 characters long.")
    private String name;

    @Email(message = "Email is not valid!")
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty
    @Size(min=6, max=15, message = "Password must be between 6 to 15 characters long.")
    private String password;

    @NotEmpty
    private String about;
}
