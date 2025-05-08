import { z } from 'zod';
import { validation } from '../validation';
import { UserRoleSchema } from './user.schema';
import { UserRole } from '../enums/user-role.enum';

export const AuthSchemas = {
  loginRequest: z
    .object({
      username: validation.requiredString(5, 50),
      password: validation.requiredString(8, 40),
    })
    .strict(),

  loginResponse: z
    .object({
      token: z.string(),
      tokenType: z.string(),
      username: z.string(),
      role: UserRoleSchema,
      fullName: z.string(),
      email: z.string().email(),
      licenseNumber: z.string().optional(),
    })
    .strict(),

  currentUserResponse: z
    .object({
      id: validation.requiredId(),
      firstName: validation.requiredString(),
      lastName: validation.requiredString(),
      username: validation.requiredString(),
      role: z.nativeEnum(UserRole),
      isActive: z.boolean(),
      createdAt: validation.dateTime(),
      email: validation.email(),
      licenseNumber: validation.optionalLicenseNumber(),
      fullName: validation.requiredString(),
    })
    .strict(),
};

// Tipos inferidos
export type LoginRequest = z.infer<typeof AuthSchemas.loginRequest>;
export type LoginResponse = z.infer<typeof AuthSchemas.loginResponse>;
export type CurrentUserResponse = z.infer<
  typeof AuthSchemas.currentUserResponse
>;
