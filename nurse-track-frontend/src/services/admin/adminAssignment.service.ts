import api from '../../api/axios';
import type {
  AssignNurseRequest,
  AssignSupervisorRequest,
  NurseDepartmentResponse,
  SupervisorDepartmentResponse,
} from '../../types/schemas/assignments.schema';

export const useAdminAssignmentService = {
  // Get the supervisor assigned to a department
  async getDepartmentSupervisor(
    departmentId: number,
  ): Promise<SupervisorDepartmentResponse> {
    const response = await api.get<SupervisorDepartmentResponse>(
      `/admin/assignments/departments/${departmentId}/supervisor`,
    );
    return response.data;
  },

  // Assign a supervisor to a department
  async assignSupervisor(
    data: AssignSupervisorRequest,
  ): Promise<SupervisorDepartmentResponse> {
    const response = await api.post<SupervisorDepartmentResponse>(
      `/admin/assignments/departments/${data.departmentId}/supervisor`,
      data,
    );
    return response.data;
  },

  // Remove the supervisor from a department
  async removeSupervisorFromDepartment(departmentId: number): Promise<void> {
    await api.delete(
      `/admin/assignments/departments/${departmentId}/supervisor`,
    );
  },

  // Get all nurses assigned to a department
  async getNursesByDepartment(
    departmentId: number,
  ): Promise<NurseDepartmentResponse[]> {
    const response = await api.get<NurseDepartmentResponse[]>(
      `/admin/assignments/departments/${departmentId}/nurses`,
    );
    return response.data;
  },

  // Assign a nurse to a department
  async assignNurseToDepartment(
    data: AssignNurseRequest,
  ): Promise<NurseDepartmentResponse> {
    const response = await api.post<NurseDepartmentResponse>(
      `/admin/assignments/departments/${data.departmentId}/nurses`,
      data,
    );
    return response.data;
  },

  // Remove a nurse from a department
  async removeNurseFromDepartment(
    departmentId: number,
    nurseId: number,
  ): Promise<void> {
    await api.delete(
      `/admin/assignments/departments/${departmentId}/nurses/${nurseId}`,
    );
  },
};
