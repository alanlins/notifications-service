package com.portfolio.notifications.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class EnvController {

    @Value("${orders_service_url}")
    private String ordersServiceUrl;

    @Value("${notifications_ws_url}")
    private String notificationsWsUrl;

    @GetMapping("/api/env")
    public Map<String, String> getEnv() {
        return Map.of(
                "ORDERS_SERVICE_URL", ordersServiceUrl,
                "NOTIFICATIONS_WS_URL", notificationsWsUrl);
    }
}