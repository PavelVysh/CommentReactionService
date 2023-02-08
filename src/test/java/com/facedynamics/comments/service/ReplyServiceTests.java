package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
    private static ReplyService replyService;
    @BeforeEach
    void init() {
        replyService = new ReplyService(replyRepository);
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

        Reply response = replyService.findById(2);

        assertEquals("didn't find an existing comment", 2 , response.getId());
    }
    @Test
    void findByIdUnSuccessfulTest() {
        when(replyRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> replyService.findById(2),
                "Should throw NotFoundException");
    }
    @Test
    void deleteByIDSuccessfulTest() {
        Reply reply = new Reply();
        reply.setText("test text");

        when(replyRepository.findById(1)).thenReturn(Optional.of(reply));

        String response = replyService.deleteById(1);

        assertEquals("Deletion of a reply by id", "test text" , response);

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

        List<Reply> response = replyService.findRepliesByCommentId(2);

        assertEquals("Finding replies by comment id", 2, response.size());
    }
    @Test
    void findReplyByCommentIdUnSuccessfulTest() {
        when(replyRepository.findRepliesByCommentId(666)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> replyService.findRepliesByCommentId(666)
                , "Should throw NotFoundException");
    }
}
