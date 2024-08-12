package com.blog.app.services;

import com.blog.app.entities.Post;
import com.blog.app.payloads.PostDto;

import java.util.List;

public interface PostService {

    //get all posts
    List<PostDto> getAllPosts();

    //get all posts by id
    PostDto getPostById(Integer postId);

    //create posts
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    //update posts
    PostDto updatePost(Integer postId, PostDto postDto);

    //delete posts
    void deletePost(Integer postId);

    //get posts by category
    List<PostDto> getPostByCategory(Integer categoryId);

    //get posts by user
    List<PostDto> getPostByUser(Integer userId);

    //search post using a keyword
    List<PostDto> searchPost(String keyword);

}
