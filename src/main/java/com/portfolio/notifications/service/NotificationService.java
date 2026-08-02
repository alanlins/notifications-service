package com.portfolio.notifications.service;

import com.portfolio.notifications.event.OrderCreatedEvent;
import com.portfolio.notifications.model.Notification;
import com.portfolio.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Notification processOrderEvent(OrderCreatedEvent event) {
        // Build notification message
        String message = String.format(
                "Order for %dx %s confirmed for %s",
                event.getQuantity(),
                event.getItem(),
                event.getCustomerName()
        );

        // Create and save notification
        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .orderId(event.getOrderId())
                .message(message)
                .createdAt(Instant.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // Broadcast to WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/notifications", savedNotification);

        return savedNotification;
    }

    public List<Notification> getRecentNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }
}
