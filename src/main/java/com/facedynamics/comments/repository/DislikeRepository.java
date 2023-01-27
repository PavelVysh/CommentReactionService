package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike, Integer> {
}
