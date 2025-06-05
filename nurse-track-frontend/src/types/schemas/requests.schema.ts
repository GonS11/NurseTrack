import { z } from 'zod';
import { validation } from '../validation';
import { UserSchemas } from './user.schema';
import { RequestStatus } from '../enums/status.enum';

export const VacationRequestSchema = {
  create: z
    .object({
      requestingNurseId: validation.requiredId(),
      startDate: validation.date(),
      endDate: validation.date(),
      reason: validation.requiredString(1, 2000),
      status: validation.requestStatus().default(RequestStatus.PENDING),
      reviewedById: validation.requiredId(),
    })
    .strict(),

  update: z
    .object({
      status: validation.requestStatus(),
      reviewedNotes: validation.optionalString(0, 2000),
    })
    .strict(),

  response: z
    .object({
      id: validation.requiredId(),
      requestingNurse: UserSchemas.simpleResponse,
      startDate: z.string(), // LocalDate as ISO string
      endDate: z.string(),
      reason: validation.requiredString(1, 2000),
      reviewedNotes: validation.optionalString(0, 2000),
      status: validation.requestStatus(),
      reviewedBy: UserSchemas.simpleResponse.optional(),
      reviewedAt: validation.dateTime().optional(),
      createdAt: validation.dateTime(),
      durationDays: validation.dateTime(),
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
      reason: validation.requiredString(1, 1000),
      status: validation.requestStatus().default(RequestStatus.PENDING),
    })
    .strict(),

  update: z
    .object({
      status: z.enum([
        RequestStatus.APPROVED,
        RequestStatus.REJECTED,
        RequestStatus.CANCELLED,
      ]),
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
      status: validation.requestStatus(),
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
