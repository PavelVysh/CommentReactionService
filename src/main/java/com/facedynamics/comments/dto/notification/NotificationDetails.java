package com.facedynamics.comments.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDetails {
    private String type;
    private int commentId;
    private int replyId;
    private int postId;
    private String postText;
    private String commentText;
    private String replyText;
    private LocalDateTime entityCreatedAt;
}