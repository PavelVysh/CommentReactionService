package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findLikesByEntityId(int entityId);
    List<Like> findLikesByUserId(int userId);
}
