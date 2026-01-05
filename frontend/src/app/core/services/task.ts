import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from './project';

export interface CreateTaskRequest {
  title: string;
  description: string;
  dueDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  createTask(projectId: string, task: CreateTaskRequest): Observable<Task> {
    return this.http.post<Task>(`${this.apiUrl}/projects/${projectId}/tasks`, task);
  }

  completeTask(taskId: string): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/tasks/${taskId}/complete`, {});
  }

  deleteTask(taskId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/tasks/${taskId}`);
  }
}
