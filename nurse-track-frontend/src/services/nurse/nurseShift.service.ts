import api from '../../api/axios';
import type { DepartmentResponse } from '../../types/schemas/department.schema';
import type { ShiftResponse } from '../../types/schemas/shifts.schema';

export const useNurseShiftService = {
  //==== NURSE DEPARTMENTS ====
  async getMyDepartments(nurseId: number): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      `/nurses/${nurseId}/departments`,
    );
    return response.data;
  },

  //==== NURSE SHIFTS ====
  async getShifts(nurseId: number): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/nurses/${nurseId}/shifts`,
    );
    return response.data;
  },

  async getDepartmentShifts(
    nurseId: number,
    departmentId: number,
  ): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/nurses/${nurseId}/departments/${departmentId}/shifts`,
    );
    return response.data;
  },
};
