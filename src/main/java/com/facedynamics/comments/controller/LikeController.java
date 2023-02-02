package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Like;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @PostMapping
    public void createLike(@RequestBody Like like) {
        likeService.save(like);
    }
    @GetMapping("/{entityId}")
    public List<Like> getLikesForEntity(@PathVariable int entityId) {
        return likeService.findLikesForEntity(entityId);
    }
    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable int likeId) {
        likeService.deleteById(likeId);
    }
    @GetMapping("/user/{userId}")
    public List<Like> getLikesByUser(@PathVariable int userId, @RequestParam EntityType entityType) {
        return likeService.findLikeByUserIdAndEntityType(userId, entityType);
    }
}
