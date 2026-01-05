package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for project persistence
 */
public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<Project> findById(UUID id);

    List<Project> findByOwnerIdAndDeletedFalse(UUID ownerId);
}
