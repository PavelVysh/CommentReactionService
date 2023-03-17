package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionsRepository reactionsRepository;
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
            return mapper.commentToReturnDTO(commentRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("")));
    }

    @Transactional
    public DeleteDTO deleteByCommentId(int id) {
        return new DeleteDTO(commentRepository.deleteById(id));
    }

    @Transactional
    public DeleteDTO deleteByPostId(int postId) {
        deleteReactionsForPost(postId);
        return new DeleteDTO(commentRepository.deleteByPostId(postId));
    }

    public Page<CommentReturnDTO> findCommentsByPostId(int postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findCommentsByPostId(postId, pageable);
        if (comments.isEmpty()) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }
        return mapper.commentToReturnDTO(comments);
    }

    private int deleteReactionsForPost(int postId) {
        return reactionsRepository.deleteByEntityIdAndEntityType(postId, EntityType.post);
    }
}
