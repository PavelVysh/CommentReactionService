package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                                                         @RequestParam boolean isLike,
                                                         @RequestParam(required = false) boolean byUser,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size,
                                                         @RequestParam(required = false, defaultValue = "updateTime") String sort,
                                                         @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction) {
        return reactionsService.findReactions(entityId, entityType, isLike, byUser, PageRequest.of(page,size, Sort.by(direction ,sort)));
    }

    @DeleteMapping("/{entityId}")
    public String deleteReaction(@PathVariable int entityId,
                                        @RequestParam int userId,
                                        @RequestParam EntityType entityType) {
        return reactionsService.deleteReaction(entityId, entityType, userId);
    }

}
