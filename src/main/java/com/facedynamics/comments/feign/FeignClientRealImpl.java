package com.facedynamics.comments.feign;

import com.facedynamics.comments.dto.notification.NotificationCreateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "feign", url = "${feign.notification.url}")
public interface FeignClientRealImpl {
    @RequestMapping(method = RequestMethod.POST, value = "/notifications")
    void createNotification(NotificationCreateDTO notificationCreateDTO);
}
