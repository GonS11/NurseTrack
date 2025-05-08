import { z } from 'zod';
import { validation } from '../validation';
import { UserRole } from '../enums/user-role.enum';

export const UserRoleSchema = z.nativeEnum(UserRole);

export const UserSchemas = {
  create: z
    .object({
      firstName: validation.requiredString(1, 50),
      lastName: validation.requiredString(1, 50),
      username: validation.username(),
      email: validation.email(),
      password: validation.password(),
      role: z.nativeEnum(UserRole, { required_error: 'Role is required' }),
      licenseNumber: validation.optionalLicenseNumber(),
    })
    .strict(),

  update: z
    .object({
      firstName: validation.optionalString(1, 50),
      lastName: validation.optionalString(1, 50),
      licenseNumber: validation.optionalLicenseNumber(),
      isActive: z.boolean().optional(),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      firstName: validation.requiredString(),
      lastName: validation.requiredString(),
      username: validation.requiredString(),
      role: z.nativeEnum(UserRole),
      isActive: z.boolean(),
      createdAt: validation.dateTime(),
      fullName: validation.requiredString(),
    })
    .strict(),

  simpleResponse: z
    .object({
      id: validation.requiredId(),
      fullName: validation.requiredString(),
      username: validation.requiredString(),
    })
    .strict(),
};

export type CreateUserRequest = z.infer<typeof UserSchemas.create>;
export type UpdateUserRequest = z.infer<typeof UserSchemas.update>;
export type UserResponse = z.infer<typeof UserSchemas.response>;
export type UserSimpleResponse = z.infer<typeof UserSchemas.simpleResponse>;
