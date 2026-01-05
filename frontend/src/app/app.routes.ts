import { Routes } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/auth';

import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { ProjectDetailsComponent } from './pages/project-details/project-details';

const authGuard = () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.getToken()) {
        return true;
    }

    return router.parseUrl('/login');
};

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [authGuard]
    },
    {
        path: 'projects/:id',
        component: ProjectDetailsComponent,
        canActivate: [authGuard]
    },
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    { path: '**', redirectTo: '/dashboard' }
];
