package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
}
