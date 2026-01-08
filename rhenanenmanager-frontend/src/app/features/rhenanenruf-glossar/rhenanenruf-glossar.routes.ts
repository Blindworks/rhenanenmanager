import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export const rhenanenrufGlossarRoutes: Routes = [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./rhenanenruf-glossar/rhenanenruf-glossar.component')
      .then(m => m.RhenanenrufGlossarComponent)
  }
];
