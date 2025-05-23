import { defineStore } from 'pinia';
import { jwtDecode } from 'jwt-decode';
import { useAuthService } from '../services/shared/auth.service';
import { UserRole } from '../types/enums/user-role.enum';
import type {
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
    },

    async getCurrentUser() {
      if (!this.token) {
        this.logout();
        throw new Error('No hay token de autenticación');
      }
      try {
        // jwtDecode lanzará si el token está expirado o mal formado
        const userData = jwtDecode<DecodedToken>(this.token);
        this.user = userData;
      } catch (e) {
        console.error('Token inválido o expirado:', e);
        this.logout();
        throw e;
      }
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.ADMIN),
    isSupervisor: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.SUPERVISOR),
    isNurse: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.NURSE),
    currentUser: (state) => state.user,
  },
});
