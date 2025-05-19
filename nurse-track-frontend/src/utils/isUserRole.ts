import { UserRole } from '../types/enums/user-role.enum';

export function isUserRole(role: any): role is UserRole {
  return (
    typeof role === 'string' &&
    Object.values(UserRole).includes(role as UserRole)
  );
}
