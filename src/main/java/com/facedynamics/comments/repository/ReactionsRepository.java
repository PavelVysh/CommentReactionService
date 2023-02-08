package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReactionsRepository extends JpaRepository<Reaction, Integer> {
    List<Reaction> findAllByEntityIdAndEntityTypeAndLike(int entityId, EntityType entityType, boolean isLike);

    List<Reaction> findAllByUserIdAndEntityTypeAndLike(int userId, EntityType entityType, boolean isLike);
    Reaction findByEntityIdAndEntityTypeAndUserIdAndLike(int entityId, EntityType entityType, int userId, boolean like);

    boolean existsByEntityIdAndEntityTypeAndUserId(int entityId, EntityType entityType, int userId);

    @Modifying
    @Query("update Reaction r SET r.like = :like WHERE r.entityType = :entityType AND r.entityId = :entityId AND r.userId = :userId")
    int changeReactionToOpposite(int entityId, EntityType entityType, int userId, boolean like);

    int deleteByEntityIdAndEntityTypeAndUserId(int entityId, EntityType entityType, int userId);
}
