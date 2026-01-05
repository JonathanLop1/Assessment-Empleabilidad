package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.CreateTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application service implementing CreateTaskUseCase
 */
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;

    public CreateTaskService(TaskRepositoryPort taskRepository, ProjectRepositoryPort projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Task createTask(UUID projectId, String title, String description, LocalDateTime dueDate,
            UUID requestingUserId) {
        // Verify project exists and user is the owner
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessRuleException("Project not found"));

        project.verifyOwnership(requestingUserId);

        if (project.isDeleted()) {
            throw new BusinessRuleException("Cannot create task for deleted project");
        }

        Task task = new Task(
                UUID.randomUUID(),
                projectId,
                title,
                description,
                dueDate,
                false,
                false);

        return taskRepository.save(task);
    }
}
