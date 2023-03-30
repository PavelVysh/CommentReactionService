package com.facedynamics.comments.events;

import com.facedynamics.comments.feign.NotificationsClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationEventListener implements ApplicationListener<NotificationEvent> {
    private NotificationsClient notificationsClient;
    private static final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    @Override
    public void onApplicationEvent(NotificationEvent event) {
        try {
            notificationsClient.send(event.getNotificationDTO());
        } catch (FeignException exc) {
            logger.error("Error during sending notification {}", exc.getMessage());
        }
    }
}
