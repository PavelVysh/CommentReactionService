package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.exception.NotFoundException;
import com.facedynamics.comments.service.CommentService;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentControllerTest {
    public static final String COMMENTS = "/comments";
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    public static final String commentJson = """
            {
            "userId": "1",
            "text": "haha",
            "postId": "1"
            }
            """;

    @Test
    void saveCorrectDataTest() throws Exception {
        CommentSaveDTO comment = new CommentSaveDTO();
        comment.setId(5);

        when(commentService.save(any())).thenReturn(comment);

        MvcResult result = mvc.perform(post(COMMENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(commentService, times(1)).save(any());
        assertTrue("should be same id", result.getResponse().getContentAsString().contains("\"id\":5"));
    }

    @Test
    void saveWithIncorrectDataTest() throws Exception {
        String invalidComment = """
                {
                "id": 2
                }
                """;
        MvcResult result = mvc.perform(post(COMMENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidComment))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue("error message check", result.getResponse().getContentAsString()
                .contains("You can't save a comment without a text"));
    }

    @Test
    void findByIdTest() throws Exception {
        CommentReturnDTO returnDTO = new CommentReturnDTO();
        returnDTO.setId(5);

        when(commentService.findById(5))
                .thenReturn(returnDTO);

        mvc.perform(get(COMMENTS + "/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("5"));
        verify(commentService, times(1)).findById(5);
    }

    @Test
    void deleteByIdTest() throws Exception {
        when(commentService.deleteByCommentId(1)).thenReturn(2);

        mvc.perform(delete(COMMENTS + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("2 comment(s) have been deleted"));
        verify(commentService, times(1)).deleteByCommentId(1);
    }

    @Test
    void findingCommentsByPostId() throws Exception {
        when(commentService.findCommentsByPostId(1, Pageable.ofSize(10)))
                .thenReturn(new PageImpl<>(Arrays.asList(new CommentReturnDTO(), new CommentReturnDTO())));

        mvc.perform(get("/posts/{id}" + COMMENTS, 1)
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());
        verify(commentService, times(1)).findCommentsByPostId(1, Pageable.ofSize(10));
    }

    @Test
    void saveCommentWithoutTextTest() throws Exception {
        String invalidCommentJson = """
                {
                "postId": "1",
                "userId": "1"
                }
                """;

        mvc.perform(post(COMMENTS)
                        .content(invalidCommentJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problems").isNotEmpty());

    }

    @Test
    void findByIdNonExistentCommentTest() throws Exception {
        when(commentService.findById(2))
                .thenThrow(new NotFoundException("Comment not found with id 2"));

        MvcResult result = mvc.perform(get(COMMENTS + "/{id}", 2))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(commentService, times(1)).findById(2);

        assertTrue("Should contain a message about non existing comment",
                result.getResponse().getContentAsString().contains("Comment not found with id 2"));
    }

    @Test
    void deleteNonExistentCommentTest() throws Exception {
        when(commentService.deleteByCommentId(2)).thenReturn(0);

        MvcResult result = mvc.perform(delete(COMMENTS + "/{id}", 2))
                .andExpect(status().isOk())
                .andReturn();

        verify(commentService, times(1)).deleteByCommentId(2);

        assertTrue("Should contain a message that 0 comments have been deleted", result.getResponse()
                .getContentAsString().contains("0 comment(s) have been deleted"));
    }

    @Test
    void deleteByNonExistentPostIdTest() throws Exception {
        when(commentService.deleteByPostId(2)).thenReturn(0);

        MvcResult result = mvc.perform(delete("/posts/{postId}" + COMMENTS, 2))
                .andExpect(status().isOk())
                .andReturn();

        verify(commentService, times(1)).deleteByPostId(2);

        assertTrue("Should contain a message that 0 posts have been deleted",
                result.getResponse().getContentAsString().contains("0 comment(s) have been deleted"));
    }
}
