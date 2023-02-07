package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.repository.ReplyRepository;
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
public class ReplyServiceTests {
    @Mock
    private static ReplyRepository replyRepository;
    @Mock
    private static ReactionsRepository reactionsRepository;
    private static ReplyService replyService;
    @BeforeEach
    void init() {
        replyService = new ReplyService(replyRepository, reactionsRepository);
    }
    @Test
    void saveTest() {
        Reply reply = new Reply();
        reply.setId(1);
        reply.setCommentId(1);
        reply.setUserId(1);
        reply.setText("test text");
        when(replyRepository.save(reply)).thenReturn(reply);

        ReplyDTO replyDTO = replyService.save(reply);

        assertEquals("test for id", 1, replyDTO.getId());
        assertEquals("text test", "test text", replyDTO.getText());
    }
    @Test
    void findByIdSuccessfulTest() {
        Reply reply = new Reply();
        reply.setId(2);

        when(replyRepository.findById(2)).thenReturn(Optional.of(reply));

        ResponseEntity<?> status = replyService.findById(2);

        assertEquals("didn't find an existing comment", HttpStatus.OK , status.getStatusCode());
    }
    @Test
    void findByIdUnSuccessfulTest() {
        when(replyRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> replyService.findById(2),
                "Should throw NotFoundException");
    }
    @Test
    void deleteByIDSuccessfulTest() {

        when(replyRepository.findById(1)).thenReturn(Optional.of(new Reply()));

        ResponseEntity<?> response = replyService.deleteById(1);

        assertEquals("Deletion of a reply by id", HttpStatus.OK , response.getStatusCode());

    }
    @Test
    void deleteByIdNotSuccessfulTest() {
        when(replyRepository.findById(666)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> replyService.deleteById(666),
                "Should throw NotFoundException");
    }
    @Test
    void findReplyByCommentIdSuccessfulTest() {
        List<Reply> replyList = new ArrayList<>(Arrays.asList(new Reply(), new Reply()));
        when(replyRepository.findRepliesByCommentId(2)).thenReturn(replyList);

        ResponseEntity<?> response = replyService.findRepliesByCommentId(2);

        assertEquals("Finding replies by comment id", HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void findReplyByCommentIdUnSuccessfulTest() {
        when(replyRepository.findRepliesByCommentId(666)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> replyService.findRepliesByCommentId(666)
                , "Should throw NotFoundException");
    }
    @Test
    void setLikesDislikesForReplyTest() {
        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.reply, false)).thenReturn(3);
        when(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                1, EntityType.reply, true)).thenReturn(2);
        Reply reply = new Reply();
        reply.setId(1);

        replyService.setLikesDislikes(reply);

        assertEquals("Likes weren't assigned properly", 2, reply.getLikes());
        assertEquals("Dislikes weren't assigned properly", 3, reply.getDislikes());
    }
}
