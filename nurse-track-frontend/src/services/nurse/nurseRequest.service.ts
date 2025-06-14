import api from '../../api/axios';
import type {
  CreateShiftChangeRequest,
  CreateVacationRequest,
  ShiftChangeResponse,
  VacationRequestResponse,
} from '../../types/schemas/requests.schema';

export const useNurseRequestService = {
  //==== VACATION REQUESTS ====
  async getMyVacationRequests(
    nurseId: number,
  ): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      `/nurses/${nurseId}/requests/vacations`,
    );
    return response.data;
  },

  async getVacationRequestById(
    nurseId: number,
    requestId: number,
  ): Promise<VacationRequestResponse> {
    const response = await api.get<VacationRequestResponse>(
      `/nurses/${nurseId}/requests/vacations/${requestId}`,
    );
    return response.data;
  },

  async createVacationRequest(
    nurseId: number,
    data: CreateVacationRequest,
  ): Promise<VacationRequestResponse> {
    const response = await api.post<VacationRequestResponse>(
      `/nurses/${nurseId}/requests/vacations`,
      data,
    );
    return response.data;
  },

  //==== SHIFT CHANGE REQUESTS ====
  async getMyShiftChangeRequests(
    nurseId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/nurses/${nurseId}/requests/shift-changes`,
    );
    return response.data;
  },

  async getMyReceivedShiftChangeRequests(
    nurseId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/nurses/${nurseId}/requests/shift-changes/received`,
    );
    return response.data;
  },

  async getShiftChangeRequestById(
    nurseId: number,
    requestId: number,
  ): Promise<ShiftChangeResponse> {
    const response = await api.get<ShiftChangeResponse>(
      `/nurses/${nurseId}/requests/shift-changes/${requestId}`,
    );
    return response.data;
  },

  async createShiftChangeRequest(
    nurseId: number,
    data: CreateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    const response = await api.post<ShiftChangeResponse>(
      `/nurses/${nurseId}/requests/shift-changes`,
      data,
    );
    return response.data;
  },
};
