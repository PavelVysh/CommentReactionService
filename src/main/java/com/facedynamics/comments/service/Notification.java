package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.notification.NotificationCreateDTO;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.FeignClientRealImpl;
import com.facedynamics.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Notification {
    private final FeignClientRealImpl feignClient;
    private final CommentRepository commentRepository;

    public void send(Comment comment, PostDTO postDTO) {
        NotificationCreateDTO notification;
        if (comment.getParentId() == null) {
            notification = sendCommentNotification(comment, postDTO);
        } else {
            notification = sendReplyNotification(comment);
        }
        feignClient.createNotification(notification);
    }

    private NotificationCreateDTO sendReplyNotification(Comment comment) {
        Comment parentComment = commentRepository.findById(comment.getParentId()).orElseThrow(() ->
                new NotFoundException("Comment with id - " + comment.getParentId() + " not found"));

        NotificationCreateDTO notification = new NotificationCreateDTO(parentComment.getUserId(), "reply");
        notification.createDetails(comment.getUserId(),null, parentComment.getText(), comment.getText(), comment.getCreatedAt());

        return notification;
    }

    private NotificationCreateDTO sendCommentNotification(Comment comment, PostDTO postDTO) {
        NotificationCreateDTO notification = new NotificationCreateDTO(postDTO.getUserId(), "comment");
        notification.createDetails(comment.getUserId(), postDTO.getText(), comment.getText(), comment.getCreatedAt());

        return notification;
    }
}
