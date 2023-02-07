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
    public Reaction save(Reaction reaction) {
        if (repository.existsByEntityIdAndEntityTypeAndUserId(reaction.getEntityId(),
                reaction.getEntityType(), reaction.getUserId())) {
            repository.changeReactionToOpposite(reaction.getEntityId(), reaction.getEntityType(),
                    reaction.getUserId(), reaction.isLike());
            return repository.findByEntityIdAndEntityTypeAndUserIdAndLike(
                    reaction.getEntityId(), reaction.getEntityType(),
                    reaction.getUserId(), reaction.isLike());
        }
        return repository.save(reaction);
    }

    public ResponseEntity<?> findReactionsForEntity(int entityId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByEntityIdAndEntityTypeAndLike(entityId, entityType, isLike);
        if (reactions.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reactions not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reactions);
    }

    @Transactional
    public ResponseEntity<?> deleteReaction(int entityId, EntityType entityType, int userId) {
        if (repository.existsByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId)) {
            repository.deleteByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Reaction was successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reaction was not found");
        }

    }

    public ResponseEntity<?> findAllByUserIdAndType(int userId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByUserIdAndEntityTypeAndLike(userId, entityType, isLike);
        if (reactions.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reaction not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reactions);
    }
}
