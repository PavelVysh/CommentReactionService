package com.facedynamics.comments.unittests.service;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
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
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceTest {
    @Mock
    private static CommentRepository commentRepository;
    @Mock
    private static ReactionsRepository reactionsRepository;
    private static CommentService commentService;
    @BeforeEach
    void init() {
        commentService = new CommentService(commentRepository, reactionsRepository);
    }

    @Test
    void saveCommentTest() {
        Comment comment = new Comment();
        comment.setText("sample");
        comment.setUserId(123);
        comment.setPostId(321);

        Comment commentWithId = new Comment();
        commentWithId.setText("sample");
        commentWithId.setUserId(123);
        commentWithId.setPostId(321);
        commentWithId.setId(1);


        when(commentRepository.save(comment)).thenReturn(commentWithId);

        CommentDTO savedComment = commentService.save(comment);

        assertEquals("text of saved comment if off", comment.getText(), savedComment.getText());
        assertEquals("id doesn't equals" ,1, commentWithId.getId());
    }
    @Test
    void findByIdSuccessful() {
        Comment comment = new Comment();
        comment.setId(2);
        comment.setReplies(new ArrayList<>());

        when(commentRepository.findById(2)).thenReturn(Optional.of(comment));

        ResponseEntity<?> status = commentService.findById(2);

        assertEquals("didn't find an existing comment",HttpStatus.OK , status.getStatusCode());
    }
    @Test
    void findByInUnSuccessful() {
        when(commentRepository.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<?> status = commentService.findById(3);

        assertEquals("trying to find comment that doesn't exist", HttpStatus.NOT_FOUND, status.getStatusCode());
    }

}
