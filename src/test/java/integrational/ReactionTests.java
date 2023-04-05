package integrational;

import com.facedynamics.comments.CommentsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
    void reactionsControllerPostMethodTest() throws Exception {
        String stringReaction = """
                {
                "like": "true",
                "entityId": "555",
                "userId": "333",
                "entityType": "post"
                }
                """;

        mvc.perform(post(REACTIONS)
                .content(stringReaction)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get(REACTIONS + "/{entityId}", 555)
                        .param("entityType", "post")
                        .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].entityType", is("post")))
                .andExpect(jsonPath("$.content[0].userId", is(333)))
                .andExpect(jsonPath("$.content[0].entityId", is(555)))
                .andExpect(jsonPath("$.content[0].like", is(true)));
    }

    @Test
    void requestWithNoBodyTest() throws Exception {
        MvcResult result = mvc.perform(post(REACTIONS))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andReturn();
        assertTrue("Checking detail message",
                result.getResponse().getContentAsString().contains("Required request body is missing"));
    }

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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Reactions not found")));
    }


    @Test
    void updateLikeToDislikeTest() throws Exception {
        String likeReaction = """
                {
                "like": "true",
                "entityId": "444",
                "userId": "22",
                "entityType": "post"
                }
                """;
        String dislikeReaction = """
                {
                "like": "false",
                "entityId": "444",
                "userId": "22",
                "entityType": "post"
                }
                """;

        mvc.perform(post(REACTIONS)
                .content(likeReaction)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get(REACTIONS + "/{entityId}", 444)
                        .param("entityType", "post")
                        .param("isLike", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].like", is(true)));

        mvc.perform(post(REACTIONS)
                .content(dislikeReaction)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(get(REACTIONS + "/{entityId}", 444)
                        .param("entityType", "post")
                        .param("isLike", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].like", is(false)));

    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        mvc.perform(delete(REACTIONS + "/{entityId}", 5678)
                        .param("entityType", "post")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rowsAffected", is(0)));

    }

    @Test
    void deleteReactionBadRequestTest() throws Exception {
        mvc.perform(delete(REACTIONS + "/{entityId}", 3))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail",
                        is("Required request parameter 'userId' for method parameter type int is not present")));
    }

}



