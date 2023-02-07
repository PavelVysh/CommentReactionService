package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReactionControllerTest {

    @Mock
    private ReactionsService reactionsService;
    private ReactionsController reactionsController;

    @BeforeEach
    void init() {
        reactionsController = new ReactionsController(reactionsService);
    }
    @Test
    void saveTest() {
        Reaction reaction = new Reaction();
        Reaction reactionWithId = new Reaction();
        reactionWithId.setId(1);

        when(reactionsService.save(reaction)).thenReturn(reactionWithId);

        Reaction returnedReaction = reactionsController.createReaction(reaction);

        assertEquals("saving reaction", 1, returnedReaction.getId());
    }
    @Test
    void findReactionForEntityTest() {
        when(reactionsService.findReactionsForEntity(1, EntityType.post, false))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = reactionsController.getReactionsForEntity(1, EntityType.post, false);

        assertEquals("finding reactions by entity", HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void findReactionsByUserTest() {
        when(reactionsService.findAllByUserIdAndType(1, EntityType.post, true))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = reactionsController.getReactionsByUser(1, EntityType.post, true);

        assertEquals("finding reactions by userID", HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deleteReactionTest() {
        when(reactionsService.deleteReaction(1, EntityType.post, 2))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = reactionsController.deleteReaction(1, 2, EntityType.post);

        assertEquals("deleting a response", HttpStatus.OK, response.getStatusCode());
        }
}
