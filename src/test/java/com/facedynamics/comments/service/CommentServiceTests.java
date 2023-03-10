package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentDeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceTests {
    @Mock
    private static CommentRepository commentRepository;
    @Mock
    private static ReactionsRepository reactionsRepository;

    private static CommentService commentService;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);
    @BeforeEach
    void init() {
        commentService = new CommentService(commentRepository,reactionsRepository, mapper);
    }

    @Test
    void saveCommentTest() {
        Comment comment = new Comment();
        comment.setText("sample");
        comment.setUserId(123);
        comment.setPostId(321);
        comment.setId(1);

        when(commentRepository.save(comment)).thenReturn(comment);

        CommentSaveDTO savedComment = commentService.save(comment);

        assertEquals("text of saved comment if off", comment.getText(), savedComment.getText());
        assertEquals("id doesn't equals" ,1, savedComment.getId());
    }
    @Test
    void findByIdSuccessfulTest() {
        Comment comment = new Comment();
        comment.setId(2);
        comment.setText("test text");

        when(commentRepository.findById(2)).thenReturn(Optional.of(comment));

        CommentReturnDTO status = commentService.findById(2);

        assertEquals("didn't find an existing comment","test text" , status.getText());
    }
    @Test
    void findByInUnSuccessfulTest() {
        when(commentRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.findById(3),
                "Should throw NotFoundException");
    }
    @Test
    void deleteByIDSuccessfulTest() {
        when(commentRepository.deleteById(1)).thenReturn(1);

        CommentDeleteDTO response = commentService.deleteByCommentId(1);

        assertEquals("Deletion of a comment by id", 1 , response.getRowsAffected());

    }
    @Test
    void deleteByIdNotSuccessfulTest() {
        when(commentRepository.deleteByPostId(666)).thenReturn(0);

        assertEquals("should say that 0 been deleted", 0,
                commentService.deleteByCommentId(666).getRowsAffected());
    }
    @Test
    void deleteByPostIDNotSuccessfulTest() {
        when(commentRepository.deleteByPostId(2)).thenReturn(0);

        assertEquals("should say 0 been deleted",
                0, commentService.deleteByPostId(2).getRowsAffected());
    }
    @Test
    void findCommentsByPostIdSuccessfulTest() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        when(commentRepository.findCommentsByPostId(1)).thenReturn(new ArrayList<>(Arrays.asList(comment1, comment2)));

        List<CommentReturnDTO> commentsFound = commentService.findCommentsByPostId(1);

        assertEquals("should have found two comments", 2, commentsFound.size());
    }
    @Test
    void findCommentsByPostIdUnSuccessfulTest() {
        when(commentRepository.findCommentsByPostId(666)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> commentService.findCommentsByPostId(666),
                "Should throw NotFoundException");
    }
    @Test
    void absentParentTest() {
        Comment comment = new Comment();
        comment.setParentId(2);

        when(commentRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.save(comment));
    }

}
