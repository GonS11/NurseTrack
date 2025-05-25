import { defineStore } from 'pinia';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../types/schemas/user.schema';
import type {
  CreateDepartmentRequest,
  DepartmentResponse,
  UpdateDepartmentRequest,
} from '../types/schemas/department.schema';
import type {
  AssignNurseRequest,
  AssignSupervisorRequest,
  NurseDepartmentResponse,
  SupervisorDepartmentResponse,
} from '../types/schemas/assignments.schema';
import { useAdminUserService } from '../services/admin/adminUser.service';
import { useAdminDepartmentService } from '../services/admin/adminDepartment.service';
import { useAdminAssignmentService } from '../services/admin/adminAssignment.service';
import type { Page } from '../types/common';

export const useAdminStore = defineStore('admin', {
  state: () => ({
    usersPage: {
      content: [] as UserResponse[],
      number: 0,
      totalPages: 0,
      totalElements: 0,
      size: 10,
    } as Page<UserResponse>,

    departments: {
      content: [],
      totalPages: 0,
      totalElements: 0,
      number: 0,
      size: 10,
    } as Page<DepartmentResponse>,

    nurseAssignments: {
      content: [],
      totalPages: 0,
      totalElements: 0,
      number: 0,
      size: 10,
    } as Page<NurseDepartmentResponse>,

    supervisorAssignments: {
      content: [],
      totalPages: 0,
      totalElements: 0,
      number: 0,
      size: 10,
    } as Page<SupervisorDepartmentResponse>,
    error: '' as string,
  }),

  actions: {
    // ==================== USER ACTIONS ====================
    async getAllUsers(page = 0, size = 10, sortBy = 'id') {
      try {
        const res = await useAdminUserService.getAllUsers(page, size, sortBy);
        this.usersPage = res;
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async createUser(input: CreateUserRequest) {
      try {
        await useAdminUserService.createUser(input);
        // After creating a user, reload the current page to see the new user and maintain pagination integrity
        await this.getAllUsers(this.usersPage.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async updateUser(id: number, input: UpdateUserRequest) {
      try {
        await useAdminUserService.updateUser(id, input);
        // After updating, reload the current page to reflect changes accurately
        await this.getAllUsers(this.usersPage.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async deleteUser(id: number) {
      try {
        await useAdminUserService.deleteUser(id);
        // After deleting, reload the current page. This is important as it might affect page count.
        await this.getAllUsers(this.usersPage.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async activateUser(id: number) {
      try {
        await useAdminUserService.activateUser(id);
        // Directly update the state to reflect the change immediately
        const user = this.usersPage.content.find((u) => u.id === id);
        if (user) {
          user.isActive = true;
        }
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async desactivateUser(id: number) {
      try {
        await useAdminUserService.desactivateUser(id);
        // Directly update the state to reflect the change immediately
        const user = this.usersPage.content.find((u) => u.id === id);

        if (user) user.isActive = false;
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // ==================== DEPARTMENT ACTIONS ====================
    async getAllDepartments(page = 0, size = 10, sortBy = 'id') {
      try {
        const response = await useAdminDepartmentService.getAllDepartments(
          page,
          size,
          sortBy,
        );
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async getAllActiveDepartments() {
      try {
        this.departments.content =
          await useAdminDepartmentService.getAllActiveDepartments();
      } catch (error) {
        console.error('Error fetching active departments:', error);
      }
    },

    async getAllInactiveDepartments() {
      try {
        this.departments.content =
          await useAdminDepartmentService.getAllInactiveDepartments();
      } catch (error) {
        console.error('Error fetching active departments:', error);
      }
    },

    async getDepartmentById(departmentId: number) {
      try {
        const department = await useAdminDepartmentService.getDepartmentById(
          departmentId,
        );
        const departmentIndex = this.departments.content.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments.content[departmentIndex] = department;
        } else {
          this.departments.content.push(department);
        }
      } catch (error) {
        console.error(
          `Error fetching department with ID ${departmentId}:`,
          error,
        );
      }
    },

    async createDepartment(department: CreateDepartmentRequest) {
      try {
        const newDepartment = await useAdminDepartmentService.createDepartment(
          department,
        );
        this.departments.content.push(newDepartment);
      } catch (error) {
        console.error('Error creating department:', error);
      }
    },

    async updateDepartment(
      departmentId: number,
      department: UpdateDepartmentRequest,
    ) {
      try {
        const updatedDepartment =
          await useAdminDepartmentService.updateDepartment(
            departmentId,
            department,
          );
        const oldDepartmentIndex = this.departments.content.findIndex(
          (department) => department.id === departmentId,
        );

        if (oldDepartmentIndex !== -1) {
          this.departments.content[oldDepartmentIndex] = updatedDepartment;
        }
      } catch (error) {
        console.error(
          `Error updating department with ID ${departmentId}:`,
          error,
        );
      }
    },

    async activeDepartment(departmentId: number) {
      try {
        await useAdminDepartmentService.activateDepartment(departmentId);
        const departmentIndex = this.departments.content.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments.content[departmentIndex].isActive = true;
        }
      } catch (error) {
        console.error(
          `Error activating department with ID ${departmentId}:`,
          error,
        );
      }
    },

    async desactivateDepartment(departmentId: number) {
      try {
        await useAdminDepartmentService.desactivateDepartment(departmentId);
        const departmentIndex = this.departments.content.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments.content[departmentIndex].isActive = false;
        }
      } catch (error) {
        console.error(
          `Error deactivating department with ID ${departmentId}:`,
          error,
        );
      }
    },

    async deleteDepartment(departmentId: number) {
      try {
        await useAdminDepartmentService.deleteDepartment(departmentId);
        this.departments.content = this.departments.content.filter(
          (department) => department.id !== departmentId,
        );
      } catch (error) {
        console.error(
          `Error deleting department with ID ${departmentId}:`,
          error,
        );
      }
    },

    // ==================== SUPERVISOR ASSIGNMENT ACTIONS ====================
    async getAllSupervisorAssignments(
      page?: number,
      size?: number,
      sortBy?: string,
    ) {
      try {
        this.supervisorAssignments =
          await useAdminAssignmentService.getAllSupervisorAssignments(
            page,
            size,
            sortBy,
          );
      } catch (error) {
        console.error('Error fetching supervisor asignments:', error);
      }
    },

    async getUnassignedDepartmentsForSupervisor() {
      try {
        this.departments.content =
          await useAdminAssignmentService.getUnassignedDepartmentsForSupervisor();
      } catch (error) {
        console.error(
          'Error fetching unassigned departments for supervisors:',
          error,
        );
      }
    },

    async getSupervisorByDepartment(departmentId: number) {
      try {
        const supervisorAssignment =
          await useAdminAssignmentService.getSupervisorByDepartment(
            departmentId,
          );
        const existingIndex = this.supervisorAssignments.content.findIndex(
          (assignment) => assignment.department.id === departmentId,
        );

        if (existingIndex !== -1) {
          this.supervisorAssignments.content[existingIndex] =
            supervisorAssignment;
        } else {
          this.supervisorAssignments.content.push(supervisorAssignment);
        }
      } catch (error) {
        console.error(
          `Error fetching supervisor for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async assignSupervisorToDepartment(request: AssignSupervisorRequest) {
      try {
        const newAssignment =
          await useAdminAssignmentService.assignSupervisorToDepartment(request);

        const existingIndex = this.supervisorAssignments.content.findIndex(
          (assignment) => assignment.department.id === request.departmentId,
        );

        if (existingIndex !== -1) {
          this.supervisorAssignments.content[existingIndex] = newAssignment;
        } else {
          this.supervisorAssignments.content.push(newAssignment);
        }
      } catch (error) {
        console.error('Error assigning supervisor to department:', error);
      }
    },

    async removeSupervisorFromDepartment(departmentId: number) {
      try {
        await useAdminAssignmentService.removeSupervisorFromDepartment(
          departmentId,
        );

        this.supervisorAssignments.content =
          this.supervisorAssignments.content.filter(
            (assignment) => assignment.department.id !== departmentId,
          );
      } catch (error) {
        console.error(
          `Error removing supervisor from department ID ${departmentId}:`,
          error,
        );
      }
    },

    // ==================== NURSE ASSIGNMENT ACTIONS ====================
    async getAllNurseAssignments(
      page?: number,
      size?: number,
      sortBy?: string,
    ) {
      try {
        this.nurseAssignments =
          await useAdminAssignmentService.getAllNurseAssignments(
            page,
            size,
            sortBy,
          );
      } catch (error) {
        console.error('Error fetching supervisor asignments:', error);
      }
    },

    async getUnassignedDepartmentsForNurses() {
      try {
        this.departments.content =
          await useAdminAssignmentService.getUnassignedDepartmentsForNurses();
      } catch (error) {
        console.error(
          'Error fetching unassigned departments for nurses:',
          error,
        );
      }
    },

    async getAllNursesByDepartment(departmentId: number) {
      try {
        this.nurseAssignments.content =
          await useAdminAssignmentService.getAllNursesByDepartment(
            departmentId,
          );
      } catch (error) {
        console.error(
          `Error fetching nurses for department ID ${departmentId}:`,
          error,
        );
      }
    },

    async assignNurseToDepartment(request: AssignNurseRequest) {
      try {
        const newAssignment =
          await useAdminAssignmentService.assignNurseToDepartment(request);
        this.nurseAssignments.content.push(newAssignment);
      } catch (error) {
        console.error('Error assigning nurse to department:', error);
      }
    },

    async removeNurseFromDepartment(departmentId: number, nurseId: number) {
      try {
        await useAdminAssignmentService.removeNurseFromDepartment(
          departmentId,
          nurseId,
        );
        this.nurseAssignments.content = this.nurseAssignments.content.filter(
          (assignment) =>
            assignment.department.id !== departmentId ||
            assignment.nurse.id !== nurseId,
        );
      } catch (error) {
        console.error(
          `Error removing nurse with ID ${nurseId} from department ID ${departmentId}:`,
          error,
        );
      }
    },
  },
});
