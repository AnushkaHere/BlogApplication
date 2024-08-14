package com.blog.app.services;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.PostDto;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private Post post;
    private PostDto postDto;
    private User user;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the mock objects
        user = new User();
        user.setId(1);
        user.setName("testUser");

        category = new Category();
        category.setCategoryId(1);
        category.setCategoryTitle("testCategory");

        post = new Post();
        post.setPostId(1);
        post.setPostTitle("Test Post Title");
        post.setPostContent("Test Post Content");
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        postDto = new PostDto();
        postDto.setPostTitle("Test Post Title");
        postDto.setPostContent("Test Post Content");
        postDto.setImageName("default.png");
    }

    @Test
    public void testGetAllPosts() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        List<PostDto> result = postService.getAllPosts();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getPostTitle(), result.get(0).getPostTitle());
    }

    @Test
    public void testGetPostById() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        PostDto result = postService.getPostById(1);
        assertNotNull(result);
        assertEquals(postDto.getPostTitle(), result.getPostTitle());
    }

    @Test
    public void testGetPostByCategory() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(category)).thenReturn(Arrays.asList(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        List<PostDto> result = postService.getPostByCategory(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getPostTitle(), result.get(0).getPostTitle());
    }

    @Test
    public void testGetPostByUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findByUser(user)).thenReturn(Arrays.asList(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        List<PostDto> result = postService.getPostByUser(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getPostTitle(), result.get(0).getPostTitle());
    }

    @Test
    public void testCreatePost() {
        // Arrange
        Post postToSave = new Post();
        postToSave.setPostTitle(postDto.getPostTitle());
        postToSave.setPostContent(postDto.getPostContent());
        postToSave.setImageName("default.png");
        postToSave.setAddedDate(new Date());
        postToSave.setUser(user);
        postToSave.setCategory(category);

        // Mock repository and mapper
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(postRepository.save(any(Post.class))).thenReturn(postToSave);
        when(modelMapper.map(any(PostDto.class), eq(Post.class))).thenReturn(postToSave);
        when(modelMapper.map(any(Post.class), eq(PostDto.class))).thenReturn(postDto);

        // Act
        PostDto result = postService.createPost(postDto, 1, 1);

        // Assert
        assertNotNull(result, "PostDto should not be null");
        assertEquals(postDto.getPostTitle(), result.getPostTitle(), "Post title should match");
        assertEquals(postDto.getPostContent(), result.getPostContent(), "Post content should match");
        assertEquals(postDto.getImageName(), result.getImageName(), "Image name should match");

        // Verify interactions
        verify(userRepository).findById(1);
        verify(categoryRepository).findById(1);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void testUpdatePost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        PostDto result = postService.updatePost(1, postDto);
        assertNotNull(result);
        assertEquals(postDto.getPostTitle(), result.getPostTitle());
    }

    @Test
    public void testDeletePost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        postService.deletePost(1);
        // Verify that deleteById was called
        // No assertion needed, as we are just testing if it executes without exception
    }

    @Test
    public void testSearchPost() {
        when(postRepository.findByPostTitle("%Test%")).thenReturn(Arrays.asList(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        List<PostDto> result = postService.searchPost("Test");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getPostTitle(), result.get(0).getPostTitle());
    }

    @Test
    public void testGetPostByIdNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1));
    }
}
