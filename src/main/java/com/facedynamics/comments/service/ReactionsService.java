package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.ReactionsRepository;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReactionsService {
    private final ReactionsRepository repository;

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

    public ResponseEntity<List<Reaction>> findReactionsForEntity(int entityId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByEntityIdAndEntityTypeAndLike(entityId, entityType, isLike);
        if (reactions.size() < 1) {
            throw new NotFoundException("Reactions not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reactions);
    }

    @Transactional
    public ResponseEntity<String> deleteReaction(int entityId, EntityType entityType, int userId) {
        if (repository.existsByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId)) {
            repository.deleteByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Reaction was successfully deleted");
        } else {
            throw new NotFoundException("Reaction was not found");
        }

    }

    public ResponseEntity<List<Reaction>> findAllByUserIdAndType(int userId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByUserIdAndEntityTypeAndLike(userId, entityType, isLike);
        if (reactions.size() < 1) {
            throw new NotFoundException("Reaction not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reactions);
    }
}
