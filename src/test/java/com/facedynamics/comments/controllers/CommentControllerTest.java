package com.facedynamics.comments.controllers;

import com.facedynamics.comments.entity.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void likesDislikesAssignedTest() throws Exception {

        ResultActions resultActions = mvc.perform(get("/comments/1"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Comment response = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(contentAsString, Comment.class);

        assertEquals("Likes assigned incorrectly", 3, response.getLikes());
        assertEquals("Dislikes assigned incorrectly", 2, response.getDislikes());
    }
    @Test
    void commentControllerPostTest() throws Exception {
        Comment comment = new Comment();
        comment.setText("i am a sample text to match");
        ResultActions resultActions = mvc.perform(post("/comments")
                .content(new ObjectMapper().writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        MvcResult result = resultActions.andReturn();
        int idOfSavedComment = objectMapper.readValue(
                result.getResponse().getContentAsString(), Comment.class).getId();

        mvc.perform(get("/comments/{id}", idOfSavedComment))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text", equalTo("i am a sample text to match")));
    }
    @Test
    void commentControllerDeleteMethodTest() throws Exception {
        mvc.perform(delete("/comments/{id}", 1))
                .andExpect(status().isOk());

        mvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isNoContent());
    }
    @Test
    void commentControllerGetListMethodTest() throws Exception {
        mvc.perform(get("/comments/posts/{id}", 4))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
