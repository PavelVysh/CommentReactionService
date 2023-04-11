package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c where c.postId=:postId and c.parentId is null")
    Page<Comment> findCommentsByPostId(int postId, Pageable pageable);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId=:postId")
    int deleteByPostId(int postId);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id=:id")
    int deleteById(int id);
}
