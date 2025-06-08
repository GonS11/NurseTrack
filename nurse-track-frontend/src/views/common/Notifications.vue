<template>
  <div class="notifications-container">
    <div class="notifications-header">
      <h1>
        My Notifications <span class="badge">{{ unreadCount }}</span>
      </h1>
      <button
        class="mark-all-read"
        @click="markAllNotificationsAsRead"
        :disabled="unreadCount === 0"
      >
        Mark all as read
      </button>
    </div>

    <div v-if="loading" class="loading-spinner">
      <span class="material-icons spin">autorenew</span>
      <p>Loading notifications...</p>
    </div>

    <div v-else>
      <div v-if="notifications.length === 0" class="empty-state">
        <span class="material-icons">notifications_off</span>
        <p>You have no new notifications!</p>
      </div>

      <div class="notifications-list">
        <div
          v-for="notification in notifications"
          :key="notification.id"
          :class="['notification-item', { unread: !notification.isRead }]"
        >
          <div class="notification-icon">
            <span class="material-icons">{{
              getNotificationIcon(notification.type)
            }}</span>
          </div>

          <div class="notification-content">
            <h3>{{ notification.title }}</h3>
            <p>{{ notification.message }}</p>
            <time>{{ formatDate(notification.createdAt) }}</time>
          </div>

          <div class="notification-actions">
            <button
              v-if="!notification.isRead"
              class="mark-read"
              @click="markThisAsRead(notification.id)"
              aria-label="Mark as read"
            >
              <span class="material-icons">check_circle</span>
            </button>
            <button
              class="delete"
              @click="deleteThisNotification(notification.id)"
              aria-label="Delete notification"
            >
              <span class="material-icons">delete</span>
            </button>
          </div>
        </div>
      </div>

      <div class="pagination-controls">
        <button :disabled="currentPage === 0" @click="previousPage">
          Previous
        </button>
        <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
        <button :disabled="currentPage >= totalPages - 1" @click="nextPage">
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useNotificationStore } from '../../stores/notification.store';
import { formatDate } from '../../utils/helpers';
import { useNotifications } from '../../composables/useNotifications';
import { useAuthStore } from '../../stores/auth.store';

// --- Reactive States ---
const loading = ref(true);
const currentPage = ref(0);

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const notificationsGlobal = useNotifications();

// --- Computed Data ---
const notifications = computed(() => notificationStore.notifications.content);
const totalPages = computed(() => notificationStore.notifications.totalPages);
const unreadCount = computed(() => notificationStore.unreadNotifications);

// --- Utility Functions ---
const getNotificationIcon = (type: string) => {
  switch (type) {
    case 'alert':
      return 'warning';
    case 'info':
      return 'info';
    case 'reminder':
      return 'notifications_active';
    case 'system':
      return 'settings';
    default:
      return 'notifications';
  }
};

const formatNotificationDate = (dateString: string) => {
  return formatDate(dateString);
};

// --- Interaction Functions ---

// Fetches notifications
const getNotifications = async () => {
  loading.value = true;

  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'User not logged in. Cannot load notifications.',
    );

    loading.value = false;
    return;
  }

  try {
    await notificationStore.getAllNotifications(
      authStore.user.id,
      currentPage.value,
      10,
      'createdAt',
    );
  } catch (error: any) {
    notificationsGlobal.showError(
      'Could not load notifications: ' + error.message,
    );
  } finally {
    loading.value = false;
  }
};

const markThisAsRead = async (id: number) => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'You need to be logged in to mark notifications as read.',
    );

    return;
  }

  try {
    await notificationStore.markAsRead(authStore.user.id, id);
    notificationsGlobal.showSuccess('Notification marked as read.');
  } catch (error: any) {
    notificationsGlobal.showError(
      'Could not mark the notification as read: ' + error.message,
    );
  }
};

const deleteThisNotification = async (id: number) => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'You need to be logged in to delete notifications.',
    );

    return;
  }

  try {
    await notificationStore.deleteNotification(authStore.user.id, id);
    notificationsGlobal.showSuccess('Notification deleted successfully.');
  } catch (error: any) {
    notificationsGlobal.showError(
      'Could not delete the notification: ' + error.message,
    );
  }
};

// Marks all notifications as read
const markAllNotificationsAsRead = async () => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'You need to be logged in to mark all notifications as read.',
    );

    return;
  }

  try {
    if (unreadCount.value === 0) {
      notificationsGlobal.showInfo('No unread notifications to mark.');
      return;
    }

    await notificationStore.markAllAsRead(authStore.user.id);
    notificationsGlobal.showSuccess('All notifications marked as read.');
  } catch (error: any) {
    notificationsGlobal.showError(
      'Could not mark all notifications as read: ' + error.message,
    );
  }
};

// Pagination Functions
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
    getNotifications();
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++;
    getNotifications();
  }
};

onMounted(() => {
  if (authStore.user?.id) {
    getNotifications();
  } else {
    notificationsGlobal.showWarning(
      'Please log in to view your notifications.',
    );
  }
});
</script>

<style lang="scss" scoped>
@use 'Notifications.scss';
</style>
