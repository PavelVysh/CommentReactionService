package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findRepliesByCommentId(int commentId);
}
