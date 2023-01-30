package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DislikeRepository extends JpaRepository<Dislike, Integer> {
    List<Dislike> findDislikeByEntityId(int entityId);
    List<Dislike> findDislikeByUserId(int userId);
}
