package com.portfolio.notifications.listener;

import com.portfolio.notifications.event.OrderCreatedEvent;
import com.portfolio.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-events", groupId = "notifications-service")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());
        notificationService.processOrderEvent(event);
    }
}
