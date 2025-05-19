import { z } from 'zod';
import { validation } from '../validation';
import { ShiftStatus } from '../enums/shift-status.enum';
import { ShiftType } from '../enums/shift-types.enum';
import { UserSchemas } from './user.schema';
import { DepartmentSchemas } from './department.schema';

export const ShiftStatusSchema = z.nativeEnum(ShiftStatus);
export const ShiftTypeSchema = z.nativeEnum(ShiftType);

export const ShiftTemplateSchemas = {
  response: z
    .object({
      id: validation.requiredId(),
      name: validation.requiredString(1, 100),
      shiftStartTime: validation.dateTime(),
      shiftEndTime: validation.dateTime(),
      type: ShiftTypeSchema,
      createdAt: validation.dateTime(),
      updatedAt: validation.dateTime(),
    })
    .strict(),
};

export const ShiftSchemas = {
  create: z
    .object({
      nurseId: validation.requiredId(),
      departmentId: validation.requiredId(),
      shiftTemplateId: validation.requiredId(),
      shiftDate: z
        .string()
        .refine(
          (date) =>
            new Date(date) >= new Date(new Date().toISOString().split('T')[0]),
          { message: 'Shift date must be today or in the future' },
        ),
      status: ShiftStatusSchema.default(ShiftStatus.SCHEDULED),
      notes: validation.optionalString(0, 500),
      createdById: validation.requiredId(),
    })
    .strict(),

  update: z
    .object({
      nurseId: validation.optionalId(),
      departmentId: validation.optionalId(),
      shiftTemplateId: validation.optionalId(),
      shiftDate: z
        .string()
        .refine(
          (date) =>
            new Date(date) >= new Date(new Date().toISOString().split('T')[0]),
          { message: 'Shift date must be today or in the future' },
        ),
      status: ShiftStatusSchema.optional(),
      notes: validation.optionalString(0, 500),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      nurse: UserSchemas.simpleResponse,
      department: DepartmentSchemas.response,
      shiftTemplate: ShiftTemplateSchemas.response,
      shiftDate: z.string(), // LocalDate as ISO string
      status: ShiftStatusSchema,
      notes: validation.optionalString(0, 500),
      createdBy: UserSchemas.simpleResponse,
      createdAt: validation.dateTime(),
      updatedAt: validation.dateTime(),
      shiftStart: validation.dateTime(), // Calculated in the backend
      shiftEnd: validation.dateTime(), // Calculated in the backend
    })
    .strict(),
};

// Tipos inferidos
export type CreateShiftRequest = z.infer<typeof ShiftSchemas.create>;
export type UpdateShiftRequest = z.infer<typeof ShiftSchemas.update>;
export type ShiftResponse = z.infer<typeof ShiftSchemas.response>;
export type ShiftTemplateResponse = z.infer<
  typeof ShiftTemplateSchemas.response
>;
