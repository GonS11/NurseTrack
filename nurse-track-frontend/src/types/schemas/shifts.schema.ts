import { z } from 'zod';
import { validation } from '../validation';
import { ShiftStatus } from '../enums/shift-status.enum';
import { UserSchemas } from './user.schema';
import { DepartmentSchemas } from './department.schema';

export const ShiftTemplateSchemas = {
  response: z
    .object({
      id: validation.requiredId(),
      name: validation.requiredString(1, 100),
      shiftStartTime: validation.dateTime(),
      shiftEndTime: validation.dateTime(),
      type: validation.shiftTypes(),
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
      shiftDate: validation.date(),
      status: validation.shiftStatus().default(ShiftStatus.SCHEDULED),
      notes: validation.optionalString(0, 500),
      createdById: validation.requiredId(),
    })
    .strict(),

  update: z
    .object({
      nurseId: validation.optionalId(),
      departmentId: validation.optionalId(),
      shiftTemplateId: validation.optionalId(),
      shiftDate: validation.date(),
      status: validation.shiftStatus().optional(),
      notes: validation.optionalString(0, 500),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      nurse: UserSchemas.simpleResponse,
      department: DepartmentSchemas.response,
      shiftTemplate: ShiftTemplateSchemas.response,
      shiftDate: z.string(),
      status: validation.shiftStatus(),
      notes: validation.optionalString(0, 500),
      createdBy: UserSchemas.simpleResponse,
      createdAt: validation.dateTime(),
      updatedAt: validation.dateTime(),
      shiftStart: validation.dateTime(),
      shiftEnd: validation.dateTime(),
    })
    .strict(),
};

export type CreateShiftRequest = z.infer<typeof ShiftSchemas.create>;
export type UpdateShiftRequest = z.infer<typeof ShiftSchemas.update>;
export type ShiftResponse = z.infer<typeof ShiftSchemas.response>;
export type ShiftTemplateResponse = z.infer<
  typeof ShiftTemplateSchemas.response
>;
