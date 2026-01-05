package com.assessment.projectmanagement.domain.port.out;

import java.util.UUID;

/**
 * Output port for audit logging
 */
public interface AuditLogPort {
    void register(String action, UUID entityId);
}
