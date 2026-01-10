import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Flexible guard to protect routes based on required roles.
 * Usage in routes:
 *
 * {
 *   path: 'admin',
 *   canActivate: [roleGuard],
 *   data: { roles: ['ROLE_ADMIN'] },
 *   component: AdminComponent
 * }
 *
 * Or for multiple allowed roles:
 * {
 *   path: 'moderation',
 *   canActivate: [roleGuard],
 *   data: { roles: ['ROLE_ADMIN', 'ROLE_MODERATOR'] },
 *   component: ModerationComponent
 * }
 */
export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if user is authenticated
  if (!authService.isAuthenticated()) {
    router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // Get required roles from route data
  const requiredRoles = route.data['roles'] as string[] | undefined;

  // If no roles specified, just check authentication
  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }

  // Check if user has any of the required roles
  const currentUser = authService.getCurrentUser();
  const userRole = currentUser?.role;

  if (userRole && requiredRoles.includes(userRole)) {
    return true;
  }

  // User doesn't have required role
  console.warn(`Access denied: Required roles: ${requiredRoles.join(', ')}, User role: ${userRole}`);
  router.navigate(['/dashboard']);
  return false;
};

/**
 * Factory function to create a guard for a specific role.
 * Usage:
 *
 * {
 *   path: 'admin',
 *   canActivate: [createRoleGuard('ROLE_ADMIN')],
 *   component: AdminComponent
 * }
 */
export function createRoleGuard(allowedRole: string): CanActivateFn {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isAuthenticated()) {
      router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }

    if (authService.hasRole(allowedRole)) {
      return true;
    }

    console.warn(`Access denied: Required role: ${allowedRole}`);
    router.navigate(['/dashboard']);
    return false;
  };
}
