package com.portfolio.notifications.listener;

import com.portfolio.notifications.event.OrderCreatedEvent;
import com.portfolio.notifications.model.Notification;
import com.portfolio.notifications.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Test
    void testHandleOrderCreatedEvent() {
        // Arrange
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId("order-789")
                .customerName("Charlie Brown")
                .item("Deluxe Gadget")
                .quantity(2)
                .createdAt(Instant.now().toString())
                .build();

        Notification notification = Notification.builder()
                .id("notif-789")
                .orderId("order-789")
                .message("Order for 2x Deluxe Gadget confirmed for Charlie Brown")
                .createdAt(Instant.now())
                .build();

        when(notificationService.processOrderEvent(any(OrderCreatedEvent.class)))
                .thenReturn(notification);

        // Act
        orderEventListener.handleOrderCreatedEvent(event);

        // Assert
        verify(notificationService).processOrderEvent(event);
    }

    @Test
    void testHandleOrderCreatedEvent_CallsServiceCorrectly() {
        // Arrange
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId("order-999")
                .customerName("Diana Prince")
                .item("Royal Widget")
                .quantity(5)
                .createdAt(Instant.now().toString())
                .build();

        when(notificationService.processOrderEvent(event))
                .thenReturn(Notification.builder().id("notif-999").build());

        // Act
        orderEventListener.handleOrderCreatedEvent(event);

        // Assert - verify processOrderEvent was called exactly once with the correct event
        verify(notificationService, times(1)).processOrderEvent(event);
    }
}
