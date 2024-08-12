package com.blog.app.services.impl;

import com.blog.app.entities.Comment;
import com.blog.app.entities.Post;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CommentDto;
import com.blog.app.repositories.CommentRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommentDto> getAllComments() {
        List<Comment>comments=commentRepository.findAll();
        List<CommentDto> commentDtos=comments.stream().map(this::commentToDto).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));

        Comment comment=this.DtoToComment(commentDto);
        comment.setContent(commentDto.getContent());
        comment.setPost(post);
        Comment savedComment=commentRepository.save(comment);

        return this.commentToDto(savedComment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment=this.commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","Id",commentId));
        commentRepository.deleteById(commentId);
    }

    public CommentDto commentToDto(Comment comment){
        return this.modelMapper.map(comment,CommentDto.class);
    }

    public Comment DtoToComment(CommentDto commentDto){
        return this.modelMapper.map(commentDto, Comment.class);
    }

}
