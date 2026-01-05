import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface Project {
  id: string;
  name: string;
  description: string;
  status: 'DRAFT' | 'ACTIVE';
  ownerId: string;
  tasks?: Task[];
}

export interface Task {
  id: string;
  title: string;
  description: string;
  status: 'TODO' | 'DONE';
  dueDate: string;
}

export interface CreateProjectRequest {
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) { }

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  createProject(project: CreateProjectRequest): Observable<Project> {
    return this.http.post<Project>(this.apiUrl, project);
  }

  activateProject(id: string): Observable<Project> {
    return this.http.patch<Project>(`${this.apiUrl}/${id}/activate`, {});
  }

  getProjectById(id: string): Observable<Project | undefined> {
    return this.http.get<Project[]>(this.apiUrl).pipe(
      // Client-side filtering since backend doesn't have getById
      // In a real app, we'd want a backend endpoint
      // mapping the array to find the specific project
      // We need to import map from rxjs
      map(projects => projects.find(p => p.id === id))
    );
  }
}
