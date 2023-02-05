package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentsByPostId(int postId);

    void deleteById(int id);
}
