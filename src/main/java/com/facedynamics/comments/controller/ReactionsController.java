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
@RequestMapping("/reactions")
@AllArgsConstructor
public class ReactionsController {

    private final ReactionsService reactionsService;

    @PostMapping
    public ReactionSaveDTO createReaction(@RequestBody @Valid Reaction reaction) {
        return reactionsService.save(reaction);
    }
    @GetMapping("/{entityId}")
    public Page<ReactionReturnDTO> getReactionsForEntity(@PathVariable int entityId,
                                                         @RequestParam EntityType entityType,
                                                         @RequestParam boolean isLike,
                                                         @RequestParam(required = false) boolean byUser,
                                                         Pageable pageable) {
        return reactionsService.findReactions(entityId, entityType, isLike, byUser, pageable);
    }

    @DeleteMapping("/{entityId}")
    public DeleteDTO deleteReaction(@PathVariable int entityId,
                                    @RequestParam int userId,
                                    @RequestParam EntityType entityType) {
        return reactionsService.deleteReaction(entityId, entityType, userId);
    }

}
