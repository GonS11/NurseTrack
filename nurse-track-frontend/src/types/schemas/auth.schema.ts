import { z } from 'zod';
import { validation } from '../validation';

export const AuthSchemas = {
  authenticationRequest: z
    .object({
      username: validation.requiredString(5, 50),
      password: validation.requiredString(8, 40),
    })
    .strict(),

  authenticationResponse: z.object({
    token: validation.requiredString(),
  }),

  registerRequest: z
    .object({
      firstname: validation.requiredString(),
      lastname: validation.requiredString(),
      username: validation.requiredString(),
      email: validation.email(),
      password: validation.password(),
      licenseNumber: validation.licenseNumber(),
    })
    .strict(),

  decodedToken: z.object({
    role: validation.requiredString(),
    username: validation.requiredString(),
    email: validation.requiredString(),
    firstname: validation.requiredString(),
    lastname: validation.requiredString(),
    licenseNumber: validation.requiredString(),
    isActive: z.boolean(),
    exp: z.number(),
    iat: z.number().optional(),
  }),
};

// Tipos inferidos
export type AuthenticationRequest = z.infer<
  typeof AuthSchemas.authenticationRequest
>;
export type AuthenticationResponse = z.infer<
  typeof AuthSchemas.authenticationResponse
>;
export type RegisterRequest = z.infer<typeof AuthSchemas.registerRequest>;

export type DecodedToken = z.infer<typeof AuthSchemas.decodedToken>;
