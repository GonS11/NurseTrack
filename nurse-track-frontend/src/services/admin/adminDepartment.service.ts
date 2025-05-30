import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateDepartmentRequest,
  DepartmentResponse,
  UpdateDepartmentRequest,
} from '../../types/schemas/department.schema';

export const useAdminDepartmentService = {
  //==== DEPARTMENTS ====
  async getAllDepartments(
    page = 0,
    size = 10,
    sortBy = 'id',
  ): Promise<Page<DepartmentResponse>> {
    const response = await api.get<Page<DepartmentResponse>>(
      '/admin/departments',
      {
        params: { page, size, sortBy },
      },
    );
    return response.data;
  },

  async getAllActiveDepartments(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/admin/departments/active',
    );
    return response.data;
  },

  async getAllInactiveDepartments(): Promise<DepartmentResponse[]> {
    const response = await api.get<DepartmentResponse[]>(
      '/admin/departments/inactive',
    );
    return response.data;
  },

  async getDepartmentById(departmentId: number): Promise<DepartmentResponse> {
    const response = await api.get<DepartmentResponse>(
      `/admin/departments/${departmentId}`,
    );
    return response.data;
  },

  async createDepartment(
    data: CreateDepartmentRequest,
  ): Promise<DepartmentResponse> {
    const response = await api.post<DepartmentResponse>(
      '/admin/departments',
      data,
    );
    return response.data;
  },

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

  async activateDepartment(departmentId: number): Promise<void> {
    await api.put<void>(`/admin/departments/${departmentId}/activate`);
    // No data to return
  },

  async desactivateDepartment(departmentId: number): Promise<void> {
    await api.put<void>(`/admin/departments/${departmentId}/desactivate`);
    // No data to return
  },

  async deleteDepartment(departmentId: number): Promise<void> {
    await api.delete<void>(`/admin/departments/${departmentId}`);
    // No data to return
  },
};
