package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.GetProjectsUseCase;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;

import java.util.List;
import java.util.UUID;

/**
 * Application service implementing GetProjectsUseCase
 */
public class GetProjectsService implements GetProjectsUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final TaskRepositoryPort taskRepository;

    public GetProjectsService(ProjectRepositoryPort projectRepository, TaskRepositoryPort taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Project> getProjectsByOwner(UUID ownerId) {
        List<Project> projects = projectRepository.findByOwnerIdAndDeletedFalse(ownerId);

        // Load tasks for each project
        for (Project project : projects) {
            List<Task> tasks = taskRepository.findByProjectIdAndDeletedFalse(project.getId());
            project.setTasks(tasks);
        }

        return projects;
    }
}
