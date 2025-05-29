<template>
  <div
    class="app-shell"
    :class="{ 'sidebar-open': isSidebarOpen, 'is-mobile': isMobile }"
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
import { ref, onMounted, onBeforeUnmount } from 'vue';
import Sidebar from './Sidebar.vue';

const isSidebarOpen = ref(false);
const isMobile = ref(window.innerWidth < 900);

const isLateralSidebarOnMobile = false;

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
    document.body.style.overflow = '';
  } else if (!wasMobile && isMobile.value) {
    // Si pasamos de desktop a móvil, el sidebar en móvil no se "cierra" (oculta),
    // sino que se transforma en la barra superior.
    isSidebarOpen.value = false; // Establecemos a false para que los estilos móviles de Sidebar actúen
    document.body.style.overflow = '';
  }
};

onMounted(() => {
  if (!isMobile.value) {
    isSidebarOpen.value = true;
  }
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
