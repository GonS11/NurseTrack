import { z } from 'zod';
import { validation } from '../validation';
import { Status } from '../enums/status.enum';
import { UserSchemas } from './user.schema';

export const StatusSchema = z.nativeEnum(Status);

export const VacationRequestSchema = {
  create: z
    .object({
      requestingNurseId: validation.requiredId(),
      startDate: z
        .string()
        .refine(
          (date) =>
            new Date(date) >= new Date(new Date().toISOString().split('T')[0]),
          { message: 'Start date must be in the future or present' },
        ),
      endDate: z
        .string()
        .refine(
          (date) =>
            new Date(date) >= new Date(new Date().toISOString().split('T')[0]),
          { message: 'End date must be in the future or present' },
        ),
      reason: validation.requiredString(1, 2000),
      status: StatusSchema.default(Status.PENDING),
      reviewedById: validation.requiredId(),
    })
    .strict(),

  update: z
    .object({
      status: StatusSchema,
      reviewedNotes: validation.optionalString(0, 2000),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      requester: UserSchemas.simpleResponse,
      startDate: z.string(), // LocalDate as ISO string
      endDate: z.string(), // LocalDate as ISO string
      reason: validation.requiredString(1, 2000),
      reviewedNotes: validation.optionalString(0, 2000),
      status: StatusSchema,
      reviewedBy: UserSchemas.simpleResponse.optional(),
      reviewedAt: validation.dateTime().optional(),
      createdAt: validation.dateTime(),
    })
    .strict(),
};

export const ShiftChangeRequestSchemas = {
  create: z
    .object({
      requestingNurseId: validation.requiredId(),
      offeredShiftId: validation.requiredId(),
      receivingNurseId: validation.requiredId(),
      desiredShiftId: validation.requiredId(),
      status: StatusSchema.default(Status.PENDING),
      reason: validation.requiredString(1, 1000),
    })
    .strict(),

  update: z
    .object({
      status: z.enum([Status.APPROVED, Status.REJECTED, Status.CANCELLED]),
      reviewedNotes: validation.requiredString(1, 1000),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      requestingNurse: UserSchemas.simpleResponse,
      offeredShiftId: validation.requiredId(),
      receivingNurse: UserSchemas.simpleResponse,
      desiredShiftId: validation.requiredId(),
      reason: validation.requiredString(1, 1000),
      reviewedNotes: validation.optionalString(0, 1000),
      status: StatusSchema,
      reviewedBy: UserSchemas.simpleResponse.optional(),
      createdAt: validation.dateTime(),
      reviewedAt: validation.dateTime().optional(),
    })
    .strict(),
};

// Tipos inferidos
export type CreateShiftChangeRequest = z.infer<
  typeof ShiftChangeRequestSchemas.create
>;
export type UpdateShiftChangeRequest = z.infer<
  typeof ShiftChangeRequestSchemas.update
>;
export type ShiftChangeResponse = z.infer<
  typeof ShiftChangeRequestSchemas.response
>;

export type CreateVacationRequest = z.infer<
  typeof VacationRequestSchema.create
>;
export type UpdateVacationRequest = z.infer<
  typeof VacationRequestSchema.update
>;
export type VacationRequestResponse = z.infer<
  typeof VacationRequestSchema.response
>;
