package com.blog.app.services;

import com.blog.app.entities.Comment;
import com.blog.app.entities.Post;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CommentDto;
import com.blog.app.repositories.CommentRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    private Comment comment;
    private CommentDto commentDto;
    private Post post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Post();
        post.setPostId(1);

        comment = new Comment();
        comment.setCommentId(1);
        comment.setContent("Test comment");
        comment.setPost(post);

        commentDto = new CommentDto();
        commentDto.setContent("Test comment");
    }

    @Test
    public void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getAllComments();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentDto.getContent(), result.get(0).getContent());
    }

    @Test
    public void testCreateComment() {
        // Prepare the post mock
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        // Ensure modelMapper maps the DTO to the Comment entity
        when(modelMapper.map(commentDto, Comment.class)).thenReturn(comment);

        // Ensure modelMapper maps the saved Comment entity to the DTO
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        // Mock the commentRepository save method to return the prepared comment
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Call the service method
        CommentDto result = commentService.createComment(commentDto, 1);

        // Assert that the result is not null and has the expected content
        assertNotNull(result);
        assertEquals(commentDto.getContent(), result.getContent());
    }



    @Test
    public void testDeleteComment() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteComment(1));
    }

    @Test
    public void testCreateCommentPostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(commentDto, 1));
    }

    @Test
    public void testDeleteCommentNotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(1));
    }
}
