package com.facedynamics.comments.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CommentReturnDTO> findById(int id, boolean post, int page, int size) {
        List<CommentReturnDTO> comments;
        if (post) {
            comments = findCommentsByPostId(id, PageRequest.of(page, size));
        } else {
            comments = List.of(findByCommentId(id));
        }
        return comments;
    }

    @Transactional
    public int deleteById(int id, EntityType entityType) {
        int deleted;
        switch (entityType) {
            case comment -> deleted = commentRepository.deleteById(id);
            case post -> {
                deleted = commentRepository.deleteByPostId(id);
                reactionsRepository.deleteByEntityIdAndEntityType(id, entityType);
            }
            default -> deleted = 0;
        }
        return deleted;
    }

    public List<CommentReturnDTO> findCommentsByPostId(int postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findCommentsByPostId(postId, pageable);
        if (comments.isEmpty()) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }

        List<CommentReturnDTO> commentDTOs = mapper.commentToReturnDTO(comments);
        for (CommentReturnDTO comment : commentDTOs) {
            comment.setPageSize(pageable.getPageSize());
            comment.setTotalPages(comments.getTotalPages());
            comment.setCurrentPage(pageable.getPageNumber());
        }
        return commentDTOs;
    }
    public CommentReturnDTO findByCommentId(int id) {
        return mapper.commentToReturnDTO(commentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Comment with id - " + id + " was not found");
        }));
    }
}
