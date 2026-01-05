package com.assessment.projectmanagement.presentation.rest;

import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.CompleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.in.CreateTaskUseCase;
import com.assessment.projectmanagement.domain.port.in.DeleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.presentation.dto.CreateTaskRequest;
import com.assessment.projectmanagement.presentation.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for task management endpoints
 */
@RestController
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final CurrentUserPort currentUserPort;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            CompleteTaskUseCase completeTaskUseCase,
            DeleteTaskUseCase deleteTaskUseCase,
            CurrentUserPort currentUserPort) {
        this.createTaskUseCase = createTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.currentUserPort = currentUserPort;
    }

    @PostMapping("/api/projects/{projectId}/tasks")
    @Operation(summary = "Create a new task", description = "Creates a new task in a project")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        Task task = createTaskUseCase.createTask(
                projectId,
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromDomain(task));
    }

    @PatchMapping("/api/tasks/{id}/complete")
    @Operation(summary = "Complete a task", description = "Marks a task as completed")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable UUID id) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        Task task = completeTaskUseCase.completeTask(id, currentUserId);
        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }

    @DeleteMapping("/api/tasks/{id}")
    @Operation(summary = "Delete a task", description = "Soft deletes a task")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        deleteTaskUseCase.deleteTask(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
