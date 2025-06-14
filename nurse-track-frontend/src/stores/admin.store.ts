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
    users: {
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

    unassignedDepartments: [] as DepartmentResponse[],
    unassignedDepartmentsForNurses: [] as DepartmentResponse[],

    error: '' as string,
  }),

  actions: {
    // ==================== USER ACTIONS ====================
    async getAllUsers(page = 0, size = 10, sortBy = 'id') {
      try {
        this.users = await useAdminUserService.getAllUsers(page, size, sortBy);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async createUser(input: CreateUserRequest) {
      try {
        await useAdminUserService.createUser(input);

        await this.getAllUsers(this.users.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async updateUser(id: number, input: UpdateUserRequest) {
      try {
        await useAdminUserService.updateUser(id, input);

        await this.getAllUsers(this.users.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async deleteUser(id: number) {
      try {
        await useAdminUserService.deleteUser(id);

        await this.getAllUsers(this.users.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async activateUser(id: number) {
      try {
        await useAdminUserService.activateUser(id);

        const user = this.users.content.find((user) => user.id === id);

        if (user) user.isActive = true;
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async desactivateUser(id: number) {
      try {
        await useAdminUserService.desactivateUser(id);

        const user = this.users.content.find((user) => user.id === id);

        if (user) user.isActive = false;
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // ==================== DEPARTMENT ACTIONS ====================
    async getAllDepartments(page = 0, size = 10, sortBy = 'id') {
      try {
        this.departments = await useAdminDepartmentService.getAllDepartments(
          page,
          size,
          sortBy,
        );
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // Para dropdowns, NO sobrescribas departments.content
    async getUnassignedDepartmentsForSupervisor() {
      try {
        this.unassignedDepartments =
          await useAdminAssignmentService.getUnassignedDepartmentsForSupervisor();
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async getAllActiveDepartments() {
      try {
        this.unassignedDepartments =
          await useAdminDepartmentService.getAllActiveDepartments();
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async getUnassignedDepartmentsForNurses() {
      try {
        this.unassignedDepartmentsForNurses =
          await useAdminAssignmentService.getUnassignedDepartmentsForNurses();
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
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
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // DEPARTMENTS
    async createDepartment(department: CreateDepartmentRequest) {
      try {
        await useAdminDepartmentService.createDepartment(department);
        await this.getAllDepartments(this.departments.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async updateDepartment(
      departmentId: number,
      department: UpdateDepartmentRequest,
    ) {
      try {
        await useAdminDepartmentService.updateDepartment(
          departmentId,
          department,
        );

        await this.getAllDepartments(this.departments.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async deleteDepartment(departmentId: number) {
      try {
        await useAdminDepartmentService.deleteDepartment(departmentId);
        await this.getAllDepartments(this.departments.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
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
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // SUPERVISOR ASSIGNMENTS
    async assignSupervisorToDepartment(request: AssignSupervisorRequest) {
      try {
        await useAdminAssignmentService.assignSupervisorToDepartment(request);
        await this.getAllSupervisorAssignments(
          this.supervisorAssignments.number,
        );
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async removeSupervisorFromDepartment(departmentId: number) {
      try {
        await useAdminAssignmentService.removeSupervisorFromDepartment(
          departmentId,
        );
        await this.getAllSupervisorAssignments(
          this.supervisorAssignments.number,
        );
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    // ==================== NURSE ASSIGNMENT ACTIONS ====================
    async getAllNurseAssignments(page = 0, size = 10, sortBy = 'id') {
      try {
        this.nurseAssignments =
          await useAdminAssignmentService.getAllNurseAssignments(
            page,
            size,
            sortBy,
          );
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async getAllNursesByDepartment(departmentId: number) {
      try {
        const assignments =
          await useAdminAssignmentService.getAllNursesByDepartment(
            departmentId,
          );

        this.nurseAssignments.content = assignments;
        this.nurseAssignments.totalElements = assignments.length;
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async assignNurseToDepartment(request: AssignNurseRequest) {
      try {
        await useAdminAssignmentService.assignNurseToDepartment(request);
        await this.getAllNurseAssignments(this.nurseAssignments.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },

    async removeNurseFromDepartment(departmentId: number, nurseId: number) {
      try {
        await useAdminAssignmentService.removeNurseFromDepartment(
          departmentId,
          nurseId,
        );
        await this.getAllNurseAssignments(this.nurseAssignments.number);
      } catch (e: any) {
        this.error = e.response?.data?.message || e.message;
        throw e;
      }
    },
  },
});
