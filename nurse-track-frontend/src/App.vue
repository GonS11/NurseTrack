<template>
  <router-view />
</template>

<script setup lang="ts">
import { watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from './stores/auth.store';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

watchEffect(async () => {
  const isPublic = route.meta.public;
  // This `isAuthenticated` will now reflect the rehydrated state
  if (!authStore.isAuthenticated && !isPublic) {
    await router.replace({ name: 'login' });
  } else if (authStore.isAuthenticated && route.name === 'login') {
    const redirect = route.query.redirect?.toString() || 'dashboard';
    await router.replace({ name: redirect });
  }
});
</script>

<style lang="scss">
@use './App.scss';
</style>
