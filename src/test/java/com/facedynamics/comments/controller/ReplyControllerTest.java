package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReplyControllerTest {
    @Mock
    private ReplyService replyService;
    private ReplyController replyController;

    @BeforeEach
    void init() {
        replyController = new ReplyController(replyService);
    }
    @Test
    void saveTest() {
        Reply replyWithId = new Reply();
        replyWithId.setId(1);
        replyWithId.setCommentId(1);
        replyWithId.setUserId(1);
        replyWithId.setText("hehe");
        when(replyService.save(replyWithId)).thenReturn(DTOMapper.fromReplyToReplyDTO(replyWithId));

        ReplyDTO replyDTO = replyController.createReply(replyWithId);

        assertEquals("saving a comment", "hehe", replyDTO.getText());
    }
    @Test
    void findByIdTest() {
        when(replyService.findById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> entity = replyController.findById(1);

        assertEquals("find reply by id", HttpStatus.OK, entity.getStatusCode());
    }
    @Test
    void findByCommentIdTest() {
        when(replyService.findRepliesByCommentId(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> entity = replyController.findRepliesByCommentId(1);

        assertEquals("find by comment id", HttpStatus.OK, entity.getStatusCode());
    }
    @Test
    void deleteReplyTest() {
        when(replyService.deleteById(1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> entity = replyController.deleteById(1);

        assertEquals("deleting reply", HttpStatus.OK, entity.getStatusCode());
    }

}
