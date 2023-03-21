package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionsRepository extends JpaRepository<Reaction, Integer> {
    Page<Reaction> findAllByEntityIdAndEntityType(int entityId, EntityType entityType, Pageable pageable);

    Page<Reaction> findAllByUserIdAndEntityType(int userId, EntityType entityType, Pageable pageable);

    boolean existsByEntityIdAndEntityTypeAndUserId(int entityId, EntityType entityType, int userId);

    int deleteByEntityIdAndEntityTypeAndUserId(int entityId, EntityType entityType, int userId);
    int deleteByEntityIdAndEntityType(int entityId, EntityType entityType);
}
