package integrational;

import com.facedynamics.comments.CommentsApplication;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CommentsApplication.class)
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class ReactionTests {
    public static final String REACTIONS = "/reactions";
    @Autowired
    private MockMvc mvc;

    @Test
    void reactionsControllerGetByEntityIdMethodTest() throws Exception {
        mvc.perform(get(REACTIONS + "/{entityId}", 1)
                        .param("entityType", "comment")
                        .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void reactionsControllerGetByUserIDMethodTest() throws Exception {
        mvc.perform(get("/users/{userId}" + REACTIONS, 1)
                        .param("isLike", "false")
                        .param("entityType", "comment")
                        .param("user", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void reactionsControllerDeleteMethodTest() throws Exception {
        mvc.perform(delete(REACTIONS + "/{entityId}", 2)
                        .param("userId", "333")
                        .param("entityType", "comment"))
                .andExpect(status().isOk());
        mvc.perform(get(REACTIONS + "/{entityId}", 2)
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

        mvc.perform(post(REACTIONS)
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get(REACTIONS + "/{entityId}", 555)
                        .param("entityType", "post")
                        .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].entityType", is("post")))
                .andExpect(jsonPath("$.content[0].userId", is(333)))
                .andExpect(jsonPath("$.content[0].entityId", is(555)));
    }

    @Test
    void updateLikeToDislikeTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setLike(true);
        reaction.setEntityId(444);
        reaction.setUserId(22);
        reaction.setEntityType(EntityType.post);
        mvc.perform(post(REACTIONS)
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        reaction.setLike(false);
        mvc.perform(post(REACTIONS)
                .content(new ObjectMapper().writeValueAsString(reaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        ResultActions resultActions = mvc.perform(get(REACTIONS + "/{entityId}", 444)
                        .param("entityType", "post")
                        .param("isLike", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].like", is(false)));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        assertTrue("didn't switch like to dislike", contentAsString.contains("\"like\":false"));
    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        MvcResult result = mvc.perform(delete(REACTIONS + "/{entityId}", 5678)
                        .param("entityType", "post")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue("tried to delete a non existing reaction",
                result.getResponse().getContentAsString().contains("\"rowsAffected\":0"));
    }

    @Test
    void deleteReactionBadRequestTest() throws Exception {
        mvc.perform(delete(REACTIONS + "/{entityId}", 3))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail",
                        is("Required request parameter 'userId' for method parameter type int is not present")));
    }

    @Test
    void requestWithNoBodyTest() throws Exception {
        mvc.perform(post(REACTIONS))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }
}



