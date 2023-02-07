package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        comment.setId(1);

        when(commentRepository.save(comment)).thenReturn(comment);

        CommentDTO savedComment = commentService.save(comment);

        assertEquals("text of saved comment if off", comment.getText(), savedComment.getText());
        assertEquals("id doesn't equals" ,1, savedComment.getId());
    }
    @Test
    void findByIdSuccessfulTest() {
        Comment comment = new Comment();
        comment.setId(2);
        comment.setReplies(new ArrayList<>());

        when(commentRepository.findById(2)).thenReturn(Optional.of(comment));

        ResponseEntity<?> status = commentService.findById(2);

        assertEquals("didn't find an existing comment",HttpStatus.OK , status.getStatusCode());
    }
    @Test
    void findByInUnSuccessfulTest() {
        when(commentRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.findById(3),
                "Should throw NotFoundException");
    }
    @Test
    void deleteByIDSuccessfulTest() {

        when(commentRepository.findById(1)).thenReturn(Optional.of(new Comment()));

        ResponseEntity<?> response = commentService.deleteById(1);

        assertEquals("Deletion of a comment by id", HttpStatus.OK , response.getStatusCode());

    }
    @Test
    void deleteByIdNotSuccessfulTest() {
        when(commentRepository.findById(666)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteById(666),
                "Should throw NotFoundException");
    }
    @Test
    void findCommentsByPostIdSuccessfulTest() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        comment1.setReplies(new ArrayList<>());
        comment2.setReplies(new ArrayList<>());

        when(commentRepository.findCommentsByPostId(1)).thenReturn(new ArrayList<>(Arrays.asList(comment1, comment2)));

        ResponseEntity<?> commentsFound = commentService.findCommentsByPostId(1);

        assertEquals("should have found two comments", HttpStatus.OK, commentsFound.getStatusCode());
    }
    @Test
    void findCommentsByPostIdUnSuccessfulTest() {
        when(commentRepository.findCommentsByPostId(666)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> commentService.findCommentsByPostId(666),
                "Should throw NotFoundException");
    }
    @Test
    void setLikeDislikeTest() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setReplies(new ArrayList<>());

        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.comment, false)).thenReturn(3);
        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.comment, true)).thenReturn(4);
        commentService.setLikesDislikes(comment, EntityType.comment);

        assertEquals("Checking likes on a comment", 4, comment.getLikes());
        assertEquals("Checking dislikes on a comment", 3, comment.getDislikes());
    }
    @Test
    void setLikeDislikeForReplyInCommentTest() {
        Comment comment = new Comment();
        List<Reply> replies = new ArrayList<>();
        Reply reply = new Reply();
        reply.setId(1);
        replies.add(reply);
        comment.setReplies(replies);

        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.reply, false)).thenReturn(3);
        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.reply, true)).thenReturn(4);

        Comment likedComment = commentService.setLikesDislikes(comment, EntityType.comment);

        assertEquals("setting likes for reply inside a comment", 4,
                likedComment.getReplies().get(0).getLikes());
        assertEquals("setting dislikes for reply inside a comment", 3,
                likedComment.getReplies().get(0).getDislikes());
    }


}
