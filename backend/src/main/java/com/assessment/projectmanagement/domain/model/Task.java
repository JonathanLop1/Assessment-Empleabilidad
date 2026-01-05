package com.assessment.projectmanagement.domain.model;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Task entity with business logic
 * Pure Java class with no framework dependencies
 */
public class Task {
    private final UUID id;
    private final UUID projectId;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private boolean completed;
    private boolean deleted;

    public Task(UUID id, UUID projectId, String title, String description, LocalDateTime dueDate, boolean completed,
            boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.deleted = deleted;
    }

    /**
     * Business rule: A completed task cannot be modified
     */
    public void complete() {
        if (deleted) {
            throw new BusinessRuleException("Cannot complete a deleted task");
        }

        if (completed) {
            throw new BusinessRuleException("Task is already completed");
        }

        this.completed = true;
    }

    public void softDelete() {
        this.deleted = true;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
