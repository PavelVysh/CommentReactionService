package integrational;

import com.facedynamics.comments.CommentsApplication;
import com.facedynamics.comments.entity.Comment;
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

import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CommentsApplication.class)
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class CommentTests {

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

        assertTrue("Likes assigned incorrectly",  contentAsString.contains("\"likes\":3"));
        assertTrue("Dislikes assigned incorrectly",  contentAsString.contains("\"dislikes\":2"));
    }

    @Test
    void commentControllerPostTest() throws Exception {
        Comment comment = new Comment();
        comment.setUserId(123);
        comment.setPostId(321);
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void commentControllerDeleteMethodTest() throws Exception {
        mvc.perform(delete("/comments/{id}", 1)
                        .param("type", "comment"))
                .andExpect(status().isOk());

        mvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void commentControllerGetListMethodTest() throws Exception {
        mvc.perform(get("/comments/{id}", 4)
                        .param("post", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void commentValidationTextFieldExceptionTest() throws Exception {
        Comment comment = new Comment();
        comment.setUserId(456);
        comment.setPostId(789);
        comment.setText("i");

        mvc.perform(post("/comments")
                        .content(new ObjectMapper().writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletingNonExistingCommentTest() throws Exception {
        MvcResult result = mvc.perform(delete("/comments/{id}", 5678)
                        .param("type", "comment"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue("should be message about 0 deleted comments",
                result.getResponse()
                        .getContentAsString().contains("0 comment(s) have been deleted"));
    }

    @Test
    void findCommentsForNonExistingPost() throws Exception {
        MvcResult result = mvc.perform(get("/comments/{postId}", 5678)
                        .param("post", "true"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue("should be message about comments non existing for post {postID}",
                result.getResponse()
                        .getContentAsString().contains("Comments for post with id 5678 were not found"));
    }
}
