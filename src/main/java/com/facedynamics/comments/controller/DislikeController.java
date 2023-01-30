package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Dislike;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.DislikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dislikes")
public class DislikeController {

    private final DislikeService dislikeService;

    public DislikeController(DislikeService dislikeService) {
        this.dislikeService = dislikeService;
    }
    @PostMapping
    public void createDislike(@RequestBody Dislike dislike) {
        dislikeService.save(dislike);
    }
    @GetMapping("/{entityId}")
    public List<Dislike> getDislikesForEntity(@PathVariable int entityId) {
        return dislikeService.findDislikesForEntity(entityId);
    }
    @DeleteMapping("/{dislikeId}")
    public void deleteDislike(@PathVariable int dislikeId) {
        dislikeService.deleteDislikeById(dislikeId);
    }
    @GetMapping("/user/{userId}")
    public List<Dislike> getDislikesByUser(@PathVariable int userId, @RequestParam EntityType entityType) {
        return dislikeService.findDislikeByUserIdAndEntityType(userId, entityType);
    }
}
