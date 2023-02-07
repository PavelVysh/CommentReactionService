package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.service.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReplyController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReplyControllerTest {
    @MockBean
    private ReplyService replyService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Reply replyWithId;

    @BeforeEach
    void init() {
        replyWithId = new Reply();
        replyWithId.setId(1);
        replyWithId.setCommentId(1);
        replyWithId.setUserId(1);
        replyWithId.setText("hehe");
    }
    @Test
    void saveTest() throws Exception {

        when(replyService.save(replyWithId)).thenReturn(DTOMapper.fromReplyToReplyDTO(replyWithId));

        mvc.perform(post("/replies")
                        .content(objectMapper.writeValueAsString(replyWithId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    void findByIdTest() throws Exception {
        when(replyService.findById(1)).thenReturn(new Reply());

        mvc.perform(get("/replies/{id}", 1))
                        .andExpect(status().isOk());

    }
    @Test
    void findByCommentIdTest() throws Exception {
        when(replyService.findRepliesByCommentId(1)).thenReturn(Arrays.asList(new Reply(), new Reply()));

        mvc.perform(get("/replies/comments/{id}", 1))
                        .andExpect(status().isOk());
    }
    @Test
    void deleteReplyTest() throws Exception {
        when(replyService.deleteById(1)).thenReturn("OK");

        mvc.perform(delete("/replies/{id}", 1)).andExpect(status().isOk());

    }

}
