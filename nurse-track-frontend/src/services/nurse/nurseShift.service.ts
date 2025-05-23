import api from '../../api/axios';
import type { DepartmentResponse } from '../../types/schemas/department.schema';
import type { ShiftResponse } from '../../types/schemas/shifts.schema';

export const useNurseShiftService = {
  async getNurseDepartments(nurseId: number): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      `/nurses/${nurseId}/departments`,
    );

    return response.data;
  },

  async getNurseShifts(nurseId: number): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/nurses/${nurseId}/shifts`,
    );

    return response.data;
  },

  async getDepartmentShiftsForNurse(
    nurseId: number,
    departmentId: number,
  ): Promise<ShiftResponse[]> {
    const response = await api.get<ShiftResponse[]>(
      `/nurses/${nurseId}/departments/${departmentId}/shifts`,
    );

    return response.data;
  },
};
