package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.repository.CommentRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }
    public Comment findById(int id) {
        return commentRepository.findById(id).orElse(null);
    }
    public void deleteCommentById(int id) {
        commentRepository.deleteById(id);
    }
    public List<Comment> findAllForPost(int postId) {
        return commentRepository.findCommentsByPostId(postId);
    }
}
