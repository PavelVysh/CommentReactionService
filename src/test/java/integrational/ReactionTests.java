package integrational;

import com.facedynamics.comments.CommentsApplication;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CommentsApplication.class)
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class ReactionTests {
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
        mvc.perform(get("/reactions/users/{userId}", 1)
                        .param("isLike", "false")
                        .param("entityType", "comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void reactionsControllerDeleteMethodTest() throws Exception {
        mvc.perform(delete("/reactions/{entityId}", 2)
                        .param("userId", "333")
                        .param("entityType", "comment"))
                .andExpect(status().isOk());
        mvc.perform(get("/reactions/{entityId}", 2)
                        .param("entityType", "comment")
                        .param("isLike", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    void reactionsControllerPostMethodTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setLike(true);
        reaction.setEntityId(555);
        reaction.setUserId(333);
        reaction.setEntityType(EntityType.post);

        mvc.perform(post("/reactions")
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get("/reactions/{entityId}", 555)
                        .param("entityType", "post")
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
        reaction.setEntityType(EntityType.post);
        MvcResult resultOne = mvc.perform(post("/reactions")
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        Reaction saved = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(
                resultOne.getResponse().getContentAsString(), Reaction.class);

        MvcResult resultTwo = mvc.perform(post("/reactions/{id}", saved.getId()))
                        .andExpect(status().isOk()).andReturn();

        Reaction switchedReaction = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(
                resultTwo.getResponse().getContentAsString(), Reaction.class);


        assertFalse("didn't switch like to dislike", switchedReaction.isLike());
        assertEquals("new Reaction created instead of switch", saved.getId(), switchedReaction.getId());
    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        MvcResult result = mvc.perform(delete("/reactions/{entityId}", 5678)
                        .param("entityType", "post")
                        .param("userId", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue("tried to delete a non existing reaction",
                result.getResponse().getContentAsString().contains("Reaction was not found"));
    }
}



