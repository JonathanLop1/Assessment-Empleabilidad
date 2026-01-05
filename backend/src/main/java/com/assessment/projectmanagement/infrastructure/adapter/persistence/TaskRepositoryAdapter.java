package com.assessment.projectmanagement.infrastructure.adapter.persistence;

import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import com.assessment.projectmanagement.infrastructure.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements TaskRepositoryPort using JPA
 * Maps between domain models and JPA entities
 */
@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository jpaRepository;

    public TaskRepositoryAdapter(TaskJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = toEntity(task);
        TaskEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Task> findByProjectIdAndDeletedFalse(UUID projectId) {
        return jpaRepository.findByProjectIdAndDeletedFalse(projectId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByProjectIdAndDeletedFalse(UUID projectId) {
        return jpaRepository.countByProjectIdAndDeletedFalse(projectId);
    }

    private TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted(),
                task.isDeleted());
    }

    private Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getProjectId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                entity.isCompleted(),
                entity.isDeleted());
    }
}
