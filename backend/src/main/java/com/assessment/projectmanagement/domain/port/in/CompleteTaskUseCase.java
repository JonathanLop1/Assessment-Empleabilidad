package com.assessment.projectmanagement.domain.port.in;

import com.assessment.projectmanagement.domain.model.Task;

import java.util.UUID;

/**
 * Input port (use case) for completing tasks
 */
public interface CompleteTaskUseCase {
    Task completeTask(UUID taskId, UUID requestingUserId);
}
