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
      page?: number,
      size?: number,
      sortBy?: string,
    ) {
      try {
        this.notifications = await useNotificationService.getAllNotifications(
          userId,
          page,
          size,
          sortBy,
        );
      } catch (error: any) {
        console.error('Error loading notifications for user', userId, error);
        throw error;
      }
    },

    async getNotificationById(userId: number, notificationId: number) {
      try {
        const notification = await useNotificationService.getNotificationById(
          userId,
          notificationId,
        );
        const notificationIndex = this.notifications.content.findIndex(
          (n) => n.id === notificationId,
        );
        if (notificationIndex !== -1) {
          this.notifications.content[notificationIndex] = notification;
        } else {
          this.notifications.content.push(notification);
        }
      } catch (error: any) {
        console.error(
          `Error fetching notification with ID ${notificationId}:`,
          error,
        );
        throw error;
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
        this.notifications.content.unshift(newNotification);
      } catch (error: any) {
        console.error(
          `Error creating notification for user ID ${userId}:`,
          error,
        );
        throw error;
      }
    },

    async markAsRead(userId: number, notificationId: number) {
      try {
        await useNotificationService.markAsRead(userId, notificationId);
        const notification = this.notifications.content.find(
          (n) => n.id === notificationId,
        );
        if (notification) {
          notification.isRead = true;
        }
      } catch (error: any) {
        console.error(
          `Error marking notification with ID ${notificationId} as read:`,
          error,
        );
        throw error;
      }
    },

    async deleteNotification(userId: number, notificationId: number) {
      try {
        await useNotificationService.deleteNotification(userId, notificationId);
        this.notifications.content = this.notifications.content.filter(
          (n) => n.id !== notificationId,
        );
      } catch (error: any) {
        console.error(
          `Error deleting notification with ID ${notificationId}:`,
          error,
        );
        throw error;
      }
    },

    async markAllAsRead(userId: number) {
      try {
        const unreadIds = this.notifications.content
          .filter((n) => !n.isRead)
          .map((n) => n.id);

        if (unreadIds.length === 0) {
          return;
        }
        for (const id of unreadIds) {
          await useNotificationService.markAsRead(userId, id);
          const notification = this.notifications.content.find(
            (n) => n.id === id,
          );
          if (notification) {
            notification.isRead = true;
          }
        }
      } catch (error: any) {
        console.error('Error marking all notifications as read:', error);
        throw error;
      }
    },
  },
  getters: {
    unreadNotifications: (state) =>
      state.notifications.content.filter((notification) => !notification.isRead)
        .length,
  },
});
