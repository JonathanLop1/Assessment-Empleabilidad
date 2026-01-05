package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.DeleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;

import java.util.UUID;

/**
 * Application service implementing DeleteTaskUseCase
 */
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;

    public DeleteTaskService(TaskRepositoryPort taskRepository, ProjectRepositoryPort projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void deleteTask(UUID taskId, UUID requestingUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessRuleException("Task not found"));

        // Verify ownership through project
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new BusinessRuleException("Project not found"));

        project.verifyOwnership(requestingUserId);

        task.softDelete();

        taskRepository.save(task);
    }
}
