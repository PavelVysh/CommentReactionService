package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exception.NotFoundException;
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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
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
    public static final String SAMPLE_TEXT = "sample Text";

    @BeforeEach
    void init() {
        commentService = new CommentService(commentRepository, reactionsRepository, mapper);
    }

    @Test
    void saveCommentTest() {
        Comment comment = new Comment();
        comment.setText(SAMPLE_TEXT);
        comment.setUserId(123);
        comment.setPostId(321);
        comment.setId(1);

        when(commentRepository.save(comment)).thenReturn(comment);

        CommentSaveDTO savedComment = commentService.save(comment);

        assertEquals("text of saved comment differs with posted comment", comment.getText(), savedComment.getText());
        assertEquals("id of saved comment differs with posted comment", comment.getId(), savedComment.getId());
        assertEquals("userId of saved comment differs with posted comment", comment.getUserId(), savedComment.getUserId());
        assertEquals("text of saved comment differs with posted comment", comment.getText(), savedComment.getText());

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void saveReplyToNonExistingComment() {
        Comment comment = new Comment();
        comment.setText(SAMPLE_TEXT);
        comment.setUserId(123);
        comment.setPostId(321);
        comment.setId(1);
        comment.setParentId(654);

        when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(NotFoundException.class, () -> commentService.save(comment));

        verify(commentRepository, times(1)).findById(654);
    }

    @Test
    void findByIdSuccessfulTest() {
        Comment comment = new Comment();
        comment.setId(2);
        comment.setText(SAMPLE_TEXT);

        when(commentRepository.findById(2)).thenReturn(Optional.of(comment));

        CommentReturnDTO returnDTO = commentService.findById(2);

        assertEquals("text of comment didn't return", SAMPLE_TEXT, returnDTO.getText());
        assertEquals("git comment with different id", 2, returnDTO.getId());

        verify(commentRepository, times(1)).findById(2);
    }

    @Test
    void findByIdUnSuccessfulTest() {

        when(commentRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.findById(3),
                "Should throw NotFoundException");

        verify(commentRepository, times(1)).findById(3);
    }

    @Test
    void findCommentsByPostIdSuccessfulTest() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Page<Comment> comments = new PageImpl<>(List.of(comment1, comment2));

        when(commentRepository.findCommentsByPostId(1, Pageable.ofSize(10))).thenReturn(comments);

        List<CommentReturnDTO> commentsFound = commentService.findByPostId(1, Pageable.ofSize(10))
                .getContent();

        assertEquals("should have found two comments", 2, commentsFound.size());

        verify(commentRepository, times(1)).findCommentsByPostId(anyInt(), any());
    }

    @Test
    void findCommentsByPostIdUnSuccessfulTest() {
        when(commentRepository.findCommentsByPostId(666, Pageable.ofSize(5))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> commentService.findByPostId(666, Pageable.ofSize(5)),
                "Should throw NotFoundException");

        verify(commentRepository, times(1)).findCommentsByPostId( eq(666), any(Pageable.class));
    }

    @Test
    void deleteByIDSuccessfulTest() {
        when(commentRepository.deleteById(1)).thenReturn(1);

        int response = commentService.deleteById(1).getRowsAffected();

        assertEquals("Deletion of a comment by id", 1, response);

        verify(commentRepository, times(1)).deleteById(1);

    }

    @Test
    void deleteByIdNotSuccessfulTest() {
        when(commentRepository.deleteById(666)).thenReturn(0);

        assertEquals("should say that 0 been deleted", 0,
                commentService.deleteById(666).getRowsAffected());

        verify(commentRepository, times(1)).deleteById(666);
    }

    @Test
    void deleteByPostIDNotSuccessfulTest() {
        when(commentRepository.deleteByPostId(2)).thenReturn(0);

        assertEquals("should say 0 been deleted",
                0, commentService.deleteByPostId(2).getRowsAffected());

        verify(commentRepository, times(1)).deleteByPostId(2);
    }

}
