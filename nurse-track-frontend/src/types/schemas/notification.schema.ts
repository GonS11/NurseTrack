import { z } from 'zod';
import { validation } from '../validation';
import { UserSchemas } from './user.schema';

export const NotificationSchemas = {
  create: z
    .object({
      userId: validation.requiredId(),
      type: validation.notificationType(),
      title: validation.requiredString(1, 100),
      message: validation.requiredString(1, 1000),
    })
    .strict(),

  response: z.object({
    id: validation.requiredId(),
    user: UserSchemas.simpleResponse,
    type: validation.notificationType(),
    title: validation.requiredString(1, 100),
    message: validation.requiredString(1, 1000),
    isRead: z.boolean(),
    createdAt: validation.dateTime(),
  }),
};

export type CreateNotificationRequest = z.infer<
  typeof NotificationSchemas.create
>;
export type NotificationResponse = z.infer<typeof NotificationSchemas.response>;
