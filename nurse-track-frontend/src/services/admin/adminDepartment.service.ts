import api from '../../api/axios';
import type {
  CreateDepartmentRequest,
  DepartmentResponse,
  UpdateDepartmentRequest,
} from '../../types/schemas/department.schema';

export const useAdminDepartmentService = {
  // Fetch all departments
  async getAllDepartments(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>('/admin/departments');
    return response.data;
  },

  // Fetch all active departments
  async getAllActiveDepartments(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/admin/departments/active',
    );
    return response.data;
  },

  // Fetch a department by ID
  async getDepartmentById(departmentId: number): Promise<DepartmentResponse> {
    const response = await api.get<DepartmentResponse>(
      `/admin/departments/${departmentId}`,
    );
    return response.data;
  },

  // Create a new department
  async createDepartment(
    data: CreateDepartmentRequest,
  ): Promise<DepartmentResponse> {
    const response = await api.post<DepartmentResponse>(
      '/admin/departments',
      data,
    );
    return response.data;
  },

  // Update an existing department
  async updateDepartment(
    departmentId: number,
    data: UpdateDepartmentRequest,
  ): Promise<DepartmentResponse> {
    const response = await api.put<DepartmentResponse>(
      `/admin/departments/${departmentId}`,
      data,
    );
    return response.data;
  },

  // Delete a department
  async deleteDepartment(departmentId: number): Promise<void> {
    await api.delete(`/admin/departments/${departmentId}`);
  },

  // Deactivate a department
  async deactivateDepartment(departmentId: number): Promise<void> {
    await api.put(`/admin/departments/${departmentId}/desactivate`);
  },

  // Activate a department
  async activateDepartment(departmentId: number): Promise<void> {
    await api.put(`/admin/departments/${departmentId}/activate`);
  },
};
