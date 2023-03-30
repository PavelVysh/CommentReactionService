package com.facedynamics.comments.events;

import com.facedynamics.comments.dto.notification.NotificationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final NotificationDTO notificationDTO;
    public NotificationEvent(Object source, NotificationDTO notificationDTO) {
        super(source);
        this.notificationDTO = notificationDTO;
    }
}
