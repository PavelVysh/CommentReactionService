package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentsByPostId(int postId);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId=:postId")
    int deleteByPostId(int postId);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id=:id")
    int deleteById(int id);
}
