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
    // New action to initialize authentication state on app load
    async initializeAuth() {
      const storedToken = localStorage.getItem('authToken');
      if (storedToken) {
        try {
          // Attempt to decode the token to get user data
          const decoded = jwtDecode<DecodedToken>(storedToken);

          // Optional: Check if token is expired (based on 'exp' field)
          const currentTime = Date.now() / 1000; // current time in seconds
          if (decoded.exp < currentTime) {
            console.warn('Stored JWT token is expired. Logging out.');
            this.logout();
            return; // Exit if expired
          }

          // If valid and not expired, set the state
          this.token = storedToken;
          this.user = decoded;
          // Set the Authorization header for Axios here as well
          api.defaults.headers.common.Authorization = `Bearer ${storedToken}`;
          console.log('Authentication state rehydrated from localStorage.');
        } catch (e) {
          console.error('Failed to decode or validate stored token:', e);
          this.logout(); // Clear invalid token
        }
      } else {
        // No token in localStorage, ensure state is clear
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

        // Ensure Axios default header is set
        api.defaults.headers.common.Authorization = `Bearer ${response.token}`;

        this.user = jwtDecode<DecodedToken>(response.token);
        console.log('Authenticated user: ', this.user);

        return response;
      } catch (error) {
        console.error('Login error: ', error);
        // It's good that you logout here, but ensure it's not double-logged
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
      localStorage.removeItem('authToken');
      // Ensure Axios default header is removed on logout
      delete api.defaults.headers.common.Authorization;
    },

    // getCurrentUser is now mostly handled by initializeAuth on app load,
    // but useful if you explicitly need to refresh user data from token later.
    // However, for initial load, initializeAuth is more robust.
    async getCurrentUser() {
      if (!this.token) {
        this.logout();
        throw new Error('No hay token de autenticación');
      }
      try {
        const userData = jwtDecode<DecodedToken>(this.token);
        // Optional: Add expiration check here too if this function is called often
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
    isAuthenticated: (state) => !!state.token && !!state.user, // Ensure user object is also present
    isAdmin: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.ADMIN),
    isSupervisor: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.SUPERVISOR),
    isNurse: (state) =>
      state.user?.roles?.some((r) => r.authority === UserRole.NURSE),
    currentUser: (state) => state.user,
  },
});
