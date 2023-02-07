package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentControllerTest {
    @Mock
    private CommentService commentService;
    private CommentController commentController;
    private Comment comment;
    @BeforeEach
    void init() {
        commentController = new CommentController(commentService);
        comment = new Comment();
        comment.setId(1);
        comment.setReplies(new ArrayList<>());
        comment.setText("haha");
        comment.setPostId(1);
        comment.setUserId(1);
    }
    @Test
    void saveTest() {
        when(commentService.save(comment)).thenReturn(DTOMapper.fromCommentToCommentDTO(comment));

        CommentDTO commentDTO = commentController.createComment(comment);

        assertEquals("saving a comment", "haha", commentDTO.getText());
    }
    @Test
    void findByIdTest() {
        when(commentService.findById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> entity = commentController.findById(1);

        assertEquals("finding by id", HttpStatus.OK, entity.getStatusCode());
    }
    @Test
    void deleteByIdTest() {
        when(commentService.deleteById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> entity = commentController.deleteById(1);

        assertEquals("deleting entity by id", HttpStatus.OK, entity.getStatusCode());
    }
    @Test
    void findingCommentsByPostId() {
        when(commentService.findCommentsByPostId(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = commentController.findCommentsByPostId(1);

        assertEquals("finding comments by post id", HttpStatus.OK, response.getStatusCode());
    }
}
