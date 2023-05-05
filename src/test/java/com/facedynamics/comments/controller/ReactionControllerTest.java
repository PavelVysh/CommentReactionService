package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.ReactionMapper;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReactionsController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockMvc(addFilters = false)
public class ReactionControllerTest {

    public static final String REACTIONS = "/reactions";
    @MockBean
    private ReactionsService reactionsService;
    @Autowired
    private MockMvc mvc;
    private final ReactionMapper mapper = Mappers.getMapper(ReactionMapper.class);
    public static final String reactionJson = """
            {
            "userId": 1,
            "entityId": 2,
            "entityType": "post",
            "like": true
            }
            """;

    @Test
    void saveTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setUserId(1);
        reaction.setEntityId(2);
        reaction.setEntityType(EntityType.post);
        reaction.setLike(true);

        when(reactionsService.save(any())).thenReturn(mapper.toSaveDTO(reaction));

        mvc.perform(post(REACTIONS)
                        .content(reactionJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.entityId", is(2)))
                .andExpect(jsonPath("$.entityType", is("post")))
                .andExpect(jsonPath("$.like", is(true)));

        verify(reactionsService, times(1)).save(any());
    }

    @Test
    void saveWithoutUserIdTest() throws Exception {
        String badReaction = """
                {
                "entityId": 2,
                "entityType": "post",
                "like": true
                }
                """;
        mvc.perform(post(REACTIONS)
                        .content(badReaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problems", hasSize(1)))
                .andExpect(jsonPath("$.problems[0].message", is("You must provide userId")));
    }

    @Test
    void saveWithoutIsLikeTest() throws Exception {
        String badReaction = """
                {
                "userId": 1,
                "entityId": 2,
                "entityType": "post"
                }
                """;
        mvc.perform(post(REACTIONS)
                        .content(badReaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problems", hasSize(1)))
                .andExpect(jsonPath("$.problems[0].message",
                        is("You must specify is this a like (like=true/false)")));
    }

    @Test
    void saveEmptyBodyTest() throws Exception {
        mvc.perform(post(REACTIONS)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void findInvalidEntityTypeTest() throws Exception {
        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "shmost")
                        .param("isLike", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Wrong parameter type")));
    }

    @Test
    void findInvalidIsLikeTest() throws Exception {
        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "something")
                        .param("isLike", "maybe"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Wrong parameter type")));
    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        when(reactionsService.deleteReaction(555, EntityType.post, 333)).thenReturn(new DeleteDTO(0));

        mvc.perform(delete(REACTIONS + "/{id}", 555)
                        .param("entityType", "post")
                        .param("userId", "333"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"rowsAffected\":0}"));
        verify(reactionsService, times(1)).deleteReaction(555, EntityType.post, 333);
    }

    @Test
    void deleteReactionTest() throws Exception {
        DeleteDTO deleted = new DeleteDTO(2);
        when(reactionsService.deleteReaction(1, EntityType.post, 2))
                .thenReturn(deleted);

        mvc.perform(delete(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("userId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rowsAffected", is(2)));

        verify(reactionsService, times(1)).deleteReaction(1, EntityType.post, 2);
    }
}
