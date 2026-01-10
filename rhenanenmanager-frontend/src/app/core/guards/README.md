# Route Guards

This directory contains route guards for protecting routes based on authentication and authorization.

## Available Guards

### 1. `authGuard` - Authentication Guard
Protects routes that require any authenticated user.

**Usage:**
```typescript
import { authGuard } from '@core/guards';

const routes: Routes = [
  {
    path: 'dashboard',
    canActivate: [authGuard],
    component: DashboardComponent
  }
];
```

**Behavior:**
- ✅ Allows authenticated users
- ❌ Redirects to `/auth/login` if not authenticated
- Preserves return URL for redirect after login

---

### 2. `adminGuard` - Admin-Only Guard
Protects routes that require admin privileges.

**Usage:**
```typescript
import { adminGuard } from '@core/guards';

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [adminGuard],
    component: AdminPanelComponent
  }
];
```

**Behavior:**
- ✅ Allows users with `ROLE_ADMIN`
- ❌ Redirects to `/auth/login` if not authenticated
- ❌ Redirects to `/dashboard` if authenticated but not admin
- Logs warning in console for access denial

---

### 3. `roleGuard` - Flexible Role-Based Guard
Protects routes based on one or more required roles (configured via route data).

**Usage (single role):**
```typescript
import { roleGuard } from '@core/guards';

const routes: Routes = [
  {
    path: 'moderation',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_MODERATOR'] },
    component: ModerationComponent
  }
];
```

**Usage (multiple allowed roles):**
```typescript
const routes: Routes = [
  {
    path: 'admin-or-mod',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MODERATOR'] }, // OR logic
    component: AdminOrModComponent
  }
];
```

**Behavior:**
- ✅ Allows users with ANY of the specified roles
- ❌ Redirects to `/auth/login` if not authenticated
- ❌ Redirects to `/dashboard` if user doesn't have required role
- Logs warning with required vs actual role

---

### 4. `createRoleGuard(role)` - Role Guard Factory
Creates a guard for a specific role (alternative to `roleGuard` with data).

**Usage:**
```typescript
import { createRoleGuard } from '@core/guards';
import { ROLES } from '@core/models/role.constants';

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [createRoleGuard(ROLES.ADMIN)],
    component: AdminComponent
  }
];
```

**Benefits:**
- Type-safe with `ROLES` constants
- More explicit in route configuration
- Better for single-role requirements

---

## Role Constants

Always use the `ROLES` constant for type safety:

```typescript
import { ROLES } from '@core/models/role.constants';

// Available roles:
ROLES.ADMIN      // 'ROLE_ADMIN'
ROLES.USER       // 'ROLE_USER'
ROLES.MODERATOR  // 'ROLE_MODERATOR'
```

---

## Examples

### Combining Guards
```typescript
// Require authentication + admin role
{
  path: 'admin',
  canActivate: [authGuard, adminGuard],
  component: AdminComponent
}
```

### Protecting Child Routes
```typescript
{
  path: 'admin',
  canActivate: [adminGuard],
  children: [
    { path: 'users', component: UserManagementComponent },
    { path: 'settings', component: SettingsComponent }
    // All child routes automatically protected
  ]
}
```

### Protecting Lazy-Loaded Modules
```typescript
{
  path: 'admin',
  canActivate: [adminGuard],
  loadChildren: () => import('./admin/admin.routes').then(m => m.ADMIN_ROUTES)
}
```

---

## Testing Guards

To check user roles in components:

```typescript
import { AuthService } from '@core/services/auth.service';

constructor(private authService: AuthService) {}

ngOnInit() {
  if (this.authService.isAdmin()) {
    // Show admin features
  }

  if (this.authService.hasRole('ROLE_MODERATOR')) {
    // Show moderator features
  }
}
```

---

## Migration Notes

If you have old role checks like:
```typescript
// ❌ Old way
currentUser?.roles?.includes('ADMIN')

// ✅ New way
authService.isAdmin()
```

Replace with centralized `AuthService` methods for consistency.
