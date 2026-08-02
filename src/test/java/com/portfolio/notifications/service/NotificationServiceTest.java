package com.portfolio.notifications.service;

import com.portfolio.notifications.event.OrderCreatedEvent;
import com.portfolio.notifications.model.Notification;
import com.portfolio.notifications.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository, messagingTemplate);
    }

    @Test
    void testProcessOrderEvent_Success() {
        // Arrange
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId("order-123")
                .customerName("Alice Johnson")
                .item("Premium Widget")
                .quantity(3)
                .createdAt(Instant.now().toString())
                .build();

        Notification savedNotification = Notification.builder()
                .id("notif-123")
                .orderId("order-123")
                .message("Order for 3x Premium Widget confirmed for Alice Johnson")
                .createdAt(Instant.now())
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // Act
        Notification result = notificationService.processOrderEvent(event);

        // Assert
        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertEquals("Order for 3x Premium Widget confirmed for Alice Johnson", result.getMessage());

        // Verify repository save was called
        ArgumentCaptor<Notification> notifCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notifCaptor.capture());

        Notification savedNotif = notifCaptor.getValue();
        assertEquals("order-123", savedNotif.getOrderId());
        assertTrue(savedNotif.getMessage().contains("Alice Johnson"));
        assertTrue(savedNotif.getMessage().contains("Premium Widget"));

        // Verify WebSocket broadcast was called
        verify(messagingTemplate).convertAndSend(
                eq("/topic/notifications"),
                any(Notification.class)
        );
    }

    @Test
    void testProcessOrderEvent_MessageFormat() {
        // Arrange
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId("order-456")
                .customerName("Bob Smith")
                .item("Standard Item")
                .quantity(1)
                .createdAt(Instant.now().toString())
                .build();

        Notification savedNotification = Notification.builder()
                .id("notif-456")
                .orderId("order-456")
                .message("Order for 1x Standard Item confirmed for Bob Smith")
                .createdAt(Instant.now())
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // Act
        Notification result = notificationService.processOrderEvent(event);

        // Assert
        assertTrue(result.getMessage().contains("1x"));
        assertTrue(result.getMessage().contains("Standard Item"));
        assertTrue(result.getMessage().contains("Bob Smith"));
    }

    @Test
    void testGetRecentNotifications() {
        // Arrange
        Notification notif1 = Notification.builder()
                .id("notif-1")
                .orderId("order-1")
                .message("Notification 1")
                .createdAt(Instant.now())
                .build();

        when(notificationRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(java.util.List.of(notif1));

        // Act
        var result = notificationService.getRecentNotifications();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Notification 1", result.get(0).getMessage());
        verify(notificationRepository).findAllByOrderByCreatedAtDesc();
    }
}
