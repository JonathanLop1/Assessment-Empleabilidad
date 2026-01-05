package com.assessment.projectmanagement.domain.port.out;

/**
 * Output port for notifications
 */
public interface NotificationPort {
    void notify(String message);
}
