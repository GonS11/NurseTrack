import api from '../../api/axios';
import type { Page } from '../../types/common';
import type {
  CreateNotificationRequest,
  NotificationResponse,
} from '../../types/schemas/notification.schema';

export const useNotificationService = {
  async getAllNotifications(
    userId: number,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'createdAt',
  ): Promise<Page<NotificationResponse>> {
    const response = await api.get<Page<NotificationResponse>>(
      `/users/${userId}/notifications`,
      { params: { page, size, sortBy } },
    );

    return response.data;
  },

  async getNotification(
    userId: number,
    notificationId: number,
  ): Promise<NotificationResponse> {
    const response = await api.get<NotificationResponse>(
      `/users/${userId}/notifications/${notificationId}`,
    );

    return response.data;
  },

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

  async markAsRead(userId: number, notificationId: number): Promise<void> {
    await api.put(`/users/${userId}/notifications/${notificationId}/read`);
  },

  async deleteNotification(
    userId: number,
    notificationId: number,
  ): Promise<void> {
    await api.delete(`/users/${userId}/notifications/${notificationId}`);
  },
};
