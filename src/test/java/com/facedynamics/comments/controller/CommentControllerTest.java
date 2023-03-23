package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exception.NotFoundException;
import com.facedynamics.comments.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentControllerTest {
    public static final String COMMENTS = "/comments";
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Comment comment;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);

    @BeforeEach
    void init() {
        comment = new Comment();
        comment.setId(1);
        comment.setText("haha");
        comment.setPostId(1);
        comment.setUserId(1);
    }

    @Test
    void saveTest() throws Exception {
        when(commentService.save(comment)).thenReturn(mapper.commentToCommentDTO(comment));

        mvc.perform(post(COMMENTS)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdTest() throws Exception {
        when(commentService.findById(1))
                .thenReturn(new CommentReturnDTO());

        mvc.perform(get(COMMENTS + "/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByIdTest() throws Exception {
        when(commentService.deleteByCommentId(1)).thenReturn(2);

        mvc.perform(delete(COMMENTS + "/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void findingCommentsByPostId() throws Exception {
        when(commentService.findCommentsByPostId(1, Pageable.ofSize(10)))
                .thenReturn(new PageImpl<>(Arrays.asList(new CommentReturnDTO(), new CommentReturnDTO())));

        mvc.perform(get("/posts/{id}" + COMMENTS, 2))
                .andExpect(status().isOk());
    }

    @Test
    void saveCommentWithoutTextTest() throws Exception {
        Comment invalidComment = new Comment();
        invalidComment.setPostId(1);
        invalidComment.setUserId(1);

        mvc.perform(post(COMMENTS)
                        .content(objectMapper.writeValueAsString(invalidComment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdNonExistentCommentTest() throws Exception {
        when(commentService.findById(2))
                .thenThrow(new NotFoundException("Comment not found with id 2"));

        MvcResult result = mvc.perform(get(COMMENTS + "/{id}", 2))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue("Should contain a message about non existing comment",
                result.getResponse().getContentAsString().contains("Comment not found with id 2"));
    }

    @Test
    void deleteNonExistentCommentTest() throws Exception {
        when(commentService.deleteByCommentId(2)).thenReturn(0);

        MvcResult result = mvc.perform(delete(COMMENTS + "/{id}", 2))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue("Should contain a message that 0 comments have been deleted", result.getResponse()
                .getContentAsString().contains("0 comment(s) have been deleted"));
    }

    @Test
    void deleteByNonExistentPostIdTest() throws Exception {
        when(commentService.deleteByPostId(2)).thenReturn(0);

        MvcResult result = mvc.perform(delete("/posts/{postId}" + COMMENTS, 2))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue("Should contain a message that 0 posts have been deleted",
                result.getResponse().getContentAsString().contains("0 comment(s) have been deleted"));
    }
}
