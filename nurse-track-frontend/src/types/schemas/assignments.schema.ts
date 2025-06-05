import { z } from 'zod';
import { validation } from '../validation';
import { UserSchemas } from './user.schema';
import { DepartmentSchemas } from './department.schema';

export const AssignmentSchemas = {
  assignNurse: z
    .object({
      departmentId: validation.requiredId(),
      nurseId: validation.requiredId(),
    })
    .strict(),

  assignSupervisor: z
    .object({
      departmentId: validation.requiredId(),
      supervisorId: validation.requiredId(),
    })
    .strict(),

  nurseDepartmentResponse: z
    .object({
      id: validation.requiredId(),
      nurse: UserSchemas.simpleResponse,
      department: DepartmentSchemas.response,
      assignedAt: validation.dateTime(),
    })
    .strict(),

  supervisorDepartmentResponse: z
    .object({
      id: validation.requiredId(),
      supervisor: UserSchemas.simpleResponse,
      department: DepartmentSchemas.response,
      assignedAt: validation.dateTime(),
    })
    .strict(),
};

export type AssignNurseRequest = z.infer<typeof AssignmentSchemas.assignNurse>;
export type AssignSupervisorRequest = z.infer<
  typeof AssignmentSchemas.assignSupervisor
>;
export type NurseDepartmentResponse = z.infer<
  typeof AssignmentSchemas.nurseDepartmentResponse
>;
export type SupervisorDepartmentResponse = z.infer<
  typeof AssignmentSchemas.supervisorDepartmentResponse
>;
