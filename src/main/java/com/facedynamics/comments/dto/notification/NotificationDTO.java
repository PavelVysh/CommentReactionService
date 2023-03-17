package com.facedynamics.comments.dto.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NotificationDTO {
    private final int ownerId;
    private final String notificationType;
    private NotificationDetails details;

}