package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReactionsService {
    private final ReactionsRepository repository;
    private final CommentRepository commentRepository;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);

    public ReactionSaveDTO save(Reaction reaction) {
        if (!checkEntityExists(reaction)) {
            throw new NotFoundException(reaction.getEntityType() + " with id - " + reaction.getEntityId() + " doesn't exist");
        }
        return mapper.reactionToSaveDTO(repository.save(reaction));
    }

    public List<ReactionReturnDTO> findReactions(int entityId,
                                                          EntityType entityType,
                                                          boolean isLike,
                                                          boolean byUser) {
        if (byUser) {
             return findByUser(entityId, entityType, isLike);
        } else {
            return findByEntity(entityId, entityType, isLike);
        }
    }

    @Transactional
    public String deleteReaction(int entityId, EntityType entityType, int userId) {
        if (repository.deleteByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId) > 0) {
            return "Reaction was successfully deleted";
        } else {
            throw new NotFoundException("Reaction was not found");
        }

    }

    private boolean checkEntityExists(Reaction reaction) {
        return switch (reaction.getEntityType().name()) {
            case "comment" -> commentRepository.existsById(reaction.getEntityId());
            case "post" -> true; //TODO
            default -> false;
        };
    }
    public List<ReactionReturnDTO> findByEntity(int entityId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByEntityIdAndEntityTypeAndLike(entityId, entityType, isLike);
        if (reactions.size() < 1) {
            throw new NotFoundException("Reactions not found");
        }
        return mapper.reactionToReturnDTO(reactions);
    }
    public List<ReactionReturnDTO> findByUser(int userId, EntityType entityType, boolean isLike) {
        List<Reaction> reactions = repository.findAllByUserIdAndEntityTypeAndLike(userId, entityType, isLike);
        if (reactions.size() < 1) {
            throw new NotFoundException("Reaction not found");
        }
        return mapper.reactionToReturnDTO(reactions);
    }
}
