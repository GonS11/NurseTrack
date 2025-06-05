<template>
  <div class="dashboard-insight-card" :class="type">
    <h3>{{ title }}</h3>
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true,
  },
  type: {
    type: String,
    default: 'info',
  },
});
</script>

<style lang="scss" scoped>
@use 'DashboardCardContent.scss';
</style>
