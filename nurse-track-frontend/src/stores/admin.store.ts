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
      content: [],
      totalPages: 0,
      totalElements: 0,
      number: 0,
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
  }),

  actions: {
    // ==================== USER ACTIONS ====================
    async getAllUsers(
      page: number = 0,
      size: number = 10,
      sortBy: string = 'id',
    ) {
      try {
        this.users = await useAdminUserService.getAllUsers(page, size, sortBy);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },

    async getUserById(userId: number) {
      try {
        const user = await useAdminUserService.getUserById(userId);
        const userIndex = this.users.content.findIndex(
          (user) => user.id === userId,
        );

        if (userIndex !== -1) {
          this.users.content[userIndex] = user;
        } else {
          this.users.content.push(user);
        }
      } catch (error) {
        console.error(`Error fetching user with ID ${userId}:`, error);
      }
    },

    async createUser(user: CreateUserRequest) {
      try {
        const newUser = await useAdminUserService.createUser(user);

        this.users.content.push(newUser);
      } catch (error) {
        console.error('Error creating user:', error);
      }
    },

    async updateUser(userId: number, user: UpdateUserRequest) {
      try {
        const updatedUser = await useAdminUserService.updateUser(userId, user);
        const oldUserIndex = this.users.content.findIndex(
          (user) => user.id === userId,
        );

        if (oldUserIndex !== -1) {
          this.users.content[oldUserIndex] = updatedUser;
        }
      } catch (error) {
        console.error(`Error updating user with ID ${userId}:`, error);
      }
    },

    async activeUser(userId: number) {
      try {
        await useAdminUserService.activateUser(userId);
        const userIndex = this.users.content.findIndex(
          (user) => user.id === userId,
        );

        if (userIndex !== -1) {
          this.users.content[userIndex].isActive = true;
        }
      } catch (error) {
        console.error(`Error activating user with ID ${userId}:`, error);
      }
    },

    async desactivateUser(userId: number) {
      try {
        await useAdminUserService.desactivateUser(userId);
        const userIndex = this.users.content.findIndex(
          (user) => user.id === userId,
        );

        if (userIndex !== -1) {
          this.users.content[userIndex].isActive = false;
        }
      } catch (error) {
        console.error(`Error deactivating user with ID ${userId}:`, error);
      }
    },

    async deleteUser(userId: number) {
      try {
        await useAdminUserService.deleteUser(userId);
        this.users.content = this.users.content.filter(
          (user) => user.id !== userId,
        );
      } catch (error) {
        console.error(`Error deleting user with ID ${userId}:`, error);
      }
    },

    // ==================== DEPARTMENT ACTIONS ====================
    async getAllDepartments(
      page: number = 0,
      size: number = 10,
      sortBy: string = 'id',
    ) {
      try {
        this.departments = await useAdminDepartmentService.getAllDepartments(
          page,
          size,
          sortBy,
        );
      } catch (error) {
        console.error('Error fetching departments:', error);
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
      page: number = 0,
      size: number = 10,
      sortBy: string = 'id',
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
      page: number = 0,
      size: number = 10,
      sortBy: string = 'id',
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
