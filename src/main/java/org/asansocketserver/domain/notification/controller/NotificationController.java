package org.asansocketserver.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification API", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
        private final NotificationService notificationService;

}
