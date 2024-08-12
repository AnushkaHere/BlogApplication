package com.blog.app;

import com.blog.app.controllers.PostController;
import com.blog.app.payloads.PostDto;
import com.blog.app.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private PostDto postDto;
    private List<PostDto> postDtoList;

    @BeforeEach
    public void setUp() {
        postDto = new PostDto();
        postDto.setPostTitle("Test Title");
        postDto.setPostContent("Test Content");
        postDto.setImageName("test.png");

        postDtoList = Arrays.asList(postDto);
    }

    @Test
    public void testGetAllPosts() throws Exception {
        when(postService.getAllPosts()).thenReturn(postDtoList);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postTitle").value("Test Title"))
                .andExpect(jsonPath("$[0].postContent").value("Test Content"));

        verify(postService, times(1)).getAllPosts();
    }

    @Test
    public void testGetPostById() throws Exception {
        when(postService.getPostById(anyInt())).thenReturn(postDto);

        mockMvc.perform(get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postTitle").value("Test Title"))
                .andExpect(jsonPath("$.postContent").value("Test Content"));

        verify(postService, times(1)).getPostById(anyInt());
    }

    @Test
    public void testGetPostByUser() throws Exception {
        when(postService.getPostByUser(anyInt())).thenReturn(postDtoList);

        mockMvc.perform(get("/api/user/1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postTitle").value("Test Title"))
                .andExpect(jsonPath("$[0].postContent").value("Test Content"));

        verify(postService, times(1)).getPostByUser(anyInt());
    }

    @Test
    public void testGetPostByCategory() throws Exception {
        when(postService.getPostByCategory(anyInt())).thenReturn(postDtoList);

        mockMvc.perform(get("/api/categories/1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postTitle").value("Test Title"))
                .andExpect(jsonPath("$[0].postContent").value("Test Content"));

        verify(postService, times(1)).getPostByCategory(anyInt());
    }

    @Test
    public void testAddPost() throws Exception {
        when(postService.createPost(any(PostDto.class), anyInt(), anyInt())).thenReturn(postDto);

        String postJson = "{\"postTitle\": \"Test Title\", \"postContent\": \"Test Content\", \"imageName\": \"test.png\"}";

        mockMvc.perform(post("/api/users/1/categories/1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postTitle").value("Test Title"))
                .andExpect(jsonPath("$.postContent").value("Test Content"));

        verify(postService, times(1)).createPost(any(PostDto.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdatePost() throws Exception {
        // Arrange: Set up a sample PostDto with updated information
        PostDto updatedPostDto = new PostDto();
        updatedPostDto.setPostTitle("Updated Title");
        updatedPostDto.setPostContent("Updated Content");
        updatedPostDto.setImageName("updated.png");

        when(postService.updatePost(anyInt(), any(PostDto.class))).thenReturn(updatedPostDto);

        String postJson = "{\"postTitle\": \"Updated Title\", \"postContent\": \"Updated Content\", \"imageName\": \"updated.png\"}";

        // Act: Perform the PUT request
        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                // Assert: Verify that the status and response content are as expected
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postTitle").value("Updated Title"))
                .andExpect(jsonPath("$.postContent").value("Updated Content"))
                .andExpect(jsonPath("$.imageName").value("updated.png"));

        // Verify that the updatePost method in the service layer was called exactly once
        verify(postService, times(1)).updatePost(anyInt(), any(PostDto.class));
    }

    @Test
    public void testDeletePost() throws Exception {
        doNothing().when(postService).deletePost(anyInt());

        mockMvc.perform(delete("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post deleted successfully"));

        verify(postService, times(1)).deletePost(anyInt());
    }
}

