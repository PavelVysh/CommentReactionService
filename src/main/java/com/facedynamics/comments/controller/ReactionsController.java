package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reactions")
public class ReactionsController {

    private final ReactionsService reactionsService;

    public ReactionsController(ReactionsService reactionsService) {
        this.reactionsService = reactionsService;
    }
    @PostMapping
    public void createLike(@RequestBody Reaction reaction) {
        reactionsService.save(reaction);
    }
    @GetMapping("/{entityId}")
    public List<Reaction> getLikesForEntity(@PathVariable int entityId, @RequestParam boolean isLike) {
        return reactionsService.findReactionsForEntity(entityId, isLike);
    }
    @DeleteMapping("/{reactionId}")
    public void deleteLike(@PathVariable int reactionId) {
        reactionsService.deleteById(reactionId);
    }
    @GetMapping("/user/{userId}")
    public List<Reaction> getReactionsByUser(@PathVariable int userId,
                                             @RequestParam EntityType entityType,
                                             @RequestParam boolean isLike) {
        return reactionsService.findAllByUserIdAndType(userId, entityType, isLike);
    }
}
