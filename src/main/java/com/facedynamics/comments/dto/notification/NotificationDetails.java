package com.facedynamics.comments.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDetails {
    private int userId;
    private String postText;
    private String commentText;
    private String replyText;
    private LocalDateTime createdAt;
}