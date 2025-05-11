import { defineStore } from 'pinia';
import type {
  CurrentUserResponse,
  LoginRequest,
  LoginResponse,
} from '../types/schemas/auth.schema';
import { useAuthService } from '../services/shared/auth.service';
import { UserRole } from '../types/enums/user-role.enum';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: null as CurrentUserResponse | null,
  }),

  actions: {
    async login(username: string, password: string) {
      try {
        const data: LoginResponse = await useAuthService.login({
          username,
          password,
        } as LoginRequest);

        this.token = data.token;
        this.user = {
          username: data.username,
          fullName: data.fullName,
          role: data.role,
          email: data.email,
          licenseNumber: data.licenseNumber,
        };

        localStorage.setItem('token', data.token);
      } catch (error) {
        console.error('Login failed: ', error);
        throw error;
      }
    },

    async loadCurrentUser() {
      if (this.token) {
        try {
          this.user = await useAuthService.getCurrentUser();
        } catch (error) {
          console.error('Failed to load current user: ', error);
          this.logout();
        }
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('token');
      useAuthService.logout();
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === UserRole.ADMIN,
    isSupervisor: (state) => state.user?.role === UserRole.SUPERVISOR,
    isNurse: (state) => state.user?.role === UserRole.NURSE,
  },
});
