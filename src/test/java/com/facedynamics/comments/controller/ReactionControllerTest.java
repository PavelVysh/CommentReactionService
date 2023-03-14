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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
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

    }
    @Test
    void findReactionForEntityTest() throws Exception {
        when(reactionsService.findReactionsForEntity(1, EntityType.post, false))
                .thenReturn(Arrays.asList(new ReactionReturnDTO(), new ReactionReturnDTO()));

        mvc.perform(get(REACTIONS + "/{id}", 1)
                .param("entityType", "post")
                .param("isLike", "false"))
                .andExpect(status().isOk());

    }
    @Test
    void findReactionsByUserTest() throws Exception {
        when(reactionsService.findAllByUserIdAndType(1, EntityType.post, true))
                .thenReturn(Arrays.asList(new ReactionReturnDTO(), new ReactionReturnDTO()));

        mvc.perform(get(REACTIONS + "/users/{id}", 1)
                .param("entityType", "post")
                .param("isLike", "true"))
                .andExpect(status().isOk());


    }
    @Test
    void deleteReactionTest() throws Exception {
        when(reactionsService.deleteReaction(1, EntityType.post, 2))
                .thenReturn("test text");

        mvc.perform(delete(REACTIONS + "/{id}", 1)
                .param("entityType", "post")
                .param("userId", "2"))
                .andExpect(status().isOk());

        }
}
