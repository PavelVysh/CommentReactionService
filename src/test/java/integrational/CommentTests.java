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
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CommentsApplication.class)
@AutoConfigureMockMvc
@Sql({"/dataForTests.sql"})
public class CommentTests {

    public static final String COMMENTS = "/comments";
    @Autowired
    private MockMvc mvc;

    @Test
    void saveCommentTest() throws Exception {
        String stringComment = """
                {
                "userId": "123",
                "postId": "321",
                "text": "i a, a sample text to match"
                }
                """;
        MvcResult result = mvc.perform(post(COMMENTS)
                        .content(stringComment)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        Pattern pattern = Pattern.compile("\"id\":(\\w*),");
        Matcher matcher = pattern.matcher(result.getResponse().getContentAsString());
        matcher.find();
        int idOfSavedComment = Integer.parseInt(matcher.group(1));

        mvc.perform(get(COMMENTS + "/{id}", idOfSavedComment))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idOfSavedComment))
                .andExpect(jsonPath("$.userId").value(123))
                .andExpect(jsonPath("$.postId").value(321))
                .andExpect(jsonPath("$.text").value("i a, a sample text to match"));
    }

    @Test
    void saveCommentValidationTextFieldExceptionTest() throws Exception {
        String noTextComment = """
                {
                "userId": "456",
                "postId": "789",
                "text": "i"
                }
                """;

        mvc.perform(post(COMMENTS)
                        .content(noTextComment)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problems[0].message")
                        .value("Text must be at least 2 characters long"));
    }


    @Test
    void findCommentsByPostIdTest() throws Exception {
        mvc.perform(get("/posts/{id}" + COMMENTS, 4))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void findCommentsForNonExistingPost() throws Exception {
        mvc.perform(get("/posts/{postId}" + COMMENTS, 5678))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Comments for post with id 5678 were not found")))
                .andReturn();
    }

    @Test
    void requestWithBadParamsInJsonBody() throws Exception {
        mvc.perform(get(COMMENTS + "/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Wrong parameter type")))
                .andReturn();

    }

    @Test
    void deleteCommentTest() throws Exception {
        mvc.perform(delete(COMMENTS + "/{id}", 1))
                .andExpect(status().isOk());

        mvc.perform(get(COMMENTS + "/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Comment with id - 1 was not found")));
    }

    @Test
    void deleteNonExistingCommentTest() throws Exception {
        mvc.perform(delete(COMMENTS + "/{id}", 5678))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rowsAffected", is(0)));

    }

    @Test
    void likesDislikesAssignedTest() throws Exception {

        ResultActions resultActions = mvc.perform(get(COMMENTS + "/1"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        assertTrue("Likes assigned incorrectly", contentAsString.contains("\"likes\":3"));
        assertTrue("Dislikes assigned incorrectly", contentAsString.contains("\"dislikes\":2"));
    }

}
