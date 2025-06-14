// src/stores/nurse.store.ts
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
import type { SupervisorDepartmentResponse } from '../types/schemas/assignments.schema';
import { useAdminAssignmentService } from '../services/admin/adminAssignment.service';

export const useNurseStore = defineStore('nurse', {
  state: () => ({
    shifts: [] as ShiftResponse[],
    allDepartmentShifts: [] as ShiftResponse[],
    departments: [] as DepartmentResponse[],
    shiftChangeRequests: [] as ShiftChangeResponse[],
    vacationRequests: [] as VacationRequestResponse[],
    departmentSupervisors: [] as SupervisorDepartmentResponse[],
  }),

  actions: {
    // ==================== SHIFT ACTIONS ====================
    async getMyDepartments(nurseId: number) {
      try {
        this.departments = await useNurseShiftService.getMyDepartments(nurseId);
        //await this.loadAllSupervisorAssignments();
      } catch (error: any) {
        console.error('Error fetching departments:', error);
        throw error;
      }
    },

    async getShifts(nurseId: number) {
      try {
        this.shifts = await useNurseShiftService.getShifts(nurseId);
      } catch (error: any) {
        console.error('Error fetching shifts:', error);
        throw error;
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
        throw error;
      }
    },

    async getAllShiftsForDepartment(departmentId: number) {
      this.allDepartmentShifts =
        await useNurseShiftService.getAllShiftsInDepartment(departmentId);
    },

    // ==================== VACATION REQUEST ACTIONS ====================
    async getMyVacationRequests(nurseId: number) {
      try {
        this.vacationRequests =
          await useNurseRequestService.getMyVacationRequests(nurseId);
      } catch (error: any) {
        console.error('Error fetching vacation requests:', error);
        throw error;
      }
    },

    async getVacationRequestById(nurseId: number, requestId: number) {
      try {
        const vacationRequest =
          await useNurseRequestService.getVacationRequestById(
            nurseId,
            requestId,
          );
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
        throw error;
      }
    },

    async createVacationRequest(
      nurseId: number,
      request: CreateVacationRequest,
    ) {
      try {
        await useNurseRequestService.createVacationRequest(nurseId, request);
        await this.getMyVacationRequests(nurseId);
      } catch (error: any) {
        throw error;
      }
    },

    // ==================== SHIFT CHANGE REQUEST ACTIONS ====================
    async getMyShiftChangeRequests(nurseId: number) {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getMyShiftChangeRequests(nurseId);
      } catch (error: any) {
        console.error('Error fetching shift change requests:', error);
        throw error;
      }
    },

    async getMyReceivedShiftChangeRequests(nurseId: number) {
      try {
        this.shiftChangeRequests =
          await useNurseRequestService.getMyReceivedShiftChangeRequests(
            nurseId,
          );
      } catch (error: any) {
        console.error('Error fetching received shift change requests:', error);
        throw error;
      }
    },

    async getShiftChangeRequestById(nurseId: number, requestId: number) {
      try {
        const shiftChangeRequest =
          await useNurseRequestService.getShiftChangeRequestById(
            nurseId,
            requestId,
          );
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
        throw error;
      }
    },

    async createShiftChangeRequest(
      nurseId: number,
      request: CreateShiftChangeRequest,
    ) {
      try {
        await useNurseRequestService.createShiftChangeRequest(nurseId, request);
        await this.getMyShiftChangeRequests(nurseId);
      } catch (error: any) {
        throw error;
      }
    },

    // ==================== ASIGNACIÓN DE SUPERVISORES ====================
    async loadAllSupervisorAssignments() {
      try {
        const pageResponse =
          await useAdminAssignmentService.getAllSupervisorAssignments(0, 100);
        this.departmentSupervisors = pageResponse.content;
      } catch (error: any) {
        console.error('Error fetching all department supervisors:', error);
        throw error;
      }
    },
  },
});
