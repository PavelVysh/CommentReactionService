package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
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
    @Mock
    private ReplyRepository replyRepository;
    private ReactionsService reactionsService;
    private Reaction reaction;

    @BeforeEach
    void init() {
        reactionsService = new ReactionsService(reactionsRepository, commentRepository, replyRepository);
        reaction = new Reaction();
        reaction.setId(15);
        reaction.setUserId(1);
        reaction.setEntityType(EntityType.post);
        reaction.setEntityId(2);
        reaction.setLike(true);
    }
    @Test
    void saveReactionTest() {

        when(reactionsRepository.save(reaction)).thenReturn(reaction);

        Reaction afterSave = reactionsService.save(reaction);

        assertEquals("Saving reaction test", 2, afterSave.getEntityId());
    }
    @Test
    void switchLikeToDislikeTest() {

        when(reactionsRepository.existsByEntityIdAndEntityTypeAndUserId(2, EntityType.post, 1))
                .thenReturn(true);
        when(reactionsRepository.changeReactionToOpposite(2, EntityType.post, 1, true))
                .thenReturn(1);
        when(reactionsRepository.findByEntityIdAndEntityTypeAndUserIdAndLike(2, EntityType.post, 1, true))
                .thenReturn(reaction);

        Reaction afterChange = reactionsService.save(reaction);
        assertEquals("changing reaction to oposite", true, afterChange.isLike());
    }
    @Test
    void findingReactionTestSuccessfulTest() {
        when(reactionsRepository.findAllByEntityIdAndEntityTypeAndLike(2, EntityType.post, true))
                .thenReturn(Arrays.asList(new Reaction(), new Reaction()));

        List<Reaction> response = reactionsService.findReactionsForEntity(2, EntityType.post, true);
        assertEquals("finding reactions for entity", 2, response.size());
    }
    @Test
    void findingReactionTestNotFoundTest() {
        when(reactionsRepository.findAllByEntityIdAndEntityTypeAndLike(2, EntityType.post, true))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () ->
                reactionsService.findReactionsForEntity(2, EntityType.post, true),
                "Should throw NotFoundException");
    }
    @Test
    void deleteReactionSuccessfulTest() {
        when(reactionsRepository.deleteByEntityIdAndEntityTypeAndUserId(4, EntityType.reply, 33))
                .thenReturn(1);

        String response = reactionsService.deleteReaction(4, EntityType.reply, 33);

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
        when(reactionsRepository.findAllByUserIdAndEntityTypeAndLike(1, EntityType.post, true))
                .thenReturn(Arrays.asList(new Reaction(), reaction));

        List<Reaction> response = reactionsService.findAllByUserIdAndType(1, EntityType.post, true);

        assertEquals("Finding reactions successfully", 2, response.size());
    }
    @Test
    void findByUserIdAndTypeFailTest() {
        when(reactionsRepository.findAllByUserIdAndEntityTypeAndLike(1, EntityType.post, true))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () ->
                reactionsService.findAllByUserIdAndType(1, EntityType.post, true),
                "Should throw NotFoundException");
    }
}
