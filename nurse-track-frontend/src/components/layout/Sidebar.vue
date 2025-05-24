<template>
  <aside :class="{ 'is-expanded': isExpanded, mobile: isMobile }">
    <div class="logo">
      <img src="../../assets/img/Nursetrack-logo.png" alt="NurseTrack" />
    </div>

    <div class="menu-toggle-wrap">
      <button
        class="menu-toggle"
        @click="ToggleMenu"
        :aria-expanded="isExpanded"
        aria-controls="sidebar-menu"
      >
        <span class="material-icons"> keyboard_double_arrow_right </span>
      </button>
    </div>

    <nav id="sidebar-menu" class="menu">
      <RouterLink
        v-for="route in allowedRoutes"
        :key="route.name"
        :to="{ name: route.name }"
        class="button"
        active-class="active"
        :aria-current="route.name === $route.name ? 'page' : null"
      >
        <span class="material-icons">{{ route.icon }}</span>
        <span class="text">{{ route.text }}</span>

        <!-- Badge solo para notificaciones -->
        <span
          v-if="route.name === 'notifications' && unreadCount > 0"
          class="badge"
          aria-label="Notificaciones no leídas"
        >
          {{ unreadCount }}
        </span>
      </RouterLink>
    </nav>

    <div class="sidebar-footer">
      <button @click="handleLogout" :disabled="isLoggingOut">
        <span v-if="isLoggingOut">Closing...</span>
        <template v-else>
          <span class="material-icons">logout</span>
          <span class="text">Close session</span>
        </template>
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { RouterLink } from 'vue-router';
import { useAuthStore } from '../../services';
import { UserRole } from '../../types/enums/user-role.enum';
import { useNotificationStore } from '../../stores/notification.store';

const authStore = useAuthStore();
const notificationStore = useNotificationStore();

// Sidebar state
const isExpanded = ref(true);
const isMobile = ref(window.innerWidth < 900);
const isLoggingOut = ref(false);

// Logout
const handleLogout = async () => {
  isLoggingOut.value = true;
  try {
    await authStore.logout();
  } finally {
    isLoggingOut.value = false;
  }
};

// Toggle sidebar
const ToggleMenu = () => {
  isExpanded.value = !isExpanded.value;
  if (isMobile.value) {
    document.body.style.overflow = isExpanded.value ? 'hidden' : 'auto';
  }
};

// Rutas permitidas
const allowedRoutes = computed(() => {
  const role = authStore.user?.roles?.[0]?.authority;
  const baseRoutes = [
    { name: 'dashboard', icon: 'home', text: 'Inicio' },
    { name: 'profile', icon: 'person', text: 'Perfil' },
    { name: 'notifications', icon: 'notifications', text: 'Notificaciones' },
    { name: 'settings', icon: 'settings', text: 'Configuración' },
    { name: 'about', icon: 'info', text: 'Acerca de' },
  ];

  if (role === UserRole.NURSE) {
    baseRoutes.splice(
      1,
      0,
      { name: 'nurse-schedule', icon: 'calendar_today', text: 'Mis Turnos' },
      { name: 'nurse-shift-swap', icon: 'swap_horiz', text: 'Cambio de Turno' },
    );
  }

  if (role === UserRole.ADMIN) {
    baseRoutes.splice(
      1,
      0,
      { name: 'admin-users', icon: 'group', text: 'Usuarios' },
      { name: 'admin-departments', icon: 'business', text: 'Departamentos' },
    );
  }

  return baseRoutes;
});

// Computed contador de no-leídas
const unreadCount = computed(() => notificationStore.unreadNotifications);

// Responsive handler
const handleResize = () => {
  isMobile.value = window.innerWidth < 900;
  if (!isMobile.value) isExpanded.value = true;
};

onMounted(async () => {
  // Carga inicial de notificaciones
  const user = authStore.currentUser;
  if (user) {
    await notificationStore.getAllNotifications(user.id);
  }

  window.addEventListener('resize', handleResize);
  if (isMobile.value) isExpanded.value = false;
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style lang="scss" scoped>
@use 'Sidebar.scss';
</style>
