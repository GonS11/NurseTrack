import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  AssignNurseRequest,
  AssignSupervisorRequest,
  NurseDepartmentResponse,
  SupervisorDepartmentResponse,
} from '../../types/schemas/assignments.schema';
import type { DepartmentResponse } from '../../types/schemas/department.schema';

export const useAdminAssignmentService = {
  // ======= SUPERVISOR =======
  getAllSupervisorAssignments: (page = 0, size = 10, sortBy = 'departmentId') =>
    api
      .get<Page<SupervisorDepartmentResponse>>(
        '/admin/assignments/departments',
        {
          params: { page, size, sortBy },
        },
      )
      .then((res) => res.data),

  getUnassignedDepartmentsForSupervisor: () =>
    api
      .get<DepartmentResponse[]>('/admin/assignments/departments/unassigned')
      .then((res) => res.data),

  getSupervisorByDepartment: (departmentId: number) =>
    api
      .get<SupervisorDepartmentResponse>(
        `/admin/assignments/departments/${departmentId}/supervisor`,
      )
      .then((res) => res.data),

  assignSupervisorToDepartment: (data: AssignSupervisorRequest) =>
    api
      .post<SupervisorDepartmentResponse>(
        `/admin/assignments/departments/${data.departmentId}/supervisor`,
        data,
      )
      .then((res) => res.data),

  removeSupervisorFromDepartment: (departmentId: number) =>
    api
      .delete<void>(`/admin/assignments/departments/${departmentId}/supervisor`)
      .then((res) => res.data),

  // ======= NURSE =============
  getAllNurseAssignments: (page = 0, size = 10, sortBy = 'departmentId') =>
    api
      .get<Page<NurseDepartmentResponse>>(
        '/admin/assignments/departments/nurses',
        { params: { page, size, sortBy } },
      )
      .then((res) => res.data),

  getUnassignedDepartmentsForNurses: () =>
    api
      .get<DepartmentResponse[]>(
        '/admin/assignments/departments/nurses/unassigned',
      )
      .then((res) => res.data),

  getAllNursesByDepartment: (departmentId: number) =>
    api
      .get<NurseDepartmentResponse[]>(
        `/admin/assignments/departments/${departmentId}/nurses`,
      )
      .then((res) => res.data),

  assignNurseToDepartment: (data: AssignNurseRequest) =>
    api
      .post<NurseDepartmentResponse>(
        `/admin/assignments/departments/${data.departmentId}/nurses`,
        data,
      )
      .then((res) => res.data),

  removeNurseFromDepartment: (departmentId: number, nurseId: number) =>
    api
      .delete<void>(
        `/admin/assignments/departments/${departmentId}/nurses/${nurseId}`,
      )
      .then((res) => res.data),
};
