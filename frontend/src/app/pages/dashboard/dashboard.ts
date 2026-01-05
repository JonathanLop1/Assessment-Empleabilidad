import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProjectService, Project } from '../../core/services/project';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private projectService = inject(ProjectService);
  private fb = inject(FormBuilder);
  public authService = inject(AuthService);

  projects = signal<Project[]>([]);
  isLoading = signal(false);
  showCreateModal = signal(false);

  createProjectForm = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required]
  });

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    this.isLoading.set(true);
    this.projectService.getProjects().subscribe({
      next: (data) => {
        this.projects.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  createProject() {
    if (this.createProjectForm.valid) {
      this.projectService.createProject(this.createProjectForm.getRawValue() as any).subscribe({
        next: (project) => {
          this.projects.update(projects => [...projects, project]);
          this.showCreateModal.set(false);
          this.createProjectForm.reset();
        }
      });
    }
  }

  toggleModal() {
    this.showCreateModal.update(v => !v);
  }
}
