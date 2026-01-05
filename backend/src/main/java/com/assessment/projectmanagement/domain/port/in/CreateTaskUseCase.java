package com.assessment.projectmanagement.domain.port.in;

import com.assessment.projectmanagement.domain.model.Task;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Input port (use case) for creating tasks
 */
public interface CreateTaskUseCase {
    Task createTask(UUID projectId, String title, String description, LocalDateTime dueDate, UUID requestingUserId);
}
