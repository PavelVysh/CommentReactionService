package com.facedynamics.comments.repository;

import com.facedynamics.comments.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findCommentsByPostId(int postId, Pageable pageable);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId=:postId")
    int deleteByPostId(int postId);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id=:id")
    int deleteById(int id);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.parentId=:parentId")
    int deleteByParentId(int parentId);
}
