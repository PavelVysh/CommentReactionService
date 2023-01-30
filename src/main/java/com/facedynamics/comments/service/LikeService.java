package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Like;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.LikeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class LikeService {

    private LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    public void save(Like like) {
        likeRepository.save(like);
    }
    public List<Like> findLikesForEntity(int entityId) {
        return likeRepository.findLikesByEntityId(entityId);
    }
    public void deleteById(int likeId) {
        likeRepository.deleteById(likeId);
    }
    public List<Like> findLikeByUserIdAndEntityType(int userId, EntityType entityType) {
        return likeRepository.findLikesByUserIdAndEntityType(userId, entityType);
    }
}
