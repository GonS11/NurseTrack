import api from '../../api/axios';
import type {
  AuthenticationRequest,
  AuthenticationResponse,
  RegisterRequest,
} from '../../types/schemas/auth.schema';

export const useAuthService = {
  async register(request: RegisterRequest): Promise<AuthenticationResponse> {
    const response = await api.post<AuthenticationResponse>(
      '/auth/register',
      request,
    );

    return response.data;
  },

  async authenticate(
    request: AuthenticationRequest,
  ): Promise<AuthenticationResponse> {
    const response = await api.post<AuthenticationResponse>(
      '/auth/authenticate',
      request,
    );

    return response.data;
  },
};
