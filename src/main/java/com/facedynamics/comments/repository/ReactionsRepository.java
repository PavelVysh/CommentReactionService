package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionsRepository extends JpaRepository<Reaction, Integer> {
    List<Reaction> findAllByEntityIdAndLike(int entityId, boolean type);
    List<Reaction> findAllByUserIdAndEntityTypeAndLike(int userId, EntityType entityType, boolean isLike);
    int countAllByEntityIdAndEntityTypeAndLike(int entityId, EntityType type, boolean isLike);
}
