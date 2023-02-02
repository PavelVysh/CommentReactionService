package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionsRepository extends JpaRepository<Reaction, Integer> {
    List<Reaction> findAllByEntityIdAndLike(int entityId, boolean type);
    List<Reaction> findAllByUserIdAndLike(int entityId, boolean type);
}
