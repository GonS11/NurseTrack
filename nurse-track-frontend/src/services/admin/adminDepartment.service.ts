import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateDepartmentRequest,
  DepartmentResponse,
  UpdateDepartmentRequest,
} from '../../types/schemas/department.schema';

export const useAdminDepartmentService = {
  getAllDepartments: (page = 0, size = 10, sortBy = 'id') =>
    api
      .get<Page<DepartmentResponse>>('/admin/departments', {
        params: { page, size, sortBy },
      })
      .then((res) => res.data),

  getAllActiveDepartments: () =>
    api
      .get<DepartmentResponse[]>('/admin/departments/active')
      .then((res) => res.data),

  getAllInactiveDepartments: () =>
    api
      .get<DepartmentResponse[]>('/admin/departments/inactive')
      .then((res) => res.data),

  getDepartmentById: (departmentId: number) =>
    api
      .get<DepartmentResponse>(`/admin/departments/${departmentId}`)
      .then((res) => res.data),

  createDepartment: (data: CreateDepartmentRequest) =>
    api
      .post<DepartmentResponse>('/admin/departments', data)
      .then((res) => res.data),

  updateDepartment: (departmentId: number, data: UpdateDepartmentRequest) =>
    api
      .put<DepartmentResponse>(`/admin/departments/${departmentId}`, data)
      .then((res) => res.data),

  activateDepartment: (departmentId: number) =>
    api
      .put<void>(`/admin/departments/${departmentId}/activate`)
      .then((res) => res.data),

  desactivateDepartment: (departmentId: number) =>
    api
      .put<void>(`/admin/departments/${departmentId}/desactivate`)
      .then((res) => res.data),

  deleteDepartment: (departmentId: number) =>
    api
      .delete<void>(`/admin/departments/${departmentId}`)
      .then((res) => res.data),
};
