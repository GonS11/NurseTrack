<template>
  <aside :class="{ 'is-open': isOpen, 'is-mobile': isMobile }">
    <div class="logo">
      <img src="../../assets/img/Nursetrack-logo.png" alt="Logo" />
    </div>
    <button class="toggle" @click="$emit('toggle')" :aria-expanded="isOpen">
      <span class="material-icons">
        {{
          isOpen && !isMobile
            ? 'keyboard_double_arrow_left'
            : 'keyboard_double_arrow_right'
        }}
      </span>
    </button>
    <nav class="menu">
      <RouterLink
        v-for="route in allowedRoutes"
        :key="route.name"
        :to="{ name: route.name }"
        class="item"
        exact-active-class="active"
        @click="closeSidebarOnMobile"
      >
        <span class="material-icons">{{ route.icon }}</span>
        <span class="text">{{ route.text }}</span>
        <span
          v-if="route.name === 'notifications' && unreadCount"
          class="badge"
          >{{ unreadCount }}</span
        >
      </RouterLink>
    </nav>
    <footer class="footer">
      <button @click="handleLogout" :disabled="isLoggingOut">
        <span class="material-icons">logout</span>
        <span class="text">Salir</span>
      </button>
    </footer>
  </aside>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { RouterLink } from 'vue-router';
import { useAuthStore } from '../../services';
import { UserRole } from '../../types/enums/user-role.enum';
import { useNotificationStore } from '../../stores/notification.store';

const props = defineProps<{
  isOpen: boolean;
  isMobile: boolean;
}>();

const emit = defineEmits(['toggle']);

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const isLoggingOut = ref(false);

const handleLogout = async () => {
  isLoggingOut.value = true;
  await authStore.logout();
  isLoggingOut.value = false;
};

const closeSidebarOnMobile = () => {
  // Si el sidebar en móvil es una barra superior, no se necesita "cerrar" al hacer clic
  // Solo se cierra si se implementa como un menú hamburguesa desplegable
  // En este caso, al ser una barra siempre visible, no hacemos nada aquí
  // Si en el futuro se implementa un menú hamburguesa en móvil, aquí se emitiría el 'toggle'
};

const allowedRoutes = computed(() => {
  const base = [
    { name: 'dashboard', icon: 'home', text: 'Home' },
    { name: 'notifications', icon: 'notifications', text: 'Notifications' },
  ];

  const role = authStore.user?.roles?.[0]?.authority;

  if (role === UserRole.ADMIN) {
    base.splice(
      1,
      0,
      { name: 'admin-users', icon: 'group', text: 'Users' },
      { name: 'admin-departments', icon: 'business', text: 'Departments' },
      {
        name: 'supervisor-assignment',
        icon: 'supervised_user_circle',
        text: 'Supervisor Management',
      },
      {
        name: 'nurse-assignment',
        icon: 'person_add',
        text: 'Nurse Management',
      },
    );
  }

  if (role === UserRole.SUPERVISOR) {
    base.splice(
      1,
      0,
      {
        name: 'supervisor-department-staff',
        icon: 'people',
        text: 'Management',
      },
      { name: 'supervisor-shifts', icon: 'calendar_today', text: 'My Shifts' },
      {
        name: 'supervisor-requests',
        icon: 'description',
        text: 'Request Management',
      },
    );
  }

  if (role === UserRole.NURSE) {
    base.splice(
      1,
      0,
      { name: 'nurse-departments', icon: 'business', text: 'My Departments' },
      { name: 'nurse-schedule', icon: 'calendar_month', text: 'My Schedule' },
      { name: 'nurse-shift-swap', icon: 'swap_horiz', text: 'Shift Request' },
      {
        name: 'nurse-vacation',
        icon: 'flight_takeoff',
        text: 'Vacation Request',
      },
    );
  }

  return base;
});

const unreadCount = computed(() => notificationStore.unreadNotifications);

onMounted(() => {
  if (authStore.currentUser) {
    notificationStore.getAllNotifications(authStore.currentUser.id);
  }
});
</script>

<style lang="scss" scoped>
@use 'Sidebar.scss';
</style>
