import { Routes } from '@angular/router';

export const CONNECTIONS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./connections-overview/connections-overview.component')
      .then(m => m.ConnectionsOverviewComponent)
  }
];
