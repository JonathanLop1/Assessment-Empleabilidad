package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for task persistence
 */
public interface TaskRepositoryPort {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findByProjectIdAndDeletedFalse(UUID projectId);

    long countByProjectIdAndDeletedFalse(UUID projectId);
}
