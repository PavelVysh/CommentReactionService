package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReactionsController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReactionControllerTest {

    public static final String REACTIONS = "/reactions";
    @MockBean
    private ReactionsService reactionsService;
    @Autowired
    private MockMvc mvc;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);
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

        when(reactionsService.save(any())).thenReturn(mapper.reactionToSaveDTO(reaction));

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
    void emptyBodySaveTest() throws Exception {
        mvc.perform(post(REACTIONS)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void findReactionForEntityTest() throws Exception {
        when(reactionsService.findByEntity(1, EntityType.post, false, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ReactionReturnDTO(), new ReactionReturnDTO())));

        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("isLike", "false")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(reactionsService, times(1)).findByEntity(1, EntityType.post, false,
                PageRequest.of(0, 10));
    }

    @Test
    void findReactionsByUserTest() throws Exception {
        when(reactionsService.findByUser(1, EntityType.post, true, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ReactionReturnDTO(), new ReactionReturnDTO())));

        mvc.perform(get("/users/{id}" + REACTIONS, 1)
                        .param("entityType", "post")
                        .param("isLike", "true")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(reactionsService, times(1)).findByUser(1, EntityType.post, true,
                PageRequest.of(0, 10));
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
                        .param("entityType", "post")
                        .param("isLike", "maybe"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Wrong parameter type")));
    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        when(reactionsService.delete(555, EntityType.post, 333)).thenReturn(new DeleteDTO(0));

        mvc.perform(delete(REACTIONS + "/{id}", 555)
                        .param("entityType", "post")
                        .param("userId", "333"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"rowsAffected\":0}"));
        verify(reactionsService, times(1)).delete(555, EntityType.post, 333);
    }

    @Test
    void deleteReactionTest() throws Exception {
        DeleteDTO deleted = new DeleteDTO(2);
        when(reactionsService.delete(1, EntityType.post, 2))
                .thenReturn(deleted);

        mvc.perform(delete(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("userId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rowsAffected", is(2)));

        verify(reactionsService, times(1)).delete(1, EntityType.post, 2);
    }
}
