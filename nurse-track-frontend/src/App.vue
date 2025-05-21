<template>
  <router-view />
</template>

<script setup lang="ts">
import { watch } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from './stores/auth.store';
import router from './router';

const route = useRoute();
const authStore = useAuthStore();

watch(
  () => authStore.isAuthenticated,

  (isAuthenticated) => {
    const currentRouteName = route.name;

    if (!isAuthenticated && !route.meta.public) {
      router.replace({ name: 'login' });
    } else if (isAuthenticated && currentRouteName === 'login') {
      router.replace({ name: 'dashboard' }).catch(() => {});
    }
  },
  { immediate: true },
);

// También vigila cambios de ruta
watch(
  () => route.path,
  () => {
    if (!authStore.isAuthenticated && !route.meta.public) {
      router.replace({ name: 'login' });
    }
  },
);
</script>
