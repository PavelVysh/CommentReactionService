package com.facedynamics.comments.dto.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NotificationDTO {
    private final int recipientId;
    private final int createdById;
    private NotificationDetails content;

}