package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.notification.NotificationDTO;
import com.facedynamics.comments.dto.notification.NotificationDetails;
import com.facedynamics.comments.dto.post.PostDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reaction;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.PostsClient;
import com.facedynamics.comments.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final CommentRepository commentRepository;
    private final PostsClient postsClient;
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    public NotificationDTO create(Comment comment, PostDTO postDTO) {
        NotificationDTO notification;
        if (comment.getParentId() == null) {
            notification = createCommentNotification(comment, postDTO);
        } else {
            notification = createReplyNotification(comment);
        }
        return notification;
    }

    public NotificationDTO create(Reaction reaction) {
        return createReactionNotification(reaction);
    }
    public void sendToKafka(NotificationDTO notificationDTO) {
        kafkaTemplate.send("notifications", notificationDTO);
    }

    private NotificationDTO createReactionNotification(Reaction reaction) {
        NotificationDTO notificationDTO = new NotificationDTO(0, reaction.getUserId());
        notificationDTO.setContent(NotificationDetails.builder()
                .type(getType(reaction))
                .isLike(reaction.isLike())
                .entityCreatedAt(LocalDateTime.now())
                .build());
        switch (notificationDTO.getContent().getType().split("_")[0]) {
            case "COMMENT" -> setNotificationDetailsComment(reaction, notificationDTO);
            case "POST" -> setNotificationDetailsPost(reaction, notificationDTO);
        }
        return notificationDTO;
    }

    private void setNotificationDetailsPost(Reaction reaction, NotificationDTO notificationDTO) {
        PostDTO postDTO = postsClient.getPostById(reaction.getEntityId());
        notificationDTO.getContent().setPostId(reaction.getEntityId());
        notificationDTO.getContent().setPostText(postDTO.getText());
        notificationDTO.setRecipientId(postDTO.getUserId());
    }

    private void setNotificationDetailsComment(Reaction reaction, NotificationDTO notificationDTO) {
        Comment comment = commentRepository.findById(reaction.getEntityId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        notificationDTO.getContent().setCommentId(reaction.getEntityId());
        notificationDTO.getContent().setCommentText(commentRepository.findById(reaction.getEntityId()).get().getText());
        notificationDTO.setRecipientId(comment.getUserId());
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
