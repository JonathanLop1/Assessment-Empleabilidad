import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProjectService, Project, Task } from '../../core/services/project';
import { TaskService } from '../../core/services/task';

@Component({
  selector: 'app-project-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './project-details.html',
  styleUrl: './project-details.css'
})
export class ProjectDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);
  private fb = inject(FormBuilder);

  project = signal<Project | undefined>(undefined);
  isLoading = signal(false);
  showCreateTaskModal = signal(false);

  createTaskForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    dueDate: ['', Validators.required]
  });

  ngOnInit() {
    this.loadProject();
  }

  loadProject() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isLoading.set(true);
      this.projectService.getProjectById(id).subscribe({
        next: (data) => {
          this.project.set(data);
          this.isLoading.set(false);
        },
        error: () => {
          this.isLoading.set(false);
        }
      });
    }
  }

  createTask() {
    const projectId = this.project()?.id;
    if (projectId && this.createTaskForm.valid) {
      const formValue = this.createTaskForm.getRawValue();
      // Ensure dueDate is in ISO format or compatible
      // The backend expects LocalDateTime, so ISO string "yyyy-MM-ddTHH:mm:ss" works
      // The input type="datetime-local" returns "yyyy-MM-ddTHH:mm"
      const dueDate = formValue.dueDate ? new Date(formValue.dueDate).toISOString().slice(0, 19) : '';

      const taskRequest = {
        title: formValue.title!,
        description: formValue.description!,
        dueDate: dueDate
      };

      this.taskService.createTask(projectId, taskRequest).subscribe({
        next: () => {
          this.loadProject(); // Reload to see new task
          this.showCreateTaskModal.set(false);
          this.createTaskForm.reset();
        }
      });
    }
  }

  completeTask(taskId: string) {
    this.taskService.completeTask(taskId).subscribe({
      next: () => {
        this.loadProject();
      }
    });
  }

  showDeleteModal = signal(false);
  taskToDelete = signal<string | null>(null);

  deleteTask(taskId: string) {
    this.taskToDelete.set(taskId);
    this.showDeleteModal.set(true);
  }

  confirmDelete() {
    const taskId = this.taskToDelete();
    if (taskId) {
      this.taskService.deleteTask(taskId).subscribe({
        next: () => {
          this.loadProject();
          this.closeDeleteModal();
        }
      });
    }
  }

  closeDeleteModal() {
    this.showDeleteModal.set(false);
    this.taskToDelete.set(null);
  }

  activateProject() {
    const projectId = this.project()?.id;
    if (projectId) {
      this.projectService.activateProject(projectId).subscribe({
        next: () => {
          this.loadProject();
        }
      });
    }
  }

  toggleModal() {
    this.showCreateTaskModal.update(v => !v);
  }
}
