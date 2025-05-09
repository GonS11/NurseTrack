import api from '../../api/axios';
import { Status } from '../../types/enums/status.enum';
import type {
  ShiftChangeResponse,
  UpdateShiftChangeRequest,
  UpdateVacationRequest,
  VacationRequestResponse,
} from '../../types/schemas/requests.schema';

export const useSupervisorRequestService = {
  // ==================== VACATION REQUESTS ====================

  // Get all pending vacation requests for the supervisor's department
  async getPendingVacationRequests(
    departmentId: number,
  ): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      `/supervisor/departments/${departmentId}/requests/vacations/pending`,
    );
    return response.data;
  },

  // Get all vacation requests for the supervisor's department
  async getAllVacationRequests(
    departmentId: number,
  ): Promise<VacationRequestResponse[]> {
    const response = await api.get<VacationRequestResponse[]>(
      `/supervisor/departments/${departmentId}/requests/vacations`,
    );
    return response.data;
  },

  // Approve a vacation request
  async approveVacationRequest(
    requestId: number,
    data: UpdateVacationRequest,
  ): Promise<VacationRequestResponse> {
    data.status = Status.APPROVED; // Set status to approved
    const response = await api.put<VacationRequestResponse>(
      `/supervisor/departments/requests/vacations/${requestId}/approve`,
      data,
    );
    return response.data;
  },

  // Reject a vacation request
  async rejectVacationRequest(
    requestId: number,
    data: UpdateVacationRequest,
  ): Promise<VacationRequestResponse> {
    data.status = Status.REJECTED; // Set status to rejected
    const response = await api.put<VacationRequestResponse>(
      `/supervisor/departments/requests/vacations/${requestId}/reject`,
      data,
    );
    return response.data;
  },

  // ==================== SHIFT CHANGE REQUESTS ====================

  // Get all pending shift change requests for the supervisor's department
  async getPendingShiftChangeRequests(
    departmentId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/supervisor/departments/${departmentId}/requests/shift-changes/pending`,
    );
    return response.data;
  },

  // Get all shift change requests for the supervisor's department
  async getAllShiftChangeRequests(
    departmentId: number,
  ): Promise<ShiftChangeResponse[]> {
    const response = await api.get<ShiftChangeResponse[]>(
      `/supervisor/departments/${departmentId}/requests/shift-changes`,
    );
    return response.data;
  },

  // Approve a shift change request
  async approveShiftChangeRequest(
    requestId: number,
    data: UpdateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    data.status = Status.REJECTED; // Set status to approved
    const response = await api.put<ShiftChangeResponse>(
      `/supervisor/departments/requests/shift-changes/${requestId}/approve`,
      data,
    );
    return response.data;
  },

  // Reject a shift change request
  async rejectShiftChangeRequest(
    requestId: number,
    data: UpdateShiftChangeRequest,
  ): Promise<ShiftChangeResponse> {
    data.status = Status.REJECTED; // Set status to rejected
    const response = await api.put<ShiftChangeResponse>(
      `/supervisor/departments/requests/shift-changes/${requestId}/reject`,
      data,
    );
    return response.data;
  },
};
