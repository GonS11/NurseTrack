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

export const useAdminStore = defineStore('admin', {
  state: () => ({
    users: [] as UserResponse[],
    departments: [] as DepartmentResponse[],
    nurseAssignments: [] as NurseDepartmentResponse[],
    supervisorAssignments: [] as SupervisorDepartmentResponse[],
  }),

  actions: {
    // ==================== USER ACTIONS ====================
    async fetchUsers() {
      try {
        this.users = await useAdminUserService.getUsers();
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },

    async fetchUserById(userId: number) {
      try {
        const user = await useAdminUserService.getUserById(userId);
        const userIndex = this.users.findIndex((user) => user.id === userId);

        if (userIndex !== -1) {
          this.users[userIndex] = user;
        } else {
          this.users.push(user);
        }
      } catch (error) {
        console.error(`Error fetching user with ID ${userId}:`, error);
      }
    },

    async createUser(user: CreateUserRequest) {
      try {
        const newUser = await useAdminUserService.createUser(user);
        this.users.push(newUser);
      } catch (error) {
        console.error('Error creating user:', error);
      }
    },

    async updateUser(userId: number, user: UpdateUserRequest) {
      try {
        const updatedUser = await useAdminUserService.updateUser(userId, user);
        const oldUserIndex = this.users.findIndex((user) => user.id === userId);

        if (oldUserIndex !== -1) {
          this.users[oldUserIndex] = updatedUser;
        }
      } catch (error) {
        console.error(`Error updating user with ID ${userId}:`, error);
      }
    },

    async activeUser(userId: number) {
      try {
        await useAdminUserService.activateUser(userId);
        const userIndex = this.users.findIndex((user) => user.id === userId);

        if (userIndex !== -1) {
          this.users[userIndex].isActive = true;
        }
      } catch (error) {
        console.error(`Error activating user with ID ${userId}:`, error);
      }
    },

    async deactivateUser(userId: number) {
      try {
        await useAdminUserService.deactivateUser(userId);
        const userIndex = this.users.findIndex((user) => user.id === userId);

        if (userIndex !== -1) {
          this.users[userIndex].isActive = false;
        }
      } catch (error) {
        console.error(`Error deactivating user with ID ${userId}:`, error);
      }
    },

    async deleteUser(userId: number) {
      try {
        await useAdminUserService.deleteUser(userId);
        this.users = this.users.filter((user) => user.id !== userId);
      } catch (error) {
        console.error(`Error deleting user with ID ${userId}:`, error);
      }
    },

    // ==================== DEPARTMENT ACTIONS ====================
    async fetchDepartments() {
      try {
        this.departments = await useAdminDepartmentService.getAllDepartments();
      } catch (error) {
        console.error('Error fetching departments:', error);
      }
    },

    async fetchActiveDepartments() {
      try {
        this.departments =
          await useAdminDepartmentService.getAllActiveDepartments();
      } catch (error) {
        console.error('Error fetching active departments:', error);
      }
    },

    async fetchDepartmentById(departmentId: number) {
      try {
        const department = await useAdminDepartmentService.getDepartmentById(
          departmentId,
        );
        const departmentIndex = this.departments.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments[departmentIndex] = department;
        } else {
          this.departments.push(department);
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
        this.departments.push(newDepartment);
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
        const oldDepartmentIndex = this.departments.findIndex(
          (department) => department.id === departmentId,
        );

        if (oldDepartmentIndex !== -1) {
          this.departments[oldDepartmentIndex] = updatedDepartment;
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
        const departmentIndex = this.departments.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments[departmentIndex].isActive = true;
        }
      } catch (error) {
        console.error(
          `Error activating department with ID ${departmentId}:`,
          error,
        );
      }
    },

    async deactivateDepartment(departmentId: number) {
      try {
        await useAdminDepartmentService.deactivateDepartment(departmentId);
        const departmentIndex = this.departments.findIndex(
          (department) => department.id === departmentId,
        );

        if (departmentIndex !== -1) {
          this.departments[departmentIndex].isActive = false;
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
        this.departments = this.departments.filter(
          (department) => department.id !== departmentId,
        );
      } catch (error) {
        console.error(
          `Error deleting department with ID ${departmentId}:`,
          error,
        );
      }
    },

    // ==================== ASSIGNMENT ACTIONS ====================
    async fetchSupervisorByDepartment(departmentId: number) {
      try {
        const supervisorAssignment =
          await useAdminAssignmentService.getDepartmentSupervisor(departmentId);
        const existingIndex = this.supervisorAssignments.findIndex(
          (assignment) => assignment.department.id === departmentId,
        );

        if (existingIndex !== -1) {
          this.supervisorAssignments[existingIndex] = supervisorAssignment;
        } else {
          this.supervisorAssignments.push(supervisorAssignment);
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
        const newAssignment = await useAdminAssignmentService.assignSupervisor(
          request,
        );
        const existingIndex = this.supervisorAssignments.findIndex(
          (assignment) => assignment.department.id === request.departmentId,
        );

        if (existingIndex !== -1) {
          this.supervisorAssignments[existingIndex] = newAssignment;
        } else {
          this.supervisorAssignments.push(newAssignment);
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
        this.supervisorAssignments = this.supervisorAssignments.filter(
          (assignment) => assignment.department.id !== departmentId,
        );
      } catch (error) {
        console.error(
          `Error removing supervisor from department ID ${departmentId}:`,
          error,
        );
      }
    },

    async fetchNursesByDepartment(departmentId: number) {
      try {
        this.nurseAssignments =
          await useAdminAssignmentService.getNursesByDepartment(departmentId);
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
        this.nurseAssignments.push(newAssignment);
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
        this.nurseAssignments = this.nurseAssignments.filter(
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
