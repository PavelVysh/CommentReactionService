package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.Mapper;
import com.facedynamics.comments.dto.comment.CommentDeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.notification.NotificationCreateDTO;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.FeignClientMockImpl;
import com.facedynamics.comments.feign.FeignClientRealImpl;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionsRepository reactionsRepository;
    private final FeignClientMockImpl feignMock;
    private final FeignClientRealImpl feignReal;
    private Mapper mapper;

    public CommentSaveDTO save(Comment comment) {
        PostDTO postDTO = feignMock.getPostById(comment.getPostId());
        checkIfParentExists(comment, postDTO);
        Comment savedComment = commentRepository.save(comment);
        sendCommentCreatedNotification(comment, postDTO);
        return mapper.commentToCommentDTO(savedComment);
    }

    public CommentReturnDTO findById(int id) {
        return mapper.commentToReturnDTO(commentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Comment with id - " + id + " was not found");
        }));
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

    public List<CommentReturnDTO> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        if (comments.size() < 1) {
            throw new NotFoundException("Comments for post with id "
                    + postId + " were not found");
        }
        return mapper.commentToReturnDTO(comments);
    }
    private int deleteReactionsForPost(int postId) {
        return reactionsRepository.deleteByEntityIdAndEntityType(postId, EntityType.post);
    }
    private void sendCommentCreatedNotification(Comment comment, PostDTO postDTO) {
        NotificationCreateDTO notification = new NotificationCreateDTO(postDTO.getUserId(), "comment");
        notification.createDetails(comment.getUserId(), postDTO.getText(), comment.getText(), comment.getCreatedAt());
        feignReal.createNotification(notification);
    }
    private void checkIfParentExists(Comment comment, PostDTO postDTO) {
        if (comment.getParentId() != null) {
            commentRepository.findById(comment.getParentId()).orElseThrow(() -> {
                throw new NotFoundException("Comment with id - " + comment.getParentId() + " was not found");
            });
        } else if (postDTO.getUserId() == 0) {
            throw new NotFoundException("Post with id - " + comment.getPostId() + " was not found");
        }
    }
}
