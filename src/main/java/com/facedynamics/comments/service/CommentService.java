package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentDTO save(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return DTOMapper.fromCommentToCommentDTO(savedComment);
    }

    public Comment findById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Comment with id - " + id + " was not found");
        });
    }

    public String deleteById(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->{
            throw new NotFoundException("Comment with id - " + id + " was not found");
        });
            commentRepository.deleteById(id);
            return comment.getText();
    }

    public List<Comment> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        if (comments.size() < 1) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }
        return comments;
    }
}
