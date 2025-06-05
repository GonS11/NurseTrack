import { z } from 'zod';
import { validation } from '../validation';

export const AuthSchemas = {
  authenticationRequest: z
    .object({
      username: validation.requiredString(3, 50),
      password: validation.password(),
    })
    .strict(),

  authenticationResponse: z.object({
    token: validation.requiredString(),
  }),

  registerRequest: z
    .object({
      firstname: validation.requiredString(1, 50),
      lastname: validation.requiredString(1, 50),
      username: validation.username(),
      email: validation.email(),
      password: validation.password(),
      licenseNumber: validation.optionalLicenseNumber(),
    })
    .strict(),

  decodedToken: z.object({
    roles: z.array(
      z.object({
        authority: validation.userRole(),
      }),
    ),
    id: validation.requiredId(),
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

export type AuthenticationRequest = z.infer<
  typeof AuthSchemas.authenticationRequest
>;
export type AuthenticationResponse = z.infer<
  typeof AuthSchemas.authenticationResponse
>;
export type RegisterRequest = z.infer<typeof AuthSchemas.registerRequest>;

export type DecodedToken = z.infer<typeof AuthSchemas.decodedToken>;
