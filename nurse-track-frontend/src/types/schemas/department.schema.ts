import { z } from 'zod';
import { validation } from '../validation';

export const DepartmentSchemas = {
  create: z
    .object({
      name: validation.requiredString(2, 100),
      location: validation.requiredString(2, 100),
    })
    .strict(),

  update: z
    .object({
      name: validation.requiredString(2, 100).optional(),
      location: validation.requiredString(2, 100).optional(),
      isActive: z.boolean().optional(),
    })
    .partial(),

  response: z
    .object({
      id: validation.requiredId(),
      name: validation.requiredString(2, 100),
      location: validation.requiredString(2, 100),
      isActive: z.boolean(),
      createdAt: validation.dateTime(),
      updatedAt: validation.dateTime(),
    })
    .strict(),
};

export type CreateDepartmentRequest = z.infer<typeof DepartmentSchemas.create>;
export type UpdateDepartmentRequest = z.infer<typeof DepartmentSchemas.update>;
export type DepartmentResponse = z.infer<typeof DepartmentSchemas.response>;
