<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from './stores/auth.store';

const authStore = useAuthStore();
const router = useRouter();

// Cargar usuario al iniciar
const loadUser = async () => {
  try {
    if (authStore.isAuthenticated && !authStore.currentUser) {
      await authStore.getCurrentUser(); // o getCurrentUser según tu método
    }
  } catch (error) {
    console.error('Error loading user:', error);
    authStore.logout();
  }
};

// Redirecciones basadas en autenticación
const handleAuthState = () => {
  const { meta, path } = router.currentRoute.value;
  if (!authStore.isAuthenticated && !meta.public) {
    router.replace({ name: 'login' });
  } else if (authStore.isAuthenticated && path === '/login') {
    router.replace({ name: 'dashboard' });
  }
};

onMounted(() => {
  loadUser();
  handleAuthState();
});

// Watchers para cambios de autenticación
watch(() => authStore.isAuthenticated, handleAuthState);
watch(() => authStore.currentUser, handleAuthState);
</script>
