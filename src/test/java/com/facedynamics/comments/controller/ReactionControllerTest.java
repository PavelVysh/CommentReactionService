package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReactionsController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReactionControllerTest {

    public static final String REACTIONS = "/reactions";
    @MockBean
    private ReactionsService reactionsService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);


    @Test
    void saveTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setUserId(1);
        reaction.setEntityId(2);
        reaction.setEntityType(EntityType.post);
        reaction.setLike(true);
        Reaction reactionWithId = new Reaction();

        when(reactionsService.save(reaction)).thenReturn(mapper.reactionToSaveDTO(reactionWithId));
        mvc.perform(post(REACTIONS)
                        .content(objectMapper.writeValueAsString(reaction))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(reactionsService, times(1)).save(any());
    }

    @Test
    void findReactionForEntityTest() throws Exception {
        when(reactionsService.findByEntity(1, EntityType.post, false, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ReactionReturnDTO(), new ReactionReturnDTO())));

        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("isLike", "false")
                        .param("size", "10"))
                .andExpect(status().isOk());

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
                .andExpect(status().isOk());
        verify(reactionsService, times(1)).findByUser(1, EntityType.post, true,
                PageRequest.of(0, 10));
    }

    @Test
    void deleteReactionTest() throws Exception {
        when(reactionsService.deleteReaction(1, EntityType.post, 2))
                .thenReturn("test text");

        mvc.perform(delete(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("userId", "2"))
                .andExpect(status().isOk());
        verify(reactionsService, times(1)).deleteReaction(1, EntityType.post, 2);
    }

    @Test
    void emptyBodySaveTest() throws Exception {
        mvc.perform(post(REACTIONS)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidUserIdRequestTest() throws Exception {
        Reaction reaction = new Reaction();
        reaction.setEntityId(2);
        reaction.setEntityType(EntityType.post);
        reaction.setLike(true);

        mvc.perform(post(REACTIONS)
                        .content(objectMapper.writeValueAsString(reaction))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidEntityTypeTest() throws Exception {
        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "shmost")
                        .param("isLike", "false"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidIsLikeTest() throws Exception {
        mvc.perform(get(REACTIONS + "/{id}", 1)
                        .param("entityType", "post")
                        .param("isLike", "maybe"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteNonExistingReactionTest() throws Exception {
        mvc.perform(delete(REACTIONS + "/{id}", 555)
                        .param("entityType", "post")
                        .param("userId",  "333"))
                .andExpect(status().isOk());
    }
}
