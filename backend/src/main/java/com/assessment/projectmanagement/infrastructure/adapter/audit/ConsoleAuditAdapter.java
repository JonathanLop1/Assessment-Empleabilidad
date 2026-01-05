package com.assessment.projectmanagement.infrastructure.adapter.audit;

import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simple audit adapter that logs to console
 * In production, this would write to a database or external audit system
 */
@Component
public class ConsoleAuditAdapter implements AuditLogPort {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleAuditAdapter.class);

    @Override
    public void register(String action, UUID entityId) {
        String auditEntry = String.format("[AUDIT] %s | Action: %s | Entity ID: %s",
                LocalDateTime.now(), action, entityId);
        logger.info(auditEntry);
        System.out.println(auditEntry);
    }
}
