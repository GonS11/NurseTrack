<template>
  <div class="notifications-container">
    <div class="notifications-header">
      <h1>
        Notifications <span class="badge">{{ unreadCount }}</span>
      </h1>
      <button
        class="mark-all-read"
        @click="markAllAsRead"
        :disabled="unreadCount === 0"
      >
        Mark all as read
      </button>
    </div>

    <div v-if="loading" class="loading-spinner">
      <span class="material-icons spin">autorenew</span>
    </div>

    <div v-else>
      <div v-if="notifications.length === 0" class="empty-state">
        <span class="material-icons">notifications_off</span>
        <p>No new notifications</p>
      </div>

      <div class="notifications-list">
        <div
          v-for="notification in notifications"
          :key="notification.id"
          :class="['notification-item', { unread: !notification.isRead }]"
        >
          <div class="notification-icon">
            <span class="material-icons">{{ getIcon(notification.type) }}</span>
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
              @click="markAsRead(notification.id)"
              aria-label="Marcar como leída"
            >
              <span class="material-icons">check_circle</span>
            </button>
            <button
              class="delete"
              @click="deleteNotification(notification.id)"
              aria-label="Eliminar notificación"
            >
              <span class="material-icons">delete</span>
            </button>
          </div>
        </div>
      </div>

      <div class="pagination-controls">
        <button :disabled="currentPage === 0" @click="changePage(-1)">
          Anterior
        </button>
        <span>Página {{ currentPage + 1 }} de {{ totalPages }}</span>
        <button
          :disabled="currentPage >= totalPages - 1"
          @click="changePage(1)"
        >
          Siguiente
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '../../services';
import { useNotificationStore } from '../../stores/notification.store';
import { useNotifications } from '../../composables/useNotifications';

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const notificationsGlobal = useNotifications();
const currentPage = ref(0);
const loading = ref(true);

const notifications = computed(() => notificationStore.notifications.content);
const totalPages = computed(() => notificationStore.notifications.totalPages);
const unreadCount = computed(() => notificationStore.unreadNotifications);

const getIcon = (type: string) => {
  const icons: Record<string, string> = {
    alert: 'warning',
    info: 'info',
    reminder: 'notifications_active',
    system: 'settings',
    default: 'notifications',
  };
  return icons[type] || icons.default;
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

const loadNotifications = async () => {
  try {
    loading.value = true;
    await notificationStore.getAllNotifications(
      authStore.user?.id || 0,
      currentPage.value,
      10,
      'createdAt',
    );
  } catch (error: any) {
    notificationsGlobal.showError(error.message);
  } finally {
    loading.value = false;
  }
};

const markAsRead = async (id: number) => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'User not logged in to mark notification as read.',
    );
    return;
  }
  try {
    await notificationStore.markAsRead(authStore.user.id, id);
    notificationsGlobal.showSuccess('Notification marked as read.');
  } catch (error: any) {
    notificationsGlobal.showError(error.message);
  }
};

const deleteNotification = async (id: number) => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'User not logged in to delete notification.',
    );
    return;
  }
  try {
    await notificationStore.deleteNotification(authStore.user.id, id);
    notificationsGlobal.showSuccess('Notification deleted successfully.');
  } catch (error: any) {
    notificationsGlobal.showError(error.message);
  }
};

const markAllAsRead = async () => {
  if (!authStore.user?.id) {
    notificationsGlobal.showWarning(
      'User not logged in to mark all notifications as read.',
    );

    return;
  }
  const unreadIds = notifications.value
    .filter((n) => !n.isRead)
    .map((n) => n.id);

  if (unreadIds.length === 0) {
    notificationsGlobal.showInfo('No unread notifications to mark.');
    return;
  }

  try {
    for (const id of unreadIds) {
      await notificationStore.markAsRead(authStore.user.id, id);
    }
    notificationsGlobal.showSuccess('All notifications marked as read.');
  } catch (error: any) {
    notificationsGlobal.showError(error.message);
  }
};

const changePage = (delta: number) => {
  currentPage.value += delta;
  loadNotifications();
};

onMounted(() => {
  if (authStore.user?.id) {
    loadNotifications();
  } else {
    notificationsGlobal.showWarning(
      'User not logged in. Cannot load notifications.',
    );
  }
});
</script>

<style lang="scss" scoped>
@use 'Notifications.scss';
</style>
