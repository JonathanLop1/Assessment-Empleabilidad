package com.assessment.projectmanagement.infrastructure.adapter.notification;

import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Simple notification adapter that logs to console
 * In production, this would send emails, push notifications, etc.
 */
@Component
public class ConsoleNotificationAdapter implements NotificationPort {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleNotificationAdapter.class);

    @Override
    public void notify(String message) {
        String notification = String.format("[NOTIFICATION] %s | %s",
                LocalDateTime.now(), message);
        logger.info(notification);
        System.out.println(notification);
    }
}
