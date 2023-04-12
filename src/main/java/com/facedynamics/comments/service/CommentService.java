package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.CommentMapper;
import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.events.NotificationEvent;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.PostsClient;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionsRepository reactionsRepository;
    private final PostsClient postsClient;
    private ApplicationEventPublisher eventPublisher;
    private CommentMapper commentMapper;
    private NotificationService notification;

    public CommentSaveDTO save(Comment comment) {
        PostDTO postDTO;
        try {
            postDTO = postsClient.getPostById(comment.getPostId());
        } catch (FeignException exc) {
            throw new NotFoundException("Post with id - " + comment.getPostId() + " was not found");
        }
        checkIfParentExists(comment, postDTO);
        Comment savedComment = commentRepository.save(comment);
        eventPublisher.publishEvent(new NotificationEvent(this, notification.create(savedComment, postDTO)));

        return commentMapper.toSaveDTO(savedComment);
    }

    public CommentReturnDTO findById(int id) {
        return commentRepository.findById(id)
                .map(comment -> commentMapper.toReturnDTO(comment))
                .orElseThrow(() -> new NotFoundException("Comment with id - " + id + " was not found"));

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

        return commentMapper.toReturnDTO(comments);
    }

    private int deleteReactionsForPost(int postId) {
        return reactionsRepository.deleteByEntityIdAndEntityType(postId, EntityType.post);
    }

    private void checkIfParentExists(Comment comment, PostDTO postDTO) {
        if (comment.getParentId() != null) {
            commentRepository.findById(comment.getParentId()).orElseThrow(() ->
                    new NotFoundException("Comment with id - " + comment.getParentId() + " was not found"));
        } else if (postDTO.getUserId() == null) {
            throw new NotFoundException("Post with id - " + comment.getPostId() + " was not found");
        }
    }
}
