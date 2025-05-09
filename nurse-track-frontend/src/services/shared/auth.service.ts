import api from '../../api/axios';
import type {
  CurrentUserResponse,
  LoginRequest,
  LoginResponse,
} from '../../types/schemas/auth.schema';

export const useAuthService = {
  // Login the user
  async login(data: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/login', data);
    return response.data;
  },

  // Get the current logged-in user's details
  async getCurrentUser(): Promise<CurrentUserResponse> {
    const response = await api.get<CurrentUserResponse>('/auth/me');
    return response.data;
  },

  // Logout the user
  async logout(): Promise<void> {
    await api.post('/auth/logout');
  },
};
