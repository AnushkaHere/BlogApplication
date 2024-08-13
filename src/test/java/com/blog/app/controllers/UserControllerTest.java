package com.blog.app.controllers;

import com.blog.app.payloads.UserDto;
import com.blog.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private List<UserDto> userDtoList;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        userDtoList = Arrays.asList(userDto);
    }

    @Test
    public void testGetUsers() throws Exception {
        when(userService.getAllUser()).thenReturn(userDtoList);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAllUser();
    }

    @Test
    public void testGetUserById() throws Exception {
        // Create a sample UserDto object to return from the service
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        // Add other fields if necessary

        // Mock the service call
        when(userService.getUserById(anyInt())).thenReturn(userDto);

        // Perform the GET request with the path variable
        mockMvc.perform(get("/api/users/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        // Verify that the service method was called exactly once
        verify(userService, times(1)).getUserById(1);
    }


    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Include all required fields in the JSON
        String userJson = "{\"name\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"password123\", \"about\": \"Test about\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }


    @Test
    public void testUpdateUser() throws Exception {
        // Create a UserDto with valid fields
        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated@example.com");
        userDto.setPassword("newPassword"); // Include password
        userDto.setAbout("Updated user profile"); // Include about

        // Mock the service call
        when(userService.updateUser(any(UserDto.class), anyInt())).thenReturn(userDto);

        // Convert the UserDto to JSON
        String userJson = "{ \"name\": \"Updated User\", \"email\": \"updated@example.com\", \"password\": \"newPassword\", \"about\": \"Updated user profile\" }";

        // Perform the PUT request with the path variable and request body
        mockMvc.perform(put("/api/users/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.password").value("newPassword"))
                .andExpect(jsonPath("$.about").value("Updated user profile"));

        // Verify that the service method was called with the correct parameters
        verify(userService, times(1)).updateUser(any(UserDto.class), eq(1));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyInt());

        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService, times(1)).deleteUser(anyInt());
    }
}

