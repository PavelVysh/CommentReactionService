package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.notification.NotificationDTO;
import com.facedynamics.comments.dto.notification.NotificationDetails;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.NotificationsClient;
import com.facedynamics.comments.feign.PostsClient;
import com.facedynamics.comments.repository.CommentRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationsClient notificationsClient;
    private final CommentRepository commentRepository;
    private final PostsClient postsClient;

    public void send(Comment comment, PostDTO postDTO) {
        NotificationDTO notification;
        if (comment.getParentId() == null) {
            notification = createCommentNotification(comment, postDTO);
        } else {
            notification = createReplyNotification(comment);
        }
        try {
            notificationsClient.send(notification);
        } catch (FeignException exc) {
            logger.error(exc.getMessage());
        }
    }

    public void send(Reaction reaction) {
        NotificationDTO notification = createReactionNotification(reaction);
        try {
            notificationsClient.send(notification);
        } catch (FeignException exc) {
            logger.error(exc.getMessage());
        }
    }

    private NotificationDTO createReactionNotification(Reaction reaction) {
        NotificationDTO notificationDTO = new NotificationDTO(getRecipient(reaction), reaction.getUserId());
        notificationDTO.setContent(NotificationDetails.builder()
                .type(getType(reaction))
                .isLike(reaction.isLike())
                .build());
        switch (notificationDTO.getContent().getType().split("_")[0]) {
            case "COMMENT" -> notificationDTO.getContent().setCommentId(reaction.getEntityId());
            case "POST" -> notificationDTO.getContent().setPostId(reaction.getEntityId());
        }

        return notificationDTO;
    }

    private String getType(Reaction reaction) {
        StringBuilder type = new StringBuilder();
        switch (reaction.getEntityType()) {
            case comment -> type.append("COMMENT_");
            case post -> type.append("POST_");
        }
        type.append(reaction.isLike() ? "LIKED" : "DISLIKED");
        return type.toString();
    }

    private int getRecipient(Reaction reaction) {
        int recipientId = 0;
        switch (reaction.getEntityType()) {
            case comment -> recipientId = commentRepository.findById(reaction.getEntityId()).get().getUserId();
            case post -> recipientId = postsClient.getPostById(reaction.getEntityId()).getUserId();
        }
        return recipientId;
    }

    private NotificationDTO createReplyNotification(Comment reply) {
        Comment comment = commentRepository.findById(reply.getParentId()).orElseThrow(() -> new NotFoundException("Comment with id - " + reply.getParentId() + " not found"));

        NotificationDTO notification = new NotificationDTO(comment.getUserId(), reply.getUserId());
        notification.setContent(NotificationDetails.builder().type("COMMENT_REPLIED").replyId(reply.getId()).commentId(comment.getId()).commentText(comment.getText()).replyText(reply.getText()).entityCreatedAt(reply.getCreatedAt()).build());
        return notification;
    }

    private NotificationDTO createCommentNotification(Comment comment, PostDTO postDTO) {
        NotificationDTO notification = new NotificationDTO(postDTO.getUserId(), comment.getUserId());
        notification.setContent(NotificationDetails.builder().type("POST_COMMENTED").commentId(comment.getId()).postId(postDTO.getUserId()).postText(postDTO.getText()).commentText(comment.getText()).entityCreatedAt(comment.getCreatedAt()).build());
        return notification;
    }
}
