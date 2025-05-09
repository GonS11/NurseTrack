import api from '../../api/axios';
import type {
  CreateNotificationRequest,
  NotificationResponse,
} from '../../types/schemas/notification.schema';

export const useNotificationService = {
  // Get all notifications for a user
  async getNotifications(userId: number): Promise<NotificationResponse[]> {
    const response = await api.get<NotificationResponse[]>(
      `/users/${userId}/notifications`,
    );
    return response.data;
  },

  // Get a specific notification by ID
  async getNotification(
    userId: number,
    notificationId: number,
  ): Promise<NotificationResponse> {
    const response = await api.get<NotificationResponse>(
      `/users/${userId}/notifications/${notificationId}`,
    );
    return response.data;
  },

  // Create a new notification
  async createNotification(
    userId: number,
    data: CreateNotificationRequest,
  ): Promise<NotificationResponse> {
    const response = await api.post<NotificationResponse>(
      `/users/${userId}/notifications`,
      data,
    );
    return response.data;
  },

  // Mark a notification as read
  async markAsRead(userId: number, notificationId: number): Promise<void> {
    await api.put(`/users/${userId}/notifications/${notificationId}/read`);
  },

  // Delete a notification
  async deleteNotification(
    userId: number,
    notificationId: number,
  ): Promise<void> {
    await api.delete(`/users/${userId}/notifications/${notificationId}`);
  },
};
