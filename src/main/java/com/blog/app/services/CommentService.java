package com.blog.app.services;

import com.blog.app.payloads.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllComments();
    CommentDto createComment(CommentDto commentDto, Integer postId);
    void deleteComment(Integer commentId);
}
