package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
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
    private Mapper mapper;

    public CommentSaveDTO save(Comment comment) {
        if (comment.getParentId() != null) {
            commentRepository.findById(comment.getParentId()).orElseThrow(() -> {
                throw new NotFoundException("Comment with id - " + comment.getParentId() + " was not found");
            });
        }
        Comment savedComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(savedComment);
    }

    public CommentReturnDTO findById(int id) {
        return mapper.commentToReturnDTO(commentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Comment with id - " + id + " was not found");
        }));
    }

    public String deleteById(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->{
            throw new NotFoundException("Comment with id - " + id + " was not found");
        });
            commentRepository.deleteById(id);
            return comment.getText();
    }

    public List<CommentReturnDTO> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        if (comments.size() < 1) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }
        return mapper.commentToReturnDTO(comments);
    }
}
