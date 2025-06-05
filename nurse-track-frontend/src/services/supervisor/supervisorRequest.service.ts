import api from '../../api/axios';
import type {
  ShiftChangeResponse,
  UpdateShiftChangeRequest,
  UpdateVacationRequest,
  VacationRequestResponse,
} from '../../types/schemas/requests.schema';

export const useSupervisorRequestService = {
  //==== VACATION REQUESTS ====
  async getPendingVacationRequests(
    departmentId: number,
  ): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      `/supervisor/departments/${departmentId}/requests/vacations/pending`,
    );

    return response.data;
  },

  async getAllVacationRequests(
    departmentId: number,
  ): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      `/supervisor/departments/${departmentId}/requests/vacations`,
    );

    return response.data;
  },

  async approveVacationRequest(
    departmentId: number,
    requestId: number,
    data: UpdateVacationRequest,
  ): Promise<VacationRequestResponse> {
    const response = await api.put<VacationRequestResponse>(
      `/supervisor/departments/${departmentId}/requests/vacations/${requestId}/approve`,
      data,
    );

    return response.data;
  },

  async rejectVacationRequest(
    departmentId: number,
    requestId: number,
    data: UpdateVacationRequest,
  ): Promise<VacationRequestResponse> {
    const response = await api.put<VacationRequestResponse>(
      `/supervisor/departments/${departmentId}/requests/vacations/${requestId}/reject`,
      data,
    );

    return response.data;
  },

  //==== SHIFT CHANGE REQUESTS ====
  async getPendingShiftChangeRequests(
    departmentId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/supervisor/departments/${departmentId}/requests/shift-changes/pending`,
    );

    return response.data;
  },

  async getAllShiftChangeRequests(
    departmentId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/supervisor/departments/${departmentId}/requests/shift-changes`,
    );

    return response.data;
  },

  async approveShiftChangeRequest(
    departmentId: number,
    requestId: number,
    data: UpdateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    const response = await api.put<ShiftChangeResponse>(
      `/supervisor/departments/${departmentId}/requests/shift-changes/${requestId}/approve`,
      data,
    );

    return response.data;
  },

  async rejectShiftChangeRequest(
    departmentId: number,
    requestId: number,
    data: UpdateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    const response = await api.put<ShiftChangeResponse>(
      `/supervisor/departments/${departmentId}/requests/shift-changes/${requestId}/reject`,
      data,
    );

    return response.data;
  },
};
