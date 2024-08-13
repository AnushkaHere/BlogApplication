package com.blog.app.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blog.app.payloads.CommentDto;
import com.blog.app.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllComments() throws Exception {
        CommentDto comment1 = new CommentDto();
        comment1.setCommentId(1);
        comment1.setContent("Test Comment 1");

        CommentDto comment2 = new CommentDto();
        comment2.setCommentId(2);
        comment2.setContent("Test Comment 2");

        List<CommentDto> comments = Arrays.asList(comment1, comment2);

        when(commentService.getAllComments()).thenReturn(comments);

        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId").value(1))  // Make sure the field name matches your DTO
                .andExpect(jsonPath("$[0].content").value("Test Comment 1"))
                .andExpect(jsonPath("$[1].commentId").value(2))  // Make sure the field name matches your DTO
                .andExpect(jsonPath("$[1].content").value("Test Comment 2"));

        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void testCreateComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("New Comment");

        CommentDto createdCommentDto = new CommentDto();
        createdCommentDto.setCommentId(1);
        createdCommentDto.setContent("New Comment");

        when(commentService.createComment(any(CommentDto.class), eq(1))).thenReturn(createdCommentDto);

        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"New Comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))  // Make sure the field name matches your DTO
                .andExpect(jsonPath("$.content").value("New Comment"));

        verify(commentService, times(1)).createComment(any(CommentDto.class), eq(1));
    }

    @Test
    public void testDeleteComment() throws Exception {
        // Perform the DELETE request
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Comment deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));

        // Verify that the service method was called once with the correct argument
        verify(commentService, times(1)).deleteComment(1);
    }

}
