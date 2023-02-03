package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.ReactionsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReactionsService {
    private final ReactionsRepository repository;

    public ReactionsService(ReactionsRepository repository) {
        this.repository = repository;
    }
    @Transactional
    public void save(Reaction reaction) {
        if (repository.existsByEntityIdAndEntityTypeAndUserId(reaction.getEntityId(),
                reaction.getEntityType(), reaction.getUserId())) {
            repository.changeReactionToOpposite(reaction.getEntityId(), reaction.getEntityType(),
                    reaction.getUserId(), reaction.isLike());
        } else {
            repository.save(reaction);
        }
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
