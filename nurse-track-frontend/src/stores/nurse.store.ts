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

    async fetchDepartments(nurseId: number) {
      try {
        this.departements = await useNurseShiftService.getNurseDepartments(
          nurseId,
        );
      } catch (error) {
        console.error('Error fetching departments:', error);
      }
    },

    async fetchShifts(nurseId: number) {
      try {
        this.shifts = await useNurseShiftService.getNurseShifts(nurseId);
      } catch (error) {
        console.error('Error fetching shifts:', error);
      }
    },

    async getShiftOfDepartment(nurseId: number, departmentId: number) {
      try {
        this.shifts = await useNurseShiftService.getDepartmentShiftsForNurse(
          nurseId,
          departmentId,
        );
      } catch (error) {
        console.error('Error fetching shifts for department:', error);
      }
    },

    // ==================== VACATION REQUEST ACTIONS ====================

    async fetchMyVacationRequest() {
      try {
        this.vacationRequests =
          await useNurseRequestService.getMyVacationRequests();
      } catch (error) {
        console.error('Error fetching vacation requests:', error);
      }
    },

    async fetchVacationRequestById(requestId: number) {
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
      } catch (error) {
        console.error('Error fetching vacation request by ID:', error);
      }
    },

    async createVacationRequest(request: CreateVacationRequest) {
      try {
        const newVacationRequest =
          await useNurseRequestService.createVacationRequest(request);
        this.vacationRequests.push(newVacationRequest);
      } catch (error) {
        console.error('Error creating vacation request:', error);
      }
    },

    // ==================== SHIFT CHANGE REQUEST ACTIONS ====================

    async fetchMyShiftChangeRequests() {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getMyShiftChangeRequests();
      } catch (error) {
        console.error('Error fetching shift change requests:', error);
      }
    },

    async fetchShiftChangeRequestById(requestId: number) {
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
      } catch (error) {
        console.error('Error fetching shift change request by ID:', error);
      }
    },

    async fetchMyReceivingShiftChange() {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getReceivedShiftChangeRequests();
      } catch (error) {
        console.error('Error fetching received shift change requests:', error);
      }
    },

    async createShiftChangeRequest(request: CreateShiftChangeRequest) {
      try {
        const newShiftChangeRequest =
          await useNurseRequestService.createShiftChangeRequest(request);
        this.shiftChangeRequests.push(newShiftChangeRequest);
      } catch (error) {
        console.error('Error creating shift change request:', error);
      }
    },
  },
});
