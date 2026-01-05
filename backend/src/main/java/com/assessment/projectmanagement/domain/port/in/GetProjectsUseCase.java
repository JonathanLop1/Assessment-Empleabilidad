package com.assessment.projectmanagement.domain.port.in;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Input port (use case) for getting projects
 */
public interface GetProjectsUseCase {
    List<Project> getProjectsByOwner(UUID ownerId);
}
