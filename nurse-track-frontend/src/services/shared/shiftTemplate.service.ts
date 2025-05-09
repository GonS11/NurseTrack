import api from '../../api/axios';
import type { ShiftTemplateResponse } from '../../types/schemas/shifts.schema';

export const useShiftTemplateService = {
  // Get all shift templates
  async getShiftTemplates(): Promise<ShiftTemplateResponse[]> {
    const response = await api.get<ShiftTemplateResponse[]>('/shift-templates');
    return response.data;
  },

  // Get a specific shift template by ID
  async getShiftTemplateById(id: number): Promise<ShiftTemplateResponse> {
    const response = await api.get<ShiftTemplateResponse>(
      `/shift-templates/${id}`,
    );
    return response.data;
  },
};
