package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.CommentMapper;
import com.facedynamics.comments.dto.comment.CommentDeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.PostsClient;
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
    private final PostsClient postsClient;
    private CommentMapper commentMapper;
    private NotificationService notification;

    public CommentSaveDTO save(Comment comment) {
        PostDTO postDTO = postsClient.getById(comment.getPostId());
        checkIfParentExists(comment, postDTO);
        Comment savedComment = commentRepository.save(comment);
        notification.send(savedComment, postDTO);
        return commentMapper.toSaveDTO(savedComment);
    }

    public CommentReturnDTO findById(int id) {
            return commentRepository.findById(id)
                    .map(comment -> commentMapper.toReturnDTO(comment))
                    .orElseThrow(() -> new NotFoundException("Comment with id - " + id + " was not found"));

    }

    @Transactional
    public CommentDeleteDTO deleteByCommentId(int id) {
        return new CommentDeleteDTO(commentRepository.deleteById(id));
    }

    @Transactional
    public CommentDeleteDTO deleteByPostId(int postId) {
        deleteReactionsForPost(postId);
        return new CommentDeleteDTO(commentRepository.deleteByPostId(postId));
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
            commentRepository.findById(comment.getParentId()).orElseThrow(() -> {
                throw new NotFoundException("Comment with id - " + comment.getParentId() + " was not found");
            });
        } else if (postDTO.getUserId() == null) {
            throw new NotFoundException("Post with id - " + comment.getPostId() + " was not found");
        }
    }
}
