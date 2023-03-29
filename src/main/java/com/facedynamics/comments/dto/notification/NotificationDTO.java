package com.facedynamics.comments.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private int recipientId;
    private int createdById;
    private NotificationDetails content;

    public NotificationDTO(int recipientId, int createdById) {
        this.recipientId = recipientId;
        this.createdById = createdById;
    }
}