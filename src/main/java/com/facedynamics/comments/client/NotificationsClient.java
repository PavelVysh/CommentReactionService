package com.facedynamics.comments.client;

import com.facedynamics.comments.dto.notification.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("notifications-service")
public interface NotificationsClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/notifications")
    void send(NotificationDTO notificationDTO);
}
