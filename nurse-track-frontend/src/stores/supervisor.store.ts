import { defineStore } from 'pinia';
import type { DepartmentResponse } from '../types/schemas/department.schema';
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../types/schemas/assignments.schema';
import type {
  CreateShiftRequest,
  ShiftResponse,
  UpdateShiftRequest,
} from '../types/schemas/shifts.schema';
import type {
  ShiftChangeResponse,
  UpdateShiftChangeRequest,
  UpdateVacationRequest,
  VacationRequestResponse,
} from '../types/schemas/requests.schema';
import { useSupervisorDepartmentService } from '../services/supervisor/supervisorDepartment.service';
import { useSupervisorRequestService } from '../services/supervisor/supervisorRequest.service';

export const useSupervisorStore = defineStore('supervisor', {
  state: () => ({
    departments: [] as DepartmentResponse[],
    nurseAssignments: [] as NurseDepartmentResponse[],
    shifts: [] as ShiftResponse[],
    shiftChangeRequests: [] as ShiftChangeResponse[],
    vacationRequests: [] as VacationRequestResponse[],
    // Optional: You could add general loading/error states here if you want to
    // manage them globally across the store, not just per component.
    // isLoading: false as boolean,
    // errorMessage: null as string | null,
  }),

  actions: {
    // ===================== DEPARTMENT ACTIONS =====================
    async getAllMyDepartments() {
      try {
        const data = await useSupervisorDepartmentService.getAllMyDepartments();
        this.departments = data;
      } catch (error: any) {
        console.error('Error fetching departments:', error);
        throw error; // Re-throw to allow component to catch and display
      }
    },

    async getMyDepartment(departmentId: number) {
      try {
        const department = await useSupervisorDepartmentService.getMyDepartment(
          departmentId,
        );
        // If you're fetching a single department that might already be in 'departments',
        // you should update it or add if not found.
        const index = this.departments.findIndex((d) => d.id === department.id);
        if (index !== -1) {
          this.departments[index] = department;
        } else {
          this.departments.push(department);
        }
      } catch (error: any) {
        console.error(
          `Error fetching department with ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    // ===================== NURSE ACTIONS =====================
    async getDepartmentNurses(departmentId: number) {
      try {
        this.nurseAssignments =
          await useSupervisorDepartmentService.getDepartmentNurses(
            departmentId,
          );
      } catch (error: any) {
        console.error(
          `Error fetching nurses for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async addNurseToDepartment(
      departmentId: number,
      request: AssignNurseRequest,
    ) {
      try {
        const newAssignment =
          await useSupervisorDepartmentService.addNurseToDepartment(
            departmentId,
            request,
          );
        this.nurseAssignments.push(newAssignment);
      } catch (error: any) {
        console.error(
          `Error adding nurse with ID ${request.nurseId} to department ID ${request.departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async removeNurseFromDepartment(departmentId: number, nurseId: number) {
      try {
        await useSupervisorDepartmentService.removeNurseFromDepartment(
          departmentId,
          nurseId,
        );
        // Corrected logic: Filter out the assignment where BOTH nurseId and departmentId match
        this.nurseAssignments = this.nurseAssignments.filter(
          (assign) =>
            !(
              assign?.nurse?.id === nurseId &&
              assign?.department?.id === departmentId
            ),
        );
      } catch (error: any) {
        console.error(
          `Error removing nurse with ID ${nurseId} from department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    // ===================== SHIFT ACTIONS =====================
    async getDepartmentShifts(departmentId: number) {
      try {
        this.shifts = await useSupervisorDepartmentService.getDepartmentShifts(
          departmentId,
        );
      } catch (error: any) {
        console.error(
          `Error fetching shifts for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async createShift(departmentId: number, request: CreateShiftRequest) {
      try {
        const newShift = await useSupervisorDepartmentService.createShift(
          departmentId,
          request,
        );
        this.shifts.push(newShift);
      } catch (error: any) {
        console.error(
          `Error creating shift for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async updateShift(
      departmentId: number,
      shiftId: number,
      request: UpdateShiftRequest,
    ) {
      try {
        const updatedShift = await useSupervisorDepartmentService.updateShift(
          departmentId,
          shiftId,
          request,
        );
        const shiftIndex = this.shifts.findIndex(
          (shift) => shift.id === shiftId,
        );

        if (shiftIndex !== -1) {
          this.shifts[shiftIndex] = updatedShift;
        }
      } catch (error: any) {
        console.error(
          `Error updating shift with ID ${shiftId} for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async cancelShift(departmentId: number, shiftId: number) {
      try {
        await useSupervisorDepartmentService.cancelShift(departmentId, shiftId);
        this.shifts = this.shifts.filter((shift) => shift.id !== shiftId);
      } catch (error: any) {
        console.error(
          `Error canceling shift with ID ${shiftId} for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    // ===================== VACATION REQUEST ACTIONS =====================
    async getPendingVacationRequests(departmentId: number) {
      try {
        this.vacationRequests =
          await useSupervisorRequestService.getPendingVacationRequests(
            departmentId,
          );
      } catch (error: any) {
        console.error(
          `Error fetching pending vacation requests for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async getAllVacationRequests(departmentId: number) {
      try {
        this.vacationRequests =
          await useSupervisorRequestService.getAllVacationRequests(
            departmentId,
          );
      } catch (error: any) {
        console.error(
          `Error fetching all vacation requests for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async approveVacationRequest(
      departmentId: number,
      requestId: number,
      request: UpdateVacationRequest,
    ) {
      try {
        const updatedRequest =
          await useSupervisorRequestService.approveVacationRequest(
            departmentId,
            requestId,
            request,
          );
        const requestIndex = this.vacationRequests.findIndex(
          (req) => req.id === requestId,
        );

        if (requestIndex !== -1) {
          this.vacationRequests[requestIndex] = updatedRequest;
        }
      } catch (error: any) {
        console.error(
          `Error approving vacation request with ID ${requestId}:`,
          error,
        );
        throw error;
      }
    },

    async rejectVacationRequest(
      departmentId: number,
      requestId: number,
      request: UpdateVacationRequest,
    ) {
      try {
        const updatedRequest =
          await useSupervisorRequestService.rejectVacationRequest(
            departmentId,
            requestId,
            request,
          );
        const requestIndex = this.vacationRequests.findIndex(
          (req) => req.id === requestId,
        );

        if (requestIndex !== -1) {
          this.vacationRequests[requestIndex] = updatedRequest;
        }
      } catch (error: any) {
        console.error(
          `Error rejecting vacation request with ID ${requestId}:`,
          error,
        );
        throw error;
      }
    },

    // ===================== SHIFT CHANGE REQUEST ACTIONS =====================
    async getPendingShiftChangeRequests(departmentId: number) {
      try {
        this.shiftChangeRequests =
          await useSupervisorRequestService.getPendingShiftChangeRequests(
            departmentId,
          );
      } catch (error: any) {
        console.error(
          `Error fetching pending shift change requests for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async getAllShiftChangeRequests(departmentId: number) {
      try {
        this.shiftChangeRequests =
          await useSupervisorRequestService.getAllShiftChangeRequests(
            departmentId,
          );
      } catch (error: any) {
        console.error(
          `Error fetching all shift change requests for department ID ${departmentId}:`,
          error,
        );
        throw error;
      }
    },

    async approveShiftChangeRequest(
      departmentId: number,
      requestId: number,
      request: UpdateShiftChangeRequest,
    ) {
      try {
        const updatedRequest =
          await useSupervisorRequestService.approveShiftChangeRequest(
            departmentId,
            requestId,
            request,
          );
        const requestIndex = this.shiftChangeRequests.findIndex(
          (req) => req.id === requestId,
        );

        if (requestIndex !== -1) {
          this.shiftChangeRequests[requestIndex] = updatedRequest;
        }
      } catch (error: any) {
        console.error(
          `Error approving shift change request with ID ${requestId}:`,
          error,
        );
        throw error;
      }
    },

    async rejectShiftChangeRequest(
      departmentId: number,
      requestId: number,
      request: UpdateShiftChangeRequest,
    ) {
      try {
        const updatedRequest =
          await useSupervisorRequestService.rejectShiftChangeRequest(
            departmentId,
            requestId,
            request,
          );
        const requestIndex = this.shiftChangeRequests.findIndex(
          (req) => req.id === requestId,
        );

        if (requestIndex !== -1) {
          this.shiftChangeRequests[requestIndex] = updatedRequest;
        }
      } catch (error: any) {
        console.error(
          `Error rejecting shift change request with ID ${requestId}:`,
          error,
        );
        throw error;
      }
    },
  },
});
