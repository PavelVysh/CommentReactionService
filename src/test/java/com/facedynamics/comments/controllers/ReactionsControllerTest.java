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

import static org.hamcrest.Matchers.hasSize;
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
}


