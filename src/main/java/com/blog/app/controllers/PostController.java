package com.blog.app.controllers;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Get post by post id
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("post_id") Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // Get posts by user id
    @GetMapping("/users/{user_id}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable("user_id") Integer userId) {
        return ResponseEntity.ok(postService.getPostByUser(userId));
    }

    // Get posts by category id
    @GetMapping("/categories/{category_id}/posts")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("category_id") Integer categoryId) {
        return ResponseEntity.ok(postService.getPostByCategory(categoryId));
    }

    // Get the posts by search using title
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keyword") String keyword){
        return new ResponseEntity<>(postService.searchPost(keyword), HttpStatus.OK);
    }

    // Create posts
    @PostMapping("/users/{user_id}/categories/{category_id}/posts")
    public ResponseEntity<PostDto> addPost(@RequestBody PostDto postDto, @PathVariable("user_id") Integer userId,
                                           @PathVariable("category_id") Integer categoryId) {
        return new ResponseEntity<>(postService.createPost(postDto, userId, categoryId), HttpStatus.CREATED);
    }

    // Update posts
    @PutMapping("/posts/{post_id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable("post_id") Integer postId) {
        return ResponseEntity.ok(postService.updatePost(postId, postDto));
    }

    // Delete posts
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("post_id") Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
    }

    //post image upload
    @PostMapping("/posts/image/upload/{post_id}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable("post_id") Integer postId) throws IOException {
        String fileName=fileService.uploadImage(path,image);
        PostDto postDto=postService.getPostById(postId);
        postDto.setImageName(fileName);
        PostDto updatedPost= postService.updatePost(postId,postDto);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }

    //method to serve files
    @GetMapping(value = "/posts/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}