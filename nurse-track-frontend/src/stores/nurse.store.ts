import { defineStore } from 'pinia';
import type { ShiftResponse } from '../types/schemas/shifts.schema';
import type { DepartmentResponse } from '../types/schemas/department.schema';
import type {
  CreateShiftChangeRequest,
  CreateVacationRequest,
  ShiftChangeResponse,
  VacationRequestResponse,
} from '../types/schemas/requests.schema';
import { useNurseShiftService } from '../services/nurse/nurseShift.service';
import { useNurseRequestService } from '../services/nurse/nurseRequest.service';

export const useNurseStore = defineStore('nurse', {
  state: () => ({
    shifts: [] as ShiftResponse[],
    departements: [] as DepartmentResponse[],
    shiftChangeRequests: [] as ShiftChangeResponse[],
    vacationRequests: [] as VacationRequestResponse[],
  }),

  actions: {
    // ==================== SHIFT ACTIONS ====================
    async getMyDepartments(nurseId: number) {
      try {
        this.departements = await useNurseShiftService.getMyDepartments(
          nurseId,
        );
      } catch (error: any) {
        console.error('Error fetching departments:', error);
      }
    },

    async getShifts(nurseId: number) {
      try {
        this.shifts = await useNurseShiftService.getShifts(nurseId);
      } catch (error: any) {
        console.error('Error fetching shifts:', error);
      }
    },

    async getDepartmentShifts(nurseId: number, departmentId: number) {
      try {
        this.shifts = await useNurseShiftService.getDepartmentShifts(
          nurseId,
          departmentId,
        );
      } catch (error: any) {
        console.error('Error fetching shifts for department:', error);
      }
    },

    // ==================== VACATION REQUEST ACTIONS ====================
    async getMyVacationRequests() {
      try {
        this.vacationRequests =
          await useNurseRequestService.getMyVacationRequests();
      } catch (error: any) {
        console.error('Error fetching vacation requests:', error);
      }
    },

    async getVacationRequestById(requestId: number) {
      try {
        const vacationRequest =
          await useNurseRequestService.getVacationRequestById(requestId);
        const vacationRequestIndex = this.vacationRequests.findIndex(
          (vacation) => vacation.id === requestId,
        );

        if (vacationRequestIndex !== -1) {
          this.vacationRequests[vacationRequestIndex] = vacationRequest;
        } else {
          this.vacationRequests.push(vacationRequest);
        }
      } catch (error: any) {
        console.error('Error fetching vacation request by ID:', error);
      }
    },

    async createVacationRequest(request: CreateVacationRequest) {
      try {
        await useNurseRequestService.createVacationRequest(request);
        await this.getMyVacationRequests();
      } catch (error: any) {
        throw error;
      }
    },

    // ==================== SHIFT CHANGE REQUEST ACTIONS ====================
    async getMyShiftChangeRequests() {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getMyShiftChangeRequests();
      } catch (error: any) {
        console.error('Error fetching shift change requests:', error);
      }
    },

    async getMyReceivedShiftChangeRequests() {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getMyReceivedShiftChangeRequests();
      } catch (error: any) {
        console.error('Error fetching received shift change requests:', error);
      }
    },

    async getShiftChangeRequestById(requestId: number) {
      try {
        const shiftChangeRequest =
          await useNurseRequestService.getShiftChangeRequestById(requestId);
        const shiftChangeRequestIndex = this.shiftChangeRequests.findIndex(
          (shiftChange) => shiftChange.id === requestId,
        );

        if (shiftChangeRequestIndex !== -1) {
          this.shiftChangeRequests[shiftChangeRequestIndex] =
            shiftChangeRequest;
        } else {
          this.shiftChangeRequests.push(shiftChangeRequest);
        }
      } catch (error: any) {
        console.error('Error fetching shift change request by ID:', error);
      }
    },

    async createShiftChangeRequest(request: CreateShiftChangeRequest) {
      try {
        await useNurseRequestService.createShiftChangeRequest(request);
        await this.getMyShiftChangeRequests();
      } catch (error: any) {
        throw error;
      }
    },
  },
});
