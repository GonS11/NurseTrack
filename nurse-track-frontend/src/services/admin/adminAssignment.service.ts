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
  //=======SUPERVISOR=======
  async getAllSupervisorAssignments(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'departmentId',
  ): Promise<Page<SupervisorDepartmentResponse>> {
    const response = await api.get<Page<SupervisorDepartmentResponse>>(
      '/admin/assignments/departments',
      { params: { page, size, sortBy } },
    );

    return response.data;
  },

  async getUnassignedDepartmentsForSupervisor(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/admin/assignments/departments/unassigned',
    );

    return response.data;
  },

  async getSupervisorByDepartment(
    departmentId: number,
  ): Promise<SupervisorDepartmentResponse> {
    const response = await api.get<SupervisorDepartmentResponse>(
      `/admin/assignments/departments/${departmentId}/supervisor`,
    );

    return response.data;
  },

  async assignSupervisorToDepartment(
    data: AssignSupervisorRequest,
  ): Promise<SupervisorDepartmentResponse> {
    const response = await api.post<SupervisorDepartmentResponse>(
      `/admin/assignments/departments/${data.departmentId}/supervisor`,
      data,
    );

    return response.data;
  },

  async removeSupervisorFromDepartment(departmentId: number): Promise<void> {
    await api.delete(
      `/admin/assignments/departments/${departmentId}/supervisor`,
    );
  },

  //=======NURSE=======
  async getAllNurseAssignments(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'departmentId',
  ): Promise<Page<NurseDepartmentResponse>> {
    const response = await api.get<Page<NurseDepartmentResponse>>(
      '/admin/assignments/departments/nurses',
      { params: { page, size, sortBy } },
    );

    return response.data;
  },

  async getUnassignedDepartmentsForNurses(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/admin/assignments/departments/nurses/unassigned',
    );

    return response.data;
  },

  async getAllNursesByDepartment(
    departmentId: number,
  ): Promise<NurseDepartmentResponse[]> {
    const response = await api.get<NurseDepartmentResponse[]>(
      `/admin/assignments/departments/${departmentId}/nurses`,
    );

    return response.data;
  },

  async assignNurseToDepartment(
    data: AssignNurseRequest,
  ): Promise<NurseDepartmentResponse> {
    const response = await api.post<NurseDepartmentResponse>(
      `/admin/assignments/departments/${data.departmentId}/nurses`,
      data,
    );

    return response.data;
  },

  async removeNurseFromDepartment(
    departmentId: number,
    nurseId: number,
  ): Promise<void> {
    await api.delete(
      `/admin/assignments/departments/${departmentId}/nurses/${nurseId}`,
    );
  },
};
