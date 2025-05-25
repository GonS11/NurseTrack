import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';

export const useAdminUserService = {
  // Return response.data directly for all methods
  getAllUsers: (page = 0, size = 10, sortBy = 'id') =>
    api
      .get<Page<UserResponse>>('/admin/users', {
        params: { page, size, sortBy },
      })
      .then((res) => res.data), // Ensure .data is returned

  getUserById: (id: number) =>
    api.get<UserResponse>(`/admin/users/${id}`).then((res) => res.data),

  createUser: (data: CreateUserRequest) =>
    api.post<UserResponse>('/admin/users', data).then((res) => res.data),

  updateUser: (id: number, data: UpdateUserRequest) =>
    api.patch<UserResponse>(`/admin/users/${id}`, data).then((res) => res.data),

  deleteUser: (id: number) =>
    api.delete<void>(`/admin/users/${id}`).then((res) => res.data),

  activateUser: (id: number) =>
    api
      .patch<UserResponse>(`/admin/users/${id}/activate`)
      .then((res) => res.data),

  desactivateUser: (id: number) =>
    api
      .patch<UserResponse>(`/admin/users/${id}/desactivate`)
      .then((res) => res.data),
};
