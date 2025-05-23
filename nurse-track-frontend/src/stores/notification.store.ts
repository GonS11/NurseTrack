import { defineStore } from 'pinia';
import type {
  CreateNotificationRequest,
  NotificationResponse,
} from '../types/schemas/notification.schema';
import { useNotificationService } from '../services/shared/notification.service';
import type { Page } from '../types/common';

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: {
      content: [],
      totalPages: 0,
      totalElements: 0,
      number: 0,
      size: 10,
    } as Page<NotificationResponse>,
  }),

  actions: {
    async getAllNotifications(
      userId: number,
      page: number = 0,
      size: number = 10,
      sortBy: string = 'createdAt',
    ) {
      try {
        this.notifications = await useNotificationService.getAllNotifications(
          userId,
          page,
          size,
          sortBy,
        );
      } catch (error) {
        console.error(
          `Error fetching notifications for user ID ${userId}:`,
          error,
        );
      }
    },

    async getNotificationById(userId: number, notificationId: number) {
      try {
        const notification = await useNotificationService.getNotificationById(
          userId,
          notificationId,
        );

        const notificationIndex = this.notifications.content.findIndex(
          (notification) => notification.id === notificationId,
        );

        if (notificationIndex !== -1) {
          this.notifications.content[notificationIndex] = notification;
        } else {
          this.notifications.content.push(notification);
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

        this.notifications.content.push(newNotification);
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
        const notificationIndex = this.notifications.content.findIndex(
          (notification) => notification.id === notificationId,
        );

        if (notificationIndex !== -1) {
          this.notifications.content[notificationIndex].isRead = true;
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

        this.notifications.content = this.notifications.content.filter(
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
