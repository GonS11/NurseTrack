import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';

export const useAdminUserService = {
  async getAllUsers(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
  ): Promise<Page<UserResponse>> {
    const response = await api.get<Page<UserResponse>>('/admin/users', {
      params: { page, size, sortBy },
    });

    return response.data;
  },

  async getUserById(userId: number): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/admin/users/${userId}`);

    return response.data;
  },

  async createUser(data: CreateUserRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>('/admin/users', data);

    return response.data;
  },

  async updateUser(
    userId: number,
    data: UpdateUserRequest,
  ): Promise<UserResponse> {
    const response = await api.put<UserResponse>(
      `/admin/users/${userId}`,
      data,
    );

    return response.data;
  },

  async activateUser(userId: number): Promise<void> {
    await api.put(`/admin/users/${userId}/activate`);
  },

  async desactivateUser(userId: number): Promise<void> {
    await api.put(`/admin/users/${userId}/desactivate`);
  },

  async deleteUser(userId: number): Promise<void> {
    await api.delete(`/admin/users/${userId}`);
  },
};
