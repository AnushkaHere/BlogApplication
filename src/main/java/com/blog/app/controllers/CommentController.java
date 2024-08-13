package com.blog.app.controllers;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CommentDto;
import com.blog.app.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //get all comments
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(){
        return ResponseEntity.ok(this.commentService.getAllComments());
    }

    //create comments
    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable("post_id") Integer postId){
        return ResponseEntity.ok(commentService.createComment(commentDto,postId));
    }

    //delete comments
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("comment_id") Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
    }
}
