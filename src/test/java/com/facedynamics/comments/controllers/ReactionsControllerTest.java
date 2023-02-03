package com.facedynamics.comments.controllers;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class ReactionsControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void reactionsControllerGetByEntityIdMethodTest() throws Exception {
        mvc.perform(get("/reactions/{entityId}", 1)
                .param("entityType", "comment")
                .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }
    @Test
    void reactionsControllerGetByUserIDMethodTest() throws Exception {
        mvc.perform(get("/reactions/user/{userId}", 1)
                .param("isLike", "false")
                .param("entityType", "comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @Test
    void reactionsControllerDeleteMethodTest() throws Exception {
        mvc.perform(delete("/reactions/{reactionId}", 18))
                .andExpect(status().isOk());
        mvc.perform(get("/reactions/{entityId}", 4)
                .param("entityType", "reply")
                .param("isLike", "true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    void reactionsControllerPostMethodTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setId(666);
        reaction.setLike(true);
        reaction.setEntityId(555);
        reaction.setEntityType(EntityType.reply);

        mvc.perform(post("/reactions")
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/reactions/{entityId}", 555)
                        .param("entityType", "reply")
                        .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    void updateLikeToDislikeTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setLike(true);
        reaction.setEntityId(444);
        reaction.setUserId(22);
        reaction.setEntityType(EntityType.reply);
        mvc.perform(post("/reactions")
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        reaction.setLike(false);
        mvc.perform(post("/reactions")
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        ResultActions resultActions = mvc.perform(get("/reactions/{entityId}", 444)
                        .param("entityType", "reply")
                        .param("isLike", "false"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Reaction[] reactions = new ObjectMapper().readValue(contentAsString, Reaction[].class);

        assertFalse("didn't switch like to dislike",reactions[0].isLike());
        assertEquals("new Reaction created instead of switch", reactions[0].getId(), 25);
    }

}


