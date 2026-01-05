package com.assessment.projectmanagement.infrastructure.adapter.persistence;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.infrastructure.entity.ProjectEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements ProjectRepositoryPort using JPA
 * Maps between domain models and JPA entities
 */
@Component
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository jpaRepository;

    public ProjectRepositoryAdapter(ProjectJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Project> findByOwnerIdAndDeletedFalse(UUID ownerId) {
        return jpaRepository.findByOwnerIdAndDeletedFalse(ownerId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private ProjectEntity toEntity(Project project) {
        return new ProjectEntity(
                project.getId(),
                project.getOwnerId(),
                project.getName(),
                project.getStatus(),
                project.isDeleted());
    }

    private Project toDomain(ProjectEntity entity) {
        return new Project(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                entity.getStatus(),
                entity.isDeleted());
    }
}
