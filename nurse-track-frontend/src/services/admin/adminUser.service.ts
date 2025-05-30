import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';

export const useAdminUserService = {
  //==== USERS ====
  async getAllUsers(
    page = 0,
    size = 10,
    sortBy = 'id',
  ): Promise<Page<UserResponse>> {
    const response = await api.get<Page<UserResponse>>('/admin/users', {
      params: { page, size, sortBy },
    });
    return response.data;
  },

  async getUserById(id: number): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/admin/users/${id}`);
    return response.data;
  },

  async createUser(data: CreateUserRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>('/admin/users', data);
    return response.data;
  },

  async updateUser(id: number, data: UpdateUserRequest): Promise<UserResponse> {
    const response = await api.patch<UserResponse>(`/admin/users/${id}`, data);
    return response.data;
  },

  async deleteUser(id: number): Promise<void> {
    await api.delete<void>(`/admin/users/${id}`);
    // No data to return for a delete operation
  },

  async activateUser(id: number): Promise<UserResponse> {
    const response = await api.patch<UserResponse>(
      `/admin/users/${id}/activate`,
    );
    return response.data;
  },

  async desactivateUser(id: number): Promise<UserResponse> {
    const response = await api.patch<UserResponse>(
      `/admin/users/${id}/desactivate`,
    );
    return response.data;
  },
};
