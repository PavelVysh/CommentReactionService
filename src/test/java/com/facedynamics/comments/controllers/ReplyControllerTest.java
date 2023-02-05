package com.facedynamics.comments.controllers;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reply;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class ReplyControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void replyControllerGetTest() throws Exception {
        mvc.perform(get("/replies/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text",
                        equalTo("I am a second reply made for comment number three by user 2")));
    }

    @Test
    void replyControllerDeleteTest() throws Exception {
        mvc.perform(delete("/replies/{id}", 3))
                .andExpect(status().isOk());
        mvc.perform(get("/replies/{id}", 3))
                .andExpect(status().isNotFound());
    }

    @Test
    void replyControllerGetListTest() throws Exception {
        mvc.perform(get("/replies/comments/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void replyControllerPostTest() throws Exception {
        Reply reply = new Reply();
        reply.setText("two chars");
        reply.setCommentId(1);
        reply.setUserId(231);
        ResultActions resultActions = mvc.perform(post("/replies")
                .content(new ObjectMapper().writeValueAsString(reply))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        MvcResult result = resultActions.andReturn();
        int idOfSavedReply = objectMapper.readValue(
                result.getResponse().getContentAsString(), Comment.class).getId();


        mvc.perform(get("/replies/{id}", idOfSavedReply))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text", equalTo("two chars")));
    }
    @Test
    void deleteNonExistingReplyTest() throws Exception {
        MvcResult result = mvc.perform(delete("/replies/{id}", 5678))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("should be message about reply {id} not found",
                "Reply with id - 5678 was not found",
                result.getResponse().getContentAsString());
    }

    @Test
    void findRepliesForNonExistingComments() throws Exception {
        MvcResult result = mvc.perform(get("/replies/comments/{commentId}", 5678))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("should be message about reply non existing for comment {commentID}",
                "Replies for comment with id - 5678 were not found",
                result.getResponse().getContentAsString());
    }
}
