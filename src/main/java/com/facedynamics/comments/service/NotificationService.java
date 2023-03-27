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

        NotificationDTO notification = new NotificationDTO(comment.getUserId(), reply.getUserId());
        notification.setContent(NotificationDetails.builder()
                .type("COMMENT_REPLIED")
                .replyId(reply.getId())
                .commentId(comment.getId())
                .commentText(comment.getText())
                .replyText(reply.getText())
                .entityCreatedAt(reply.getCreatedAt())
                .build());
        return notification;
    }

    private NotificationDTO createCommentNotification(Comment comment, PostDTO postDTO) {
        NotificationDTO notification = new NotificationDTO(postDTO.getUserId(), comment.getUserId());
        notification.setContent(NotificationDetails.builder()
                .type("POST_COMMENTED")
                .commentId(comment.getId())
                .postId(postDTO.getUserId())
                .postText(postDTO.getText())
                .commentText(comment.getText())
                .entityCreatedAt(comment.getCreatedAt())
                .build());
        return notification;
    }
}
