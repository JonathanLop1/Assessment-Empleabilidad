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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CompleteTaskService
 * Tests focus on business logic without Spring context
 */
@ExtendWith(MockitoExtension.class)
class CompleteTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private AuditLogPort auditLog;

    @Mock
    private NotificationPort notification;

    private CompleteTaskService completeTaskService;

    @BeforeEach
    void setUp() {
        completeTaskService = new CompleteTaskService(
                taskRepository,
                projectRepository,
                auditLog,
                notification);
    }

    @Test
    void CompleteTask_AlreadyCompleted_ShouldFail() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Task task = new Task(taskId, projectId, "Test Task", "Description", null, true, false); // Already completed
        Project project = new Project(projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> completeTaskService.completeTask(taskId, ownerId));

        assertTrue(exception.getMessage().contains("already completed"));
        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLog, never()).register(any(), any());
        verify(notification, never()).notify(any());
    }

    @Test
    void CompleteTask_ShouldGenerateAuditAndNotification() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Task task = new Task(taskId, projectId, "Test Task", "Description", null, false, false); // Not completed
        Project project = new Project(projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = completeTaskService.completeTask(taskId, ownerId);

        // Assert
        assertNotNull(result);

        // Verify audit log was called with correct parameters
        verify(auditLog).register(eq("TASK_COMPLETED"), eq(taskId));

        // Verify notification was called with a message containing the task title
        verify(notification).notify(contains("Test Task"));
        verify(notification).notify(contains("completed"));

        // Verify task was saved
        verify(taskRepository).save(any(Task.class));
    }
}
