import api from '../../api/axios';
import type {
  CreateShiftChangeRequest,
  CreateVacationRequest,
  ShiftChangeResponse,
  VacationRequestResponse,
} from '../../types/schemas/requests.schema';

export const useNurseRequestService = {
  // ==================== VACATION REQUESTS ====================
  async createVacationRequest(
    data: CreateVacationRequest,
  ): Promise<VacationRequestResponse> {
    const response = await api.post<VacationRequestResponse>(
      '/nurses/requests/vacations',
      data,
    );
    return response.data;
  },

  async getVacationRequestById(
    requestId: number,
  ): Promise<VacationRequestResponse> {
    const response = await api.get<VacationRequestResponse>(
      `/nurses/requests/vacations/${requestId}`,
    );
    return response.data;
  },

  async getMyVacationRequests(): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      '/nurses/requests/vacations',
    );
    return response.data;
  },

  // ==================== SHIFT CHANGE REQUESTS ====================
  async createShiftChangeRequest(
    data: CreateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    const response = await api.post<ShiftChangeResponse>(
      '/nurses/requests/shift-changes',
      data,
    );

    return response.data;
  },

  async getShiftChangeRequestById(
    requestId: number,
  ): Promise<ShiftChangeResponse> {
    const response = await api.get<ShiftChangeResponse>(
      `/nurses/requests/shift-changes/${requestId}`,
    );

    return response.data;
  },

  async getMyShiftChangeRequests(): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      '/nurses/requests/shift-changes',
    );

    return response.data;
  },

  async getReceivedShiftChangeRequests(): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      '/nurses/requests/shift-changes/received',
    );

    return response.data;
  },
};
