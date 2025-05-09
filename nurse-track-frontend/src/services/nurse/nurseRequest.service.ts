import api from '../../api/axios';
import type {
  CreateShiftChangeRequest,
  CreateVacationRequest,
  ShiftChangeResponse,
  VacationRequestResponse,
} from '../../types/schemas/requests.schema';

export const useNurseRequestService = {
  // ==================== VACATION REQUESTS ====================

  // Create a new vacation request
  async createVacationRequest(
    data: CreateVacationRequest,
  ): Promise<VacationRequestResponse> {
    const response = await api.post<VacationRequestResponse>(
      '/nurses/requests/vacations',
      data,
    );
    return response.data;
  },

  // Get a specific vacation request by ID
  async getVacationRequestById(
    requestId: number,
  ): Promise<VacationRequestResponse> {
    const response = await api.get<VacationRequestResponse>(
      `/nurses/requests/vacations/${requestId}`,
    );
    return response.data;
  },

  // Get all vacation requests made by the nurse
  async getMyVacationRequests(): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      '/nurses/requests/vacations',
    );
    return response.data;
  },

  // ==================== SHIFT CHANGE REQUESTS ====================

  // Create a new shift change request
  async createShiftChangeRequest(
    data: CreateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    const response = await api.post<ShiftChangeResponse>(
      '/nurses/requests/shift-changes',
      data,
    );
    return response.data;
  },

  // Get a specific shift change request by ID
  async getShiftChangeRequestById(
    requestId: number,
  ): Promise<ShiftChangeResponse> {
    const response = await api.get<ShiftChangeResponse>(
      `/nurses/requests/shift-changes/${requestId}`,
    );
    return response.data;
  },

  // Get all shift change requests made by the nurse
  async getMyShiftChangeRequests(): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      '/nurses/requests/shift-changes',
    );
    return response.data;
  },

  // Get all shift change requests received by the nurse
  async getReceivedShiftChangeRequests(): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      '/nurses/requests/shift-changes/received',
    );
    return response.data;
  },
};
