<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useAuthStore } from './services';

const authStore = useAuthStore();

/*onMounted(async () => {
  try {
    if (authStore.isAuthenticated) {
      await authStore.loadCurrentUser();
    }
  } catch (error) {
    console.error('Error loading user:', error);
  }
});*/
</script>
