package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ReactionsController {

    private final ReactionsService reactionsService;

    @PostMapping("/reactions")
    public ReactionSaveDTO createReaction(@RequestBody @Valid Reaction reaction) {
        return reactionsService.save(reaction);
    }
    @GetMapping("/reactions/{entityId}")
    public Page<ReactionReturnDTO> getReactionsForEntity(@PathVariable int entityId,
                                                         @RequestParam EntityType entityType,
                                                         @RequestParam boolean isLike,
                                                         Pageable pageable) {
        return reactionsService.findByEntity(entityId, entityType, isLike, pageable);
    }
    @GetMapping("/users/{userId}/reactions")
    public Page<ReactionReturnDTO> getReactionsByUserId(@PathVariable int userId,
                                                        @RequestParam EntityType entityType,
                                                        @RequestParam boolean isLike,
                                                        Pageable pageable) {
        return reactionsService.findByUser(userId, entityType, isLike, pageable);
    }

    @DeleteMapping("/reactions/{entityId}")
    public DeleteDTO deleteReaction(@PathVariable int entityId,
                                        @RequestParam int userId,
                                        @RequestParam EntityType entityType) {
        return reactionsService.deleteReaction(entityId, entityType, userId);
    }

}
