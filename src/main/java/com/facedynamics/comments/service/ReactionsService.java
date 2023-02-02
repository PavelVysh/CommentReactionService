package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ReactionsService {
    private ReactionsRepository repository;

    public ReactionsService(ReactionsRepository repository) {
        this.repository = repository;
    }
    public void save(Reaction reaction) {
        repository.save(reaction);
    }
    public List<Reaction> findReactionsForEntity(int entityId, EntityType entityType,  boolean isLike) {
        return repository.findAllByEntityIdAndEntityTypeAndLike(entityId, entityType, isLike);
    }
    public void deleteById(int reactionId) {
        repository.deleteById(reactionId);
    }
    public List<Reaction> findAllByUserIdAndType(int userId, EntityType entityType, boolean isLike) {
        return repository.findAllByUserIdAndEntityTypeAndLike(userId, entityType, isLike);
    }
}
