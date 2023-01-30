package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Dislike;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.DislikeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class DislikeService {

    private DislikeRepository dislikeRepository;

    public DislikeService(DislikeRepository dislikeRepository) {
        this.dislikeRepository = dislikeRepository;
    }
    public void save(Dislike dislike) {
        dislikeRepository.save(dislike);
    }
    public List<Dislike> findDislikesForEntity(int entityId) {
        return dislikeRepository.findDislikeByEntityId(entityId);
    }
    public void deleteDislikeById(int dislikeId) {
        dislikeRepository.deleteById(dislikeId);
    }
    public List<Dislike> findDislikeByUserIdAndEntityType(int userId, EntityType entityType) {
        return dislikeRepository.findDislikeByUserIdAndEntityType(userId, entityType);
    }
}
