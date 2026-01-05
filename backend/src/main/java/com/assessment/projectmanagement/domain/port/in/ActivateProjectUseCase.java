package com.assessment.projectmanagement.domain.port.in;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.UUID;

/**
 * Input port (use case) for activating projects
 */
public interface ActivateProjectUseCase {
    Project activateProject(UUID projectId, UUID requestingUserId);
}
