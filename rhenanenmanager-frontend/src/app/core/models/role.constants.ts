/**
 * Role name constants matching backend role names
 */
export const ROLES = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER',
  MODERATOR: 'ROLE_MODERATOR'
} as const;

export type RoleName = typeof ROLES[keyof typeof ROLES];
