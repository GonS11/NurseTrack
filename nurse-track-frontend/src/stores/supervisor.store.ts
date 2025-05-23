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
  }),

  actions: {
    // ===================== DEPARTMENT ACTIONS =====================
    async getAllMyDepartments() {
      try {
        this.departments =
          await useSupervisorDepartmentService.getAllMyDepartments();
      } catch (error) {
        console.error('Error fetching departments:', error);
      }
    },

    async getMyDepartment(departmentId: number) {
      try {
        const department = await useSupervisorDepartmentService.getMyDepartment(
          departmentId,
        );
        this.departments = [department];
      } catch (error) {
        console.error(
          `Error fetching department with ID ${departmentId}:`,
          error,
        );
      }
    },

    // ===================== NURSE ACTIONS =====================
    async getDepartmentNurses(departmentId: number) {
      try {
        this.nurseAssignments =
          await useSupervisorDepartmentService.getDepartmentNurses(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching nurses for department ID ${departmentId}:`,
          error,
        );
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
      } catch (error) {
        console.error(
          `Error adding nurse with ID ${request.nurseId} to department ID ${request.departmentId}:`,
          error,
        );
      }
    },

    async removeNurseFromDepartment(departmentId: number, nurseId: number) {
      try {
        await useSupervisorDepartmentService.removeNurseFromDepartment(
          departmentId,
          nurseId,
        );
        this.nurseAssignments = this.nurseAssignments.filter(
          (assign) =>
            assign?.nurse?.id !== nurseId ||
            assign?.department?.id !== departmentId,
        );
      } catch (error) {
        console.error(
          `Error removing nurse with ID ${nurseId} from department ID ${departmentId}:`,
          error,
        );
      }
    },

    // ===================== SHIFT ACTIONS =====================
    async getDepartmentShifts(departmentId: number) {
      try {
        this.shifts = await useSupervisorDepartmentService.getDepartmentShifts(
          departmentId,
        );
      } catch (error) {
        console.error(
          `Error fetching shifts for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async createShift(departmentId: number, request: CreateShiftRequest) {
      try {
        const newShift = await useSupervisorDepartmentService.createShift(
          departmentId,
          request,
        );
        this.shifts.push(newShift);
      } catch (error) {
        console.error(
          `Error creating shift for department ID ${departmentId}:`,
          error,
        );
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
      } catch (error) {
        console.error(
          `Error updating shift with ID ${shiftId} for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async cancelShift(departmentId: number, shiftId: number) {
      try {
        await useSupervisorDepartmentService.cancelShift(departmentId, shiftId);
        this.shifts = this.shifts.filter((shift) => shift.id !== shiftId);
      } catch (error) {
        console.error(
          `Error canceling shift with ID ${shiftId} for department ID ${departmentId}:`,
          error,
        );
      }
    },

    // ===================== VACATION REQUEST ACTIONS =====================
    async getPendingVacationRequests(departmentId: number) {
      try {
        this.vacationRequests =
          await useSupervisorRequestService.getPendingVacationRequests(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching pending vacation requests for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async getAllVacationRequests(departmentId: number) {
      try {
        this.vacationRequests =
          await useSupervisorRequestService.getAllVacationRequests(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching all vacation requests for department ID ${departmentId}:`,
          error,
        );
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
          (request) => request.id === requestId,
        );

        if (requestIndex !== -1) {
          this.vacationRequests[requestIndex] = updatedRequest;
        }
      } catch (error) {
        console.error(
          `Error approving vacation request with ID ${requestId}:`,
          error,
        );
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
          (request) => request.id === requestId,
        );

        if (requestIndex !== -1) {
          this.vacationRequests[requestIndex] = updatedRequest;
        }
      } catch (error) {
        console.error(
          `Error rejecting vacation request with ID ${requestId}:`,
          error,
        );
      }
    },

    // ===================== SHIFT CHANGE REQUEST ACTIONS =====================
    async getPendingShiftChangeRequests(departmentId: number) {
      try {
        this.shiftChangeRequests =
          await useSupervisorRequestService.getPendingShiftChangeRequests(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching pending shift change requests for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async getAllShiftChangeRequests(departmentId: number) {
      try {
        this.shiftChangeRequests =
          await useSupervisorRequestService.getAllShiftChangeRequests(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching all shift change requests for department ID ${departmentId}:`,
          error,
        );
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
          (request) => request.id === requestId,
        );

        if (requestIndex !== -1) {
          this.shiftChangeRequests[requestIndex] = updatedRequest;
        }
      } catch (error) {
        console.error(
          `Error approving shift change request with ID ${requestId}:`,
          error,
        );
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
          (request) => request.id === requestId,
        );

        if (requestIndex !== -1) {
          this.shiftChangeRequests[requestIndex] = updatedRequest;
        }
      } catch (error) {
        console.error(
          `Error rejecting shift change request with ID ${requestId}:`,
          error,
        );
      }
    },
  },
});
