package com.assessment.projectmanagement.presentation.dto;

import com.assessment.projectmanagement.domain.model.Task;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private UUID projectId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private boolean completed;
    private boolean deleted;

    // Constructors
    public TaskResponse() {
    }

    public TaskResponse(UUID id, UUID projectId, String title, String description, LocalDateTime dueDate,
            boolean completed, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.deleted = deleted;
        this.status = completed ? "DONE" : "TODO";
    }

    // Factory method
    public static TaskResponse fromDomain(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted(),
                task.isDeleted());
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.status = completed ? "DONE" : "TODO";
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
