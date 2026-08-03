package com.portfolio.notifications.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class EnvController {

    @Value("${ORDERS_SERVICE_URL : http://localhost:8081}")
    private String ordersServiceUrl;

    @Value("${NOTIFICATIONS_WS_URL : http://localhost:8082/ws}")
    private String notificationsWsUrl;

    @GetMapping("/api/env")
    public Map<String, String> getEnv() {
        return Map.of(
                "ORDERS_SERVICE_URL", ordersServiceUrl,
                "NOTIFICATIONS_WS_URL", notificationsWsUrl);
    }
}