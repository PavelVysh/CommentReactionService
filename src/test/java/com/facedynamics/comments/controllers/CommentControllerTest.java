package com.facedynamics.comments.controllers;

import com.facedynamics.comments.entity.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

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
}
