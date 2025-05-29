<template>
  <RouterLink :to="{ name: link }">
    <div class="dashboard-stat-card" :class="color">
      <div class="icon-wrapper">
        <span class="material-icons">{{ icon }}</span>
      </div>
      <div class="content">
        <p class="title">{{ title }}</p>
        <p class="value">{{ value }}</p>
      </div>
    </div>
  </RouterLink>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true,
  },
  value: {
    type: Number,
    required: true,
  },
  icon: {
    type: String,
    required: true,
  },
  link: {
    type: String,
    required: false,
  },
  color: {
    type: String,
    default: 'primary',
  },
});
</script>

<style lang="scss" scoped>
@use 'DashboardCard.scss';
</style>
