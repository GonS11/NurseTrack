import { defineStore } from 'pinia';
import { jwtDecode } from 'jwt-decode';
import { useAuthService } from '../services/shared/auth.service';
import { UserRole } from '../types/enums/user-role.enum';
import type {
  AuthenticationRequest,
  AuthenticationResponse,
  DecodedToken,
  RegisterRequest,
} from '../types/schemas/auth.schema';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as DecodedToken | null,
    token: localStorage.getItem('authToken') || null,
  }),

  actions: {
    async login(
      username: string,
      password: string,
    ): Promise<AuthenticationResponse> {
      try {
        const response = await useAuthService.authenticate({
          username,
          password,
        });

        this.token = response.token;
        localStorage.setItem('authToken', response.token);

        this.user = jwtDecode<DecodedToken>(response.token);
        console.log('Authenticated user: ', this.user);

        return response;
      } catch (error) {
        console.error('Login error: ', error);
        this.logout();
        throw error;
      }
    },

    async register(request: RegisterRequest): Promise<AuthenticationResponse> {
      try {
        const response = await useAuthService.register(request);

        console.log('Correct register: ', response);
        return response;
      } catch (error) {
        console.error('Register error: ', error);
        throw error;
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('authToken'); // Key corregida
      // Limpiar cualquier otra data relacionada
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role?.includes(UserRole.ADMIN) || false,
    isSupervisor: (state) =>
      state.user?.role?.includes(UserRole.SUPERVISOR) || false,
    isNurse: (state) => state.user?.role?.includes(UserRole.NURSE) || false,
    currentUser: (state) => state.user,
  },
});
