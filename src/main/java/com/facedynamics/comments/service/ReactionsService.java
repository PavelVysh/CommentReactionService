package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.ReactionsRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Transactional
    public ResponseEntity<?> deleteById(int entityId, EntityType entityType, int userId) {
        if (repository.existsByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId)) {
            repository.deleteByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Reaction was succesfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reaction was not found");
        }

    }
    public List<Reaction> findAllByUserIdAndType(int userId, EntityType entityType, boolean isLike) {
        return repository.findAllByUserIdAndEntityTypeAndLike(userId, entityType, isLike);
    }
}
