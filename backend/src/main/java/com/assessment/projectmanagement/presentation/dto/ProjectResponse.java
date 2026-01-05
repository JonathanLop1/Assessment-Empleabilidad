package com.assessment.projectmanagement.presentation.dto;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.ProjectStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProjectResponse {

    private UUID id;
    private UUID ownerId;
    private String name;
    private ProjectStatus status;
    private boolean deleted;
    private List<TaskResponse> tasks;

    // Constructors
    public ProjectResponse() {
    }

    public ProjectResponse(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted,
            List<TaskResponse> tasks) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
        this.tasks = tasks;
    }

    // Factory method
    public static ProjectResponse fromDomain(Project project) {
        List<TaskResponse> taskResponses = project.getTasks().stream()
                .map(TaskResponse::fromDomain)
                .collect(Collectors.toList());

        return new ProjectResponse(
                project.getId(),
                project.getOwnerId(),
                project.getName(),
                project.getStatus(),
                project.isDeleted(),
                taskResponses);
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<TaskResponse> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskResponse> tasks) {
        this.tasks = tasks;
    }
}
