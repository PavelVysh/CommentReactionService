package com.facedynamics.comments.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private int ownerId;
    private final String notificationType;
    private NotificationDetails details;

    public NotificationDTO(int ownerId, String notificationType) {
        this.ownerId = ownerId;
        this.notificationType = notificationType;
    }
}