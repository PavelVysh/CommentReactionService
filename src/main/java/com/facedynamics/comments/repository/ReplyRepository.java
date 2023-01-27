package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
