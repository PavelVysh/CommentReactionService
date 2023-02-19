package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReactionServiceTests {
    @Mock
    private ReactionsRepository reactionsRepository;
    @Mock
    private CommentRepository commentRepository;
    private ReactionsService reactionsService;
    private Reaction reaction;

    @BeforeEach
    void init() {
        reactionsService = new ReactionsService(reactionsRepository, commentRepository);
        reaction = new Reaction();
        reaction.setUserId(1);
        reaction.setEntityType(EntityType.post);
        reaction.setEntityId(2);
        reaction.setLike(true);
    }
    @Test
    void saveReactionTest() {

        when(reactionsRepository.save(reaction)).thenReturn(reaction);

        ReactionSaveDTO afterSave = reactionsService.save(reaction);

        assertEquals("Saving reaction test", 2, afterSave.getEntityId());
    }
    @Test
    void findingReactionTestSuccessfulTest() {
        when(reactionsRepository.findAllByEntityIdAndEntityTypeAndLike(2, EntityType.post, true, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(new Reaction(), new Reaction())));

        List<ReactionReturnDTO> response = reactionsService.findReactions(2, EntityType.post, true, false,
                PageRequest.of(0, 10));
        assertEquals("finding reactions for entity", 2, response.size());
    }
    @Test
    void findingReactionTestNotFoundTest() {
        when(reactionsRepository.findAllByEntityIdAndEntityTypeAndLike(2, EntityType.post, true, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThrows(NotFoundException.class, () ->
                reactionsService.findReactions(2, EntityType.post, true, false, PageRequest.of(0, 10)),
                "Should throw NotFoundException");
    }
    @Test
    void deleteReactionSuccessfulTest() {
        when(reactionsRepository.deleteByEntityIdAndEntityTypeAndUserId(4, EntityType.comment, 33))
                .thenReturn(1);

        String response = reactionsService.deleteReaction(4, EntityType.comment, 33);

        assertEquals("delete successfully test", "Reaction was successfully deleted", response);
    }
    @Test
    void deleteReactionFailTest() {
        when(reactionsRepository.existsByEntityIdAndEntityTypeAndUserId(2, EntityType.post, 1))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () -> reactionsService.deleteReaction(2, EntityType.post, 1)
                , "Should throw NotFoundException");
    }
    @Test
    void findByUserIdAndTypeSuccessfulTest() {
        when(reactionsRepository.findAllByUserIdAndEntityTypeAndLike(1, EntityType.post, true,
                PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(new Reaction(), reaction)));

        List<ReactionReturnDTO> response = reactionsService.findReactions(1, EntityType.post, true,
                true, PageRequest.of(0, 10));

        assertEquals("Finding reactions successfully", 2, response.size());
    }
    @Test
    void findByUserIdAndTypeFailTest() {
        when(reactionsRepository.findAllByUserIdAndEntityTypeAndLike(1, EntityType.post, true, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThrows(NotFoundException.class, () ->
                reactionsService.findReactions(1, EntityType.post, true, true, PageRequest.of(0, 10)),
                "Should throw NotFoundException");
    }
}
