package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.ReactionMapper;
import com.facedynamics.comments.dto.reaction.ReactionDeleteDTO;
import com.facedynamics.comments.dto.reaction.ReactionReturnDTO;
import com.facedynamics.comments.dto.reaction.ReactionSaveDTO;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.NotificationsClient;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReactionsService {
    private final ReactionsRepository repository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final NotificationsClient notificationsClient;
    private final ReactionMapper reactionsMapper = Mappers.getMapper(ReactionMapper.class);

    public ReactionSaveDTO save(Reaction reaction) {
        if (!checkEntityExists(reaction)) {
            throw new NotFoundException(reaction.getEntityType() + " with id - " + reaction.getEntityId() + " doesn't exist");
        }
        if (!notificationNotRequired(reaction)) {
            notificationsClient.send(notificationService.create(reaction));
        }
        return reactionsMapper.toSaveDTO(repository.save(reaction));
    }

    public Page<ReactionReturnDTO> findReactions(int entityId,
                                                 EntityType entityType,
                                                 boolean byUser,
                                                 Pageable pageable) {
        if (byUser) {
             return findByUser(entityId, entityType, pageable);
        } else {
            return findByEntity(entityId, entityType, pageable);
        }
    }

    @Transactional
    public ReactionDeleteDTO deleteReaction(int entityId, EntityType entityType, int userId) {
        return new ReactionDeleteDTO(repository.deleteByEntityIdAndEntityTypeAndUserId(entityId, entityType, userId));
    }

    private boolean checkEntityExists(Reaction reaction) {
        return switch (reaction.getEntityType().name()) {
            case "comment" -> commentRepository.existsById(reaction.getEntityId());
            case "post" -> true; //TODO
            default -> false;
        };
    }
    public Page<ReactionReturnDTO> findByEntity(int entityId, EntityType entityType, Pageable pageable) {
        Page<Reaction> reactions = repository.findAllByEntityIdAndEntityType(entityId, entityType, pageable);
        if (reactions.isEmpty()) {
            throw new NotFoundException("Reactions not found");
        }
        return reactionsMapper.toReturnDTO(reactions);
    }
    public Page<ReactionReturnDTO> findByUser(int userId, EntityType entityType, Pageable pageable) {
        Page<Reaction> reactions = repository.findAllByUserIdAndEntityType(userId, entityType, pageable);
        if (reactions.isEmpty()) {
            throw new NotFoundException("Reaction not found");
        }
        return reactionsMapper.toReturnDTO(reactions);
    }
    public boolean notificationNotRequired(Reaction reaction) {
        return repository.existsByEntityIdAndEntityTypeAndUserId(
                reaction.getEntityId(),
                reaction.getEntityType(),
                reaction.getUserId());
    }
}
