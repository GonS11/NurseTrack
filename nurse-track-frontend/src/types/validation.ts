import { z } from 'zod';
import { UserRole } from './enums/user-role.enum';
import { ShiftStatus } from './enums/shift-status.enum';
import { ShiftType } from './enums/shift-types.enum';
import { RequestStatus } from './enums/status.enum';
import { NotificationType } from './enums/notification-type.enum';

export const validation = {
  //RequiredId
  requiredId: () => {
    return z.number().positive();
  },

  //OptionalId
  optionalId: () => {
    return z.number().positive().optional();
  },

  // Validador para string obligatorios
  requiredString: (min = 1, max = 255) => {
    return z
      .string()
      .min(min, `Should have at least ${min} characters`)
      .max(max, `Cannot exceed ${max} characters`)
      .trim();
  },

  // Validador para string opcionales
  optionalString: (min = 1, max = 255) => {
    return z
      .string()
      .min(min, `Should have at least ${min} characters`)
      .max(max, `Cannot exceed ${max} characters`)
      .trim()
      .optional();
  },

  // Username
  username: () => {
    return z
      .string()
      .min(3, 'Username must be at least 3 characters')
      .max(50, 'Username cannot exceed 50 characters')
      .regex(
        /^[a-zA-Z0-9_]+$/,
        'Username can only contain letters, numbers and underscores',
      )
      .trim();
  },

  // Validador email
  email: () => {
    return z.string().email('Invalid email').max(100, 'Email too long').trim();
  },

  // Validador password
  password: () => {
    return z
      .string()
      .min(8, 'The password should have at least 8 characters')
      .max(255, 'The password cannot exceed 255 characters');
  },

  optionalPassword: () => {
    return z
      .string()
      .min(8, 'The password should have at least 8 characters')
      .max(255, 'The password cannot exceed 255 characters')
      .optional()
      .or(z.literal(''));
  },

  // License number
  licenseNumber: () => {
    return z
      .string()
      .max(50, 'License number must not exceed 50 characters')
      .regex(
        /^([A-Z]-)?\d{4,8}$/,
        'Invalid license number format. Expected pattern: A-1234 or 123456',
      )
      .trim();
  },

  optionalLicenseNumber: () => {
    return z.preprocess(
      (val) => (typeof val === 'string' && val.trim() === '' ? undefined : val),
      z
        .string()
        .trim()
        .max(50, 'License number must not exceed 50 characters')
        .regex(
          /^([A-Z]-)?\d{4,8}$/,
          'Invalid license number format. Expected pattern: A-1234 or 123456',
        )
        .optional(),
    );
  },

  // Validador para números positivos
  positiveNumber: () => {
    return z.number().positive('Should be a positive number');
  },

  positiveNumberOptional: () => {
    return z.number().positive('Should be a positive number').optional();
  },

  //ShiftDate
  date: () => {
    return z.string().refine(
      (date) => {
        if (!date) return false;

        const inputDate = new Date(date);
        const today = new Date(new Date().toISOString().split('T')[0]);

        return inputDate >= today;
      },
      { message: 'Date must be today or in the future' },
    );
  },

  // Date time
  dateTime: () => {
    return z.string().datetime();
  },

  // Date time opcional
  optionalDateTime: () => {
    return z.string().datetime().optional();
  },

  optionalBoolean: () => {
    return z.boolean().optional();
  },

  // Roles
  userRole: () => {
    return z.nativeEnum(UserRole, {
      errorMap: () => ({ message: 'Invalid user role' }),
    });
  },

  // Shift Status
  shiftStatus: () => {
    return z.nativeEnum(ShiftStatus, {
      errorMap: () => ({ message: 'Invalid shift status' }),
    });
  },

  // Shift Type
  shiftTypes: () => {
    return z.nativeEnum(ShiftType, {
      errorMap: () => ({ message: 'Invalid shift type' }),
    });
  },

  // Rquest Status
  requestStatus: () => {
    return z.nativeEnum(RequestStatus, {
      errorMap: () => ({ message: 'Invalid request status' }),
    });
  },

  // Notification type
  notificationType: () => {
    return z.nativeEnum(NotificationType, {
      errorMap: () => ({ message: 'Invalid notification type' }),
    });
  },

  // Convertidor de esquema Zod a Vuelidate
  /*toVuelidate: (schema: z.ZodTypeAny) => {
      return toTypedSchema(schema);
    }*/
  toVuelidate: (schema: z.ZodTypeAny) => {
    return {
      $validator: (value: unknown) => schema.safeParse(value).success,
      $message: (ctx: any) => {
        const result = schema.safeParse(ctx.$model);

        if (!result.success) {
          return result.error.errors[0]?.message || 'Invalid value';
        }

        return '';
      },
    };
  },
};

export type Validation = typeof validation;
