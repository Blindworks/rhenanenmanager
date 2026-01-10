import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/welcome/welcome.routes').then(m => m.WELCOME_ROUTES)
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES),
    canActivate: [authGuard]
  },
  {
    path: 'profile',
    loadChildren: () => import('./features/profile/profile.routes').then(m => m.PROFILE_ROUTES),
    canActivate: [authGuard]
  },
  {
    path: 'rhenanenruf-glossar',
    loadChildren: () => import('./features/rhenanenruf-glossar/rhenanenruf-glossar.routes').then(m => m.rhenanenrufGlossarRoutes),
    canActivate: [authGuard]
  },
  {
    path: 'connections',
    loadChildren: () => import('./features/connections/connections.routes').then(m => m.CONNECTIONS_ROUTES),
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '/' }
];
