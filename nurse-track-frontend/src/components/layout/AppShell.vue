<template>
  <div
    class="app-shell"
    :class="[
      { 'sidebar-open': isSidebarOpen, 'is-mobile': isMobile },
      userRoleClass, // Esta es la clase dinámica que generaremos
    ]"
  >
    <Sidebar
      @toggle="toggleSidebar"
      :is-open="isSidebarOpen"
      :is-mobile="isMobile"
    />
    <div
      v-if="isSidebarOpen && isMobile && isLateralSidebarOnMobile"
      class="overlay"
      @click="toggleSidebar"
    ></div>
    <main :class="{ 'sidebar-shifted': isSidebarOpen && !isMobile }">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from 'vue';
import Sidebar from './Sidebar.vue';
import { useAuthStore } from '../../stores/auth.store';

const isSidebarOpen = ref(false);
const isMobile = ref(window.innerWidth < 900);

const isLateralSidebarOnMobile = false;

const authStore = useAuthStore();

const userRoleClass = computed(() => {
  if (authStore.isAdmin) return 'role-admin';

  if (authStore.isSupervisor) return 'role-supervisor';

  if (authStore.isNurse) return 'role-nurse';

  return '';
});

const toggleSidebar = () => {
  if (isMobile.value && isLateralSidebarOnMobile) {
    isSidebarOpen.value = !isSidebarOpen.value;

    document.body.style.overflow = isSidebarOpen.value ? 'hidden' : '';
  } else if (!isMobile.value) {
    isSidebarOpen.value = !isSidebarOpen.value;

    document.body.style.overflow = '';
  }
};

const handleResize = () => {
  const wasMobile = isMobile.value;
  isMobile.value = window.innerWidth < 900;

  if (wasMobile && !isMobile.value) {
    isSidebarOpen.value = true;
  } else if (!isMobile.value) {
    isSidebarOpen.value = true;
  } else if (isMobile.value && !isLateralSidebarOnMobile) {
    isSidebarOpen.value = false;
  }

  document.body.style.overflow = '';
};

onMounted(() => {
  if (!isMobile.value) isSidebarOpen.value = true;

  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  document.body.style.overflow = '';
});
</script>

<style lang="scss" scoped>
@use 'AppShell.scss';
</style>
