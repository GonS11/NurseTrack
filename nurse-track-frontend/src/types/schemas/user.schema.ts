import { z } from 'zod';
import { validation } from '../validation';

export const UserSchemas = {
  create: z
    .object({
      firstname: validation.requiredString(1, 50),
      lastname: validation.requiredString(1, 50),
      username: validation.username(),
      email: validation.email(),
      password: validation.password(),
      role: validation.userRole(),
      licenseNumber: validation.optionalLicenseNumber(),
    })
    .strict(),

  update: z
    .object({
      firstname: validation.optionalString(1, 50),
      lastname: validation.optionalString(1, 50),
      password: validation.optionalPassword(),
      role: validation.userRole().optional(),
      licenseNumber: validation.optionalLicenseNumber(),
      isActive: z.boolean().optional(),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      firstname: validation.requiredString(),
      lastname: validation.requiredString(),
      username: validation.requiredString(),
      email: validation.email(),
      role: validation.userRole(),
      isActive: z.boolean(),
      createdAt: validation.dateTime(),
    })
    .strict(),

  simpleResponse: z
    .object({
      id: validation.requiredId(),
      firstname: validation.requiredString(),
      lastname: validation.requiredString(),
      username: validation.requiredString(),
    })
    .strict(),
};

export type CreateUserRequest = z.infer<typeof UserSchemas.create>;
export type UpdateUserRequest = z.infer<typeof UserSchemas.update>;
export type UserResponse = z.infer<typeof UserSchemas.response>;
export type UserSimpleResponse = z.infer<typeof UserSchemas.simpleResponse>;
