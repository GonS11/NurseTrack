import { defineStore } from 'pinia';
import { jwtDecode } from 'jwt-decode';
import { useAuthService } from '../services/shared/auth.service';
import { UserRole } from '../types/enums/user-role.enum';
import type {
  AuthenticationResponse,
  DecodedToken,
  RegisterRequest,
} from '../types/schemas/auth.schema';
import api from '../api/axios';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // Initialize token from localStorage if it exists
    token: localStorage.getItem('authToken') || null,
    // user will be hydrated by the 'initializeAuth' action
    user: null as DecodedToken | null,
  }),

  actions: {
    async initializeAuth() {
      const storedToken = localStorage.getItem('authToken');
      if (storedToken) {
        try {
          const decoded = jwtDecode<DecodedToken>(storedToken);

          const currentTime = Date.now() / 1000;
          if (decoded.exp < currentTime) {
            console.warn('Stored JWT token is expired. Logging out.');
            this.logout();
            return;
          }

          this.token = storedToken;
          this.user = decoded;
          api.defaults.headers.common.Authorization = `Bearer ${storedToken}`;
          console.log('Authentication state rehydrated from localStorage.');
        } catch (e) {
          console.error('Failed to decode or validate stored token:', e);
          this.logout();
        }
      } else {
        this.logout();
      }
    },

    async login(username: string, password: string) {
      try {
        const response = await useAuthService.authenticate({
          username,
          password,
        });

        this.token = response.token;
        localStorage.setItem('authToken', response.token);

        api.defaults.headers.common.Authorization = `Bearer ${response.token}`;

        this.user = jwtDecode<DecodedToken>(response.token);
        console.log('Authenticated user: ', this.user);

        return response;
      } catch (error: any) {
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
      } catch (error: any) {
        console.error('Register error: ', error);

        throw error;
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('authToken');
      delete api.defaults.headers.common.Authorization;
    },

    async getCurrentUser() {
      if (!this.token) {
        this.logout();
        throw new Error('No hay token de autenticación');
      }
      try {
        const userData = jwtDecode<DecodedToken>(this.token);
        const currentTime = Date.now() / 1000;
        if (userData.exp < currentTime) {
          console.warn('JWT token is expired in getCurrentUser. Logging out.');
          this.logout();
          throw new Error('Token expirado');
        }
        this.user = userData;
      } catch (e) {
        console.error('Token inválido o expirado:', e);
        this.logout();
        throw e;
      }
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token && !!state.user,
    isAdmin: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.ADMIN) || false,
    isSupervisor: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.SUPERVISOR) ||
      false,
    isNurse: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.NURSE) || false,
    currentUser: (state) => state.user,
  },
});
