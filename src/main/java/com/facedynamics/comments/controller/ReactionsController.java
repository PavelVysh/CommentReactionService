package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.reaction.ReactionDeleteDTO;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ReactionReturnDTO> getReactionsForEntity(@PathVariable int entityId,
                                                         @RequestParam EntityType entityType,
                                                         @RequestParam boolean isLike) {
        return reactionsService.findReactionsForEntity(entityId, entityType, isLike);
    }

    @DeleteMapping("/{entityId}")
    public ReactionDeleteDTO deleteReaction(@PathVariable int entityId,
                                            @RequestParam int userId,
                                            @RequestParam EntityType entityType) {
        return reactionsService.deleteReaction(entityId, entityType, userId);
    }

    @GetMapping("/users/{userId}")
    public List<ReactionReturnDTO> getReactionsByUser(@PathVariable int userId,
                                             @RequestParam EntityType entityType,
                                             @RequestParam boolean isLike) {
        return reactionsService.findAllByUserIdAndType(userId, entityType, isLike);
    }
}
