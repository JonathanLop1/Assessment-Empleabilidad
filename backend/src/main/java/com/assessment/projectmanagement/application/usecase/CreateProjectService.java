package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.ProjectStatus;
import com.assessment.projectmanagement.domain.port.in.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;

import java.util.UUID;

/**
 * Application service implementing CreateProjectUseCase
 * Contains no business logic - delegates to domain model
 */
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;

    public CreateProjectService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProject(String name, UUID ownerId) {
        Project project = new Project(
                UUID.randomUUID(),
                ownerId,
                name,
                ProjectStatus.DRAFT,
                false);

        return projectRepository.save(project);
    }
}
