package com.assessment.projectmanagement.application.usecase;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.ProjectStatus;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivateProjectService
 * Tests focus on business logic without Spring context
 */
@ExtendWith(MockitoExtension.class)
class ActivateProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private AuditLogPort auditLog;

    @Mock
    private NotificationPort notification;

    private ActivateProjectService activateProjectService;

    @BeforeEach
    void setUp() {
        activateProjectService = new ActivateProjectService(
                projectRepository,
                taskRepository,
                auditLog,
                notification);
    }

    @Test
    void ActivateProject_WithTasks_ShouldSucceed() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false);

        Task task = new Task(UUID.randomUUID(), projectId, "Test Task", "Description", null, false, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectIdAndDeletedFalse(projectId)).thenReturn(tasks);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = activateProjectService.activateProject(projectId, ownerId);

        // Assert
        assertNotNull(result);
        verify(projectRepository).save(any(Project.class));
        verify(auditLog).register("PROJECT_ACTIVATED", projectId);
        verify(notification).notify(contains("activated"));
    }

    @Test
    void ActivateProject_WithoutTasks_ShouldFail() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false);

        List<Task> tasks = new ArrayList<>(); // Empty task list

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectIdAndDeletedFalse(projectId)).thenReturn(tasks);

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> activateProjectService.activateProject(projectId, ownerId));

        assertTrue(exception.getMessage().contains("without at least one active task"));
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLog, never()).register(any(), any());
        verify(notification, never()).notify(any());
    }

    @Test
    void ActivateProject_ByNonOwner_ShouldFail() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID(); // Different from owner
        Project project = new Project(projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false);

        Task task = new Task(UUID.randomUUID(), projectId, "Test Task", "Description", null, false, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectIdAndDeletedFalse(projectId)).thenReturn(tasks);

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> activateProjectService.activateProject(projectId, differentUserId));

        assertTrue(exception.getMessage().contains("not the owner"));
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLog, never()).register(any(), any());
        verify(notification, never()).notify(any());
    }
}
