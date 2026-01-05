package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.ActivateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;

import java.util.List;
import java.util.UUID;

/**
 * Application service implementing ActivateProjectUseCase
 * Orchestrates business logic and cross-cutting concerns (audit, notification)
 */
public class ActivateProjectService implements ActivateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final TaskRepositoryPort taskRepository;
    private final AuditLogPort auditLog;
    private final NotificationPort notification;

    public ActivateProjectService(
            ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLog = auditLog;
        this.notification = notification;
    }

    @Override
    public Project activateProject(UUID projectId, UUID requestingUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessRuleException("Project not found"));

        // Load tasks for validation
        List<Task> tasks = taskRepository.findByProjectIdAndDeletedFalse(projectId);
        project.setTasks(tasks);

        // Business logic in domain model
        project.activate(requestingUserId);

        // Save changes
        Project savedProject = projectRepository.save(project);

        // Cross-cutting concerns
        auditLog.register("PROJECT_ACTIVATED", projectId);
        notification.notify("Project '" + project.getName() + "' has been activated");

        return savedProject;
    }
}
