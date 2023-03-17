package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.notification.NotificationDTO;
import com.facedynamics.comments.dto.notification.NotificationDetails;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.NotificationsClient;
import com.facedynamics.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationsClient notificationsClient;
    private final CommentRepository commentRepository;

    public void send(Comment comment, PostDTO postDTO) {
        NotificationDTO notification;
        if (comment.getParentId() == null) {
            notification = createCommentNotification(comment, postDTO);
        } else {
            notification = createReplyNotification(comment);
        }
        notificationsClient.sendNotification(notification);
    }

    private NotificationDTO createReplyNotification(Comment reply) {
        Comment comment = commentRepository.findById(reply.getParentId()).orElseThrow(() ->
                new NotFoundException("Comment with id - " + reply.getParentId() + " not found"));

        NotificationDTO notification = new NotificationDTO(comment.getUserId(), "reply");
        notification.setDetails(NotificationDetails.builder()
                .commentText(comment.getText())
                .replyText(reply.getText())
                .createdAt(reply.getCreatedAt())
                .userId(reply.getUserId())
                .build());
        return notification;
    }

    private NotificationDTO createCommentNotification(Comment comment, PostDTO postDTO) {
        NotificationDTO notification = new NotificationDTO(postDTO.getUserId(), "comment");
        notification.setDetails(NotificationDetails.builder()
                .userId(comment.getUserId())
                .postText(postDTO.getText())
                .commentText(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build());
        return notification;
    }
}
