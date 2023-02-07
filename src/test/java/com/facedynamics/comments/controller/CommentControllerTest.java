package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentControllerTest {
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Comment comment;
    @BeforeEach
    void init() {
//        commentController = new CommentController(commentService);
        comment = new Comment();
        comment.setId(1);
        comment.setReplies(new ArrayList<>());
        comment.setText("haha");
        comment.setPostId(1);
        comment.setUserId(1);
    }
    @Test
    void saveTest() throws Exception {
        when(commentService.save(comment)).thenReturn(DTOMapper.fromCommentToCommentDTO(comment));

        mvc.perform(post("/comments")
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    void findByIdTest() throws Exception {
        when(commentService.findById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(commentService.findById(2)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk());
        mvc.perform(get("/comments/{id}", 2))
                .andExpect(status().isNotFound());

    }
    @Test
    void deleteByIdTest() throws Exception{
        when(commentService.deleteById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(delete("/comments/{id}", 1))
                .andExpect(status().isOk());
    }
    @Test
    void findingCommentsByPostId() throws Exception{
        when(commentService.findCommentsByPostId(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(get("/comments/posts/{id}", 2))
                .andExpect(status().isOk());
    }
}
