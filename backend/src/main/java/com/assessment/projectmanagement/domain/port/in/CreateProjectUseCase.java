package com.assessment.projectmanagement.domain.port.in;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.UUID;

/**
 * Input port (use case) for creating projects
 */
public interface CreateProjectUseCase {
    Project createProject(String name, UUID ownerId);
}
