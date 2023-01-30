package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Dislike;
import com.facedynamics.comments.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DislikeRepository extends JpaRepository<Dislike, Integer> {
    List<Dislike> findDislikeByEntityId(int entityId);
    List<Dislike> findDislikeByUserIdAndEntityType(int userId, EntityType entityType);
}
