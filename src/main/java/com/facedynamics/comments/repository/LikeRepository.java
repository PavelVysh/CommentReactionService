package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Like;
import com.facedynamics.comments.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findLikesByEntityId(int entityId);
    List<Like> findLikesByUserIdAndEntityType(int userId, EntityType entityType);
    int countLikesByEntityIdAndEntityType(int entityId, EntityType entityType);
}
