package com.assessment.projectmanagement.presentation.rest;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.in.ActivateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.GetProjectsUseCase;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.presentation.dto.CreateProjectRequest;
import com.assessment.projectmanagement.presentation.dto.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for project management endpoints
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Project management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final GetProjectsUseCase getProjectsUseCase;
    private final ActivateProjectUseCase activateProjectUseCase;
    private final CurrentUserPort currentUserPort;

    public ProjectController(
            CreateProjectUseCase createProjectUseCase,
            GetProjectsUseCase getProjectsUseCase,
            ActivateProjectUseCase activateProjectUseCase,
            CurrentUserPort currentUserPort) {
        this.createProjectUseCase = createProjectUseCase;
        this.getProjectsUseCase = getProjectsUseCase;
        this.activateProjectUseCase = activateProjectUseCase;
        this.currentUserPort = currentUserPort;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project in DRAFT status")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        Project project = createProjectUseCase.createProject(request.getName(), currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.fromDomain(project));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects for the authenticated user")
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        List<Project> projects = getProjectsUseCase.getProjectsByOwner(currentUserId);
        List<ProjectResponse> responses = projects.stream()
                .map(ProjectResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a project", description = "Activates a project if it has at least one active task")
    public ResponseEntity<ProjectResponse> activateProject(@PathVariable UUID id) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        Project project = activateProjectUseCase.activateProject(id, currentUserId);
        return ResponseEntity.ok(ProjectResponse.fromDomain(project));
    }
}
