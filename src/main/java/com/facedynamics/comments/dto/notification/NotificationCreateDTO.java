package com.facedynamics.comments.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationCreateDTO {
    public NotificationCreateDTO(int ownerId, String notificationType) {
        this.ownerId = ownerId;
        this.notificationType = notificationType;
    }
    private int ownerId;
    private final String notificationType;
    private Details details;

    public void createDetails(int userId,
                              String postText,
                              String commentText,
                              LocalDateTime createdAt) {
        details = new Details(userId, postText, commentText, createdAt);
    }
}
@Getter
@Setter
@AllArgsConstructor
class Details {
    private int userId;
    private String postText;
    private String commentText;
    private LocalDateTime createdAt;
}