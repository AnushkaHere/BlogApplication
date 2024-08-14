package com.blog.app.controllers;

import com.blog.app.payloads.PostDto;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    // Test for getting all posts
    @Test
    public void testGetAllPosts() throws Exception {
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostDto());

        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(posts.size()));
    }

    // Test for getting post by post id
    @Test
    public void testGetPostById() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setPostId(1);

        when(postService.getPostById(1)).thenReturn(postDto);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1));
    }

    // Test for getting posts by user id
    @Test
    public void testGetPostByUser() throws Exception {
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostDto());

        when(postService.getPostByUser(1)).thenReturn(posts);

        mockMvc.perform(get("/api/users/1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(posts.size()));
    }

    // Test for getting posts by category id
    @Test
    public void testGetPostByCategory() throws Exception {
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostDto());

        when(postService.getPostByCategory(1)).thenReturn(posts);

        mockMvc.perform(get("/api/categories/1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(posts.size()));
    }

    // Test for creating a post
    @Test
    public void testAddPost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setPostId(1);

        when(postService.createPost(any(PostDto.class), eq(1), eq(1))).thenReturn(postDto);

        mockMvc.perform(post("/api/users/1/categories/1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Post\", \"content\": \"Test Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(1));
    }

    // Test for updating a post
    @Test
    public void testUpdatePost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setPostId(1);

        when(postService.updatePost(eq(1), any(PostDto.class))).thenReturn(postDto);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Post\", \"content\": \"Updated Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1));
    }

    // Test for deleting a post
    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isOk());

        verify(postService, times(1)).deletePost(1);
    }

    // Test for uploading post image
    @Test
    public void testUploadPostImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        PostDto postDto = new PostDto();
        postDto.setPostId(1);
        postDto.setImageName("image.jpg");

        when(fileService.uploadImage(anyString(), any(MultipartFile.class))).thenReturn("image.jpg");
        when(postService.getPostById(1)).thenReturn(postDto);
        when(postService.updatePost(eq(1), any(PostDto.class))).thenReturn(postDto);

        mockMvc.perform(multipart("/api/posts/image/upload/1").file(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageName").value("image.jpg"));
    }

    // Test for downloading post image
    @Test
    public void testDownloadImage() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("image".getBytes());
        when(fileService.getResource(anyString(), anyString())).thenReturn(inputStream);

        mockMvc.perform(get("/api/posts/image/image.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE));
    }
}
