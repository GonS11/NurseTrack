<template>
  <div class="app-shell">
    <Sidebar @toggle="onToggle" :collapsed="collapsed" />
    <main :class="{ 'is-collapsed': collapsed }">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Sidebar from './Sidebar.vue';

const collapsed = ref(false);
const onToggle = () => {
  collapsed.value = !collapsed.value;
};
</script>

<style lang="scss" scoped>
@use 'AppShell.scss';
</style>
