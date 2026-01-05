package com.assessment.projectmanagement.infrastructure.config;

import com.assessment.projectmanagement.application.usecase.*;
import com.assessment.projectmanagement.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for use case beans
 * Wires up use cases with their dependencies
 */
@Configuration
public class UseCaseConfig {

    @Bean
    public CreateProjectService createProjectService(ProjectRepositoryPort projectRepository) {
        return new CreateProjectService(projectRepository);
    }

    @Bean
    public ActivateProjectService activateProjectService(
            ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        return new ActivateProjectService(projectRepository, taskRepository, auditLog, notification);
    }

    @Bean
    public CreateTaskService createTaskService(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository) {
        return new CreateTaskService(taskRepository, projectRepository);
    }

    @Bean
    public CompleteTaskService completeTaskService(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        return new CompleteTaskService(taskRepository, projectRepository, auditLog, notification);
    }

    @Bean
    public GetProjectsService getProjectsService(
            ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository) {
        return new GetProjectsService(projectRepository, taskRepository);
    }

    @Bean
    public DeleteTaskService deleteTaskService(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository) {
        return new DeleteTaskService(taskRepository, projectRepository);
    }
}
