<template>
  <div class="app-shell" :class="{ 'sidebar-open': isSidebarOpen }">
    <Sidebar @toggle="toggleSidebar" :is-open="isSidebarOpen" />
    <div
      v-if="isSidebarOpen && isMobile"
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

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
  // Controla el scroll del body en móvil cuando el sidebar está abierto
  if (isMobile.value) {
    document.body.style.overflow = isSidebarOpen.value ? 'hidden' : '';
  }
};

const handleResize = () => {
  isMobile.value = window.innerWidth < 900;
  // Si no es móvil, el sidebar siempre estará cerrado por defecto
  if (!isMobile.value) {
    isSidebarOpen.value = false;
    document.body.style.overflow = ''; // Asegura que el scroll se restablezca
  }
};

onMounted(() => {
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  document.body.style.overflow = ''; // Limpia el estilo del body al desmontar
});
</script>

<style lang="scss" scoped>
@use 'AppShell.scss';
</style>
