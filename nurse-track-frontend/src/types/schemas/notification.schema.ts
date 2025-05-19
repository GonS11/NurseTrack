import { z } from 'zod';
import { validation } from '../validation';
import { NotificationType } from '../enums/notification-type.enum';
import { UserSchemas } from './user.schema';

// Convertir el enum a Zod schema
export const NotificationTypeSchema = z.nativeEnum(NotificationType);

export const NotificationSchemas = {
  create: z
    .object({
      userId: validation.requiredId(),
      type: NotificationTypeSchema,
      title: validation.requiredString(1, 100),
      message: validation.requiredString(1, 1000),
    })
    .strict(),

  response: z.object({
    id: validation.requiredId(),
    user: UserSchemas.simpleResponse,
    type: NotificationTypeSchema,
    title: validation.requiredString(1, 100),
    message: validation.requiredString(1, 1000),
    isRead: z.boolean(),
    createdAt: validation.dateTime(),
  }),
};

// Tipos inferidos
export type CreateNotificationRequest = z.infer<
  typeof NotificationSchemas.create
>;
export type NotificationResponse = z.infer<typeof NotificationSchemas.response>;
