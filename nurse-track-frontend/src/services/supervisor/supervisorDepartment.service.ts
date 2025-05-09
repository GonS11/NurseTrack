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

  // Get the supervisor's department
  async getMyDepartment(departmentId: number): Promise<DepartmentResponse> {
    const response = await api.get<DepartmentResponse>(
      `/supervisor/departments/${departmentId}`,
    );
    return response.data;
  },

  // ==================== NURSES ====================

  // Get all nurses in the supervisor's department
  async getDepartmentNurses(
    departmentId: number,
  ): Promise<NurseDepartmentResponse[]> {
    const response = await api.get<NurseDepartmentResponse[]>(
      `/supervisor/departments/${departmentId}/nurses`,
    );
    return response.data;
  },

  // Add a nurse to the supervisor's department
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

  // Remove a nurse from the supervisor's department
  async removeNurseFromDepartment(
    departmentId: number,
    nurseId: number,
  ): Promise<void> {
    await api.delete(
      `/supervisor/departments/${departmentId}/nurses/${nurseId}`,
    );
  },

  // ==================== SHIFTS ====================

  // Get all shifts in the supervisor's department
  async getDepartmentShifts(departmentId: number): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/supervisor/departments/${departmentId}/shifts`,
    );
    return response.data;
  },

  // Create a new shift in the supervisor's department
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

  // Update an existing shift in the supervisor's department
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

  // Cancel a shift in the supervisor's department
  async cancelShift(departmentId: number, shiftId: number): Promise<void> {
    await api.delete(
      `/supervisor/departments/${departmentId}/shifts/${shiftId}`,
    );
  },
};
