package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.CompleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;

import java.util.UUID;

/**
 * Application service implementing CompleteTaskUseCase
 * Orchestrates business logic and cross-cutting concerns (audit, notification)
 */
public class CompleteTaskService implements CompleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final AuditLogPort auditLog;
    private final NotificationPort notification;

    public CompleteTaskService(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.auditLog = auditLog;
        this.notification = notification;
    }

    @Override
    public Task completeTask(UUID taskId, UUID requestingUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessRuleException("Task not found"));

        // Verify ownership through project
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new BusinessRuleException("Project not found"));

        project.verifyOwnership(requestingUserId);

        // Business logic in domain model
        task.complete();

        // Save changes
        Task savedTask = taskRepository.save(task);

        // Cross-cutting concerns
        auditLog.register("TASK_COMPLETED", taskId);
        notification.notify("Task '" + task.getTitle() + "' has been completed");

        return savedTask;
    }
}
