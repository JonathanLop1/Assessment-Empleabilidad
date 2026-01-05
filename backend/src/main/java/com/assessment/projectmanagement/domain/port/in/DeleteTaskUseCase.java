package com.assessment.projectmanagement.domain.port.in;

import java.util.UUID;

/**
 * Input port (use case) for deleting tasks
 */
public interface DeleteTaskUseCase {
    void deleteTask(UUID taskId, UUID requestingUserId);
}
