package com.assessment.projectmanagement.domain.model;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Project aggregate root with business logic
 * Pure Java class with no framework dependencies
 */
public class Project {
    private final UUID id;
    private final UUID ownerId;
    private final String name;
    private ProjectStatus status;
    private boolean deleted;
    private final List<Task> tasks;

    public Project(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
        this.tasks = new ArrayList<>();
    }

    /**
     * Business rule: A project can only be activated if it has at least one active
     * task
     */
    public void activate(UUID requestingUserId) {
        verifyOwnership(requestingUserId);

        if (deleted) {
            throw new BusinessRuleException("Cannot activate a deleted project");
        }

        if (status == ProjectStatus.ACTIVE) {
            throw new BusinessRuleException("Project is already active");
        }

        long activeTasks = tasks.stream()
                .filter(task -> !task.isDeleted())
                .count();

        if (activeTasks == 0) {
            throw new BusinessRuleException("Cannot activate project without at least one active task");
        }

        this.status = ProjectStatus.ACTIVE;
    }

    /**
     * Business rule: Only the owner can modify a project
     */
    public void verifyOwnership(UUID requestingUserId) {
        if (!this.ownerId.equals(requestingUserId)) {
            throw new BusinessRuleException("User is not the owner of this project");
        }
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    public void softDelete(UUID requestingUserId) {
        verifyOwnership(requestingUserId);
        this.deleted = true;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
