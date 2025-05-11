import { defineStore } from 'pinia';
import type {
  CreateNotificationRequest,
  NotificationResponse,
} from '../types/schemas/notification.schema';
import { useNotificationService } from '../services/shared/notification.service';

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: [] as NotificationResponse[],
  }),

  actions: {
    async fetchNotifications(userId: number) {
      try {
        this.notifications = await useNotificationService.getNotifications(
          userId,
        );
      } catch (error) {
        console.error(
          `Error fetching notifications for user ID ${userId}:`,
          error,
        );
      }
    },

    async fetchNotificationById(userId: number, notificationId: number) {
      try {
        const notification = await useNotificationService.getNotification(
          userId,
          notificationId,
        );
        const notificationIndex = this.notifications.findIndex(
          (notification) => notification.id === notificationId,
        );

        if (notificationIndex !== -1) {
          this.notifications[notificationIndex] = notification;
        } else {
          this.notifications.push(notification);
        }
      } catch (error) {
        console.error(
          `Error fetching notification with ID ${notificationId} for user ID ${userId}:`,
          error,
        );
      }
    },

    async createNotification(
      userId: number,
      request: CreateNotificationRequest,
    ) {
      try {
        const newNotification = await useNotificationService.createNotification(
          userId,
          request,
        );
        this.notifications.push(newNotification);
      } catch (error) {
        console.error(
          `Error creating notification for user ID ${userId}:`,
          error,
        );
      }
    },

    async markAsRead(userId: number, notificationId: number) {
      try {
        await useNotificationService.markAsRead(userId, notificationId);
        const notificationIndex = this.notifications.findIndex(
          (notification) => notification.id === notificationId,
        );

        if (notificationIndex !== -1) {
          this.notifications[notificationIndex].isRead = true;
        }
      } catch (error) {
        console.error(
          `Error marking notification with ID ${notificationId} as read for user ID ${userId}:`,
          error,
        );
      }
    },

    async deleteNotification(userId: number, notificationId: number) {
      try {
        await useNotificationService.deleteNotification(userId, notificationId);
        this.notifications = this.notifications.filter(
          (notification) => notification.id !== notificationId,
        );
      } catch (error) {
        console.error(
          `Error deleting notification with ID ${notificationId} for user ID ${userId}:`,
          error,
        );
      }
    },
  },
});
