<template>
  <GlobalNotification
    v-model="notifications.showNotification.value"
    :message="notifications.notificationMessage.value"
    :type="notifications.notificationType.value"
    :dismissible="true"
    :autoClose="notifications.notificationAutoClose.value"
    @dismiss="notifications.dismiss"
  />

  <router-view />
</template>

<script setup lang="ts">
import { watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from './stores/auth.store';
import { useNotifications } from './composables/useNotifications';
import GlobalNotification from './components/common/GlobalNotification.vue';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const notifications = useNotifications();

// This watchEffect ensures navigation based on authentication status and route metadata.
watchEffect(async () => {
  const isPublic = route.meta.public;

  if (!authStore.isAuthenticated && !isPublic) {
    await router.replace({ name: 'login' });
  } else if (authStore.isAuthenticated && route.name === 'login') {
    const redirect = route.query.redirect?.toString() || 'dashboard';
    await router.replace({ name: redirect });
  }
});
</script>
