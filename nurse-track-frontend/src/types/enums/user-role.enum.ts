export enum UserRole {
  ADMIN = 'ROLE_ADMIN',
  SUPERVISOR = 'ROLE_SUPERVISOR',
  NURSE = 'ROLE_NURSE',
}

export interface UserRoleConfig {
  displayName: string;
  description: string;
}

export const UserRoleData: Record<UserRole, UserRoleConfig> = {
  [UserRole.ADMIN]: {
    displayName: 'Administrator',
    description: 'Has full access to the system',
  },
  [UserRole.SUPERVISOR]: {
    displayName: 'Supervisor',
    description: 'Manages staff and schedule',
  },
  [UserRole.NURSE]: {
    displayName: 'Nurse',
    description: 'Basic access to the system',
  },
};
