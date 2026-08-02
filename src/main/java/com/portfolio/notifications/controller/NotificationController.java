package com.portfolio.notifications.controller;

import com.portfolio.notifications.model.Notification;
import com.portfolio.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getRecentNotifications() {
        List<Notification> notifications = notificationService.getRecentNotifications();
        return ResponseEntity.ok(notifications);
    }
}
