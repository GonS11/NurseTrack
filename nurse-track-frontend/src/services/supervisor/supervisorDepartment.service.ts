import api from '../../api/axios';
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../../types/schemas/assignments.schema';
import type { DepartmentResponse } from '../../types/schemas/department.schema';
import type {
  CreateShiftRequest,
  ShiftResponse,
  UpdateShiftRequest,
} from '../../types/schemas/shifts.schema';

export const useSupervisorDepartmentService = {
  // ==================== DEPARTMENT ====================
  async getAllMyDepartments(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/supervisor/departments',
    );

    return response.data;
  },

  async getMyDepartment(departmentId: number): Promise<DepartmentResponse> {
    const response = await api.get<DepartmentResponse>(
      `/supervisor/departments/${departmentId}`,
    );

    return response.data;
  },

  // ==================== NURSES ====================
  async getDepartmentNurses(
    departmentId: number,
  ): Promise<NurseDepartmentResponse[]> {
    const response = await api.get<NurseDepartmentResponse[]>(
      `/supervisor/departments/${departmentId}/nurses`,
    );

    return response.data;
  },

  async addNurseToDepartment(
    departmentId: number,
    data: AssignNurseRequest,
  ): Promise<NurseDepartmentResponse> {
    const response = await api.post<NurseDepartmentResponse>(
      `/supervisor/departments/${departmentId}/nurses`,
      data,
    );

    return response.data;
  },

  async removeNurseFromDepartment(
    departmentId: number,
    nurseId: number,
  ): Promise<void> {
    await api.delete(
      `/supervisor/departments/${departmentId}/nurses/${nurseId}`,
    );
  },

  // ==================== SHIFTS ====================
  async getDepartmentShifts(departmentId: number): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/supervisor/departments/${departmentId}/shifts`,
    );

    return response.data;
  },

  async createShift(
    departmentId: number,
    data: CreateShiftRequest,
  ): Promise<ShiftResponse> {
    const response = await api.post<ShiftResponse>(
      `/supervisor/departments/${departmentId}/shifts`,
      data,
    );
    return response.data;
  },

  async updateShift(
    departmentId: number,
    shiftId: number,
    data: UpdateShiftRequest,
  ): Promise<ShiftResponse> {
    const response = await api.put<ShiftResponse>(
      `/supervisor/departments/${departmentId}/shifts/${shiftId}`,
      data,
    );
    return response.data;
  },

  async cancelShift(departmentId: number, shiftId: number): Promise<void> {
    await api.delete(
      `/supervisor/departments/${departmentId}/shifts/${shiftId}`,
    );
  },
};
