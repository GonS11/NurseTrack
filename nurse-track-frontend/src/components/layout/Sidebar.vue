<template>
  <aside :class="{ 'is-open': isOpen, 'is-mobile': isMobile }">
    <div class="logo">
      <img src="../../assets/img/Nursetrack-logo.png" alt="Logo" />
    </div>
    <button class="toggle" @click="$emit('toggle')" :aria-expanded="isOpen">
      <span class="material-icons">
        {{
          isOpen ? 'keyboard_double_arrow_left' : 'keyboard_double_arrow_right'
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
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { RouterLink } from 'vue-router';
import { useAuthStore } from '../../services';
import { UserRole } from '../../types/enums/user-role.enum';
import { useNotificationStore } from '../../stores/notification.store';

const props = defineProps<{
  isOpen: boolean; // Renombrada de 'collapsed' a 'isOpen' para mayor claridad
}>();

const emit = defineEmits(['toggle']);

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const isMobile = ref(window.innerWidth < 900);
const isLoggingOut = ref(false);

const handleLogout = async () => {
  isLoggingOut.value = true;
  await authStore.logout();
  isLoggingOut.value = false;
};

const closeSidebarOnMobile = () => {
  // En móvil, si el sidebar está abierto (expandido), lo cerramos (colapsamos a solo iconos)
  // Al hacer clic en un enlace.
  if (isMobile.value && props.isOpen) {
    emit('toggle');
  }
  // No necesitamos controlar el overflow aquí, AppShell lo maneja.
};

const allowedRoutes = computed(() => {
  const base = [
    { name: 'dashboard', icon: 'home', text: 'Inicio' },
    { name: 'notifications', icon: 'notifications', text: 'Notificaciones' },
  ];
  const role = authStore.user?.roles?.[0]?.authority;
  if (role === UserRole.NURSE)
    base.splice(
      1,
      0,
      { name: 'nurse-schedule', icon: 'calendar_today', text: 'Mis Turnos' },
      { name: 'nurse-shift-swap', icon: 'swap_horiz', text: 'Cambio de Turno' },
    );
  if (role === UserRole.ADMIN)
    base.splice(
      1,
      0,
      { name: 'admin-users', icon: 'group', text: 'Usuarios' },
      { name: 'admin-departments', icon: 'business', text: 'Departamentos' },
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
  return base;
});

const unreadCount = computed(() => notificationStore.unreadNotifications);

const handleResize = () => {
  isMobile.value = window.innerWidth < 900;
  // Cuando se redimensiona a desktop, si el sidebar está abierto,
  // nos aseguramos de que no tenga overflow oculto en el body
  // y lo dejamos abierto (expandido) por defecto en desktop.
  if (!isMobile.value) {
    emit('toggle'); // Esto forzará el estado a 'isOpen = false' en AppShell si ya estaba abierto,
    // y luego el onToggle de AppShell lo pondrá en true (expandido)
    // si ya estaba expandido y pasó a no ser móvil.
    // Para simplificar, en desktop siempre asumimos que está "expandido"
    // visualmente, por lo que su estado de 'isOpen' es lo que controla el tamaño.
  } else {
    // Si es móvil y el sidebar estaba expandido, lo colapsamos (solo iconos)
    // para que no ocupe demasiado espacio inicialmente.
    if (props.isOpen) {
      emit('toggle');
    }
  }
};

onMounted(() => {
  window.addEventListener('resize', handleResize);
  // Al montar, si es móvil, asegúrate de que el sidebar esté colapsado (solo iconos)
  // por defecto.
  if (isMobile.value) {
    if (props.isOpen) {
      // Si AppShell lo inició como true, lo colapsamos.
      emit('toggle');
    }
  } else {
    // En desktop, por defecto debe estar abierto (expandido)
    if (!props.isOpen) {
      emit('toggle');
    }
  }

  if (authStore.currentUser) {
    notificationStore.getAllNotifications(authStore.currentUser.id);
  }
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style lang="scss" scoped>
@use 'Sidebar.scss';
</style>
