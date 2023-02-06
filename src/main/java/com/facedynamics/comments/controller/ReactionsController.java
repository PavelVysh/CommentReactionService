package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.ReactionsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reactions")
public class ReactionsController {

    private final ReactionsService reactionsService;

    public ReactionsController(ReactionsService reactionsService) {
        this.reactionsService = reactionsService;
    }

    @PostMapping
    public void createLike(@RequestBody @Valid Reaction reaction) {
        reactionsService.save(reaction);
    }

    @GetMapping("/{entityId}")
    public ResponseEntity<?> getReactionsForEntity(@PathVariable int entityId,
                                                @RequestParam EntityType entityType,
                                                @RequestParam boolean isLike) {
        return reactionsService.findReactionsForEntity(entityId, entityType, isLike);
    }

    @DeleteMapping("/{entityId}")
    public ResponseEntity<?> deleteReaction(@PathVariable int entityId,
                                        @RequestParam int userId,
                                        @RequestParam EntityType entityType) {
        return reactionsService.deleteById(entityId, entityType, userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReactionsByUser(@PathVariable int userId,
                                             @RequestParam EntityType entityType,
                                             @RequestParam boolean isLike) {
        return reactionsService.findAllByUserIdAndType(userId, entityType, isLike);
    }
}
