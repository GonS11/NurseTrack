<template>
  <div class="department-detail-card">
    <h3>{{ department.name }}</h3>
    <p><strong>Location:</strong> {{ department.location }}</p>
    <p>
      <strong>Status:</strong>
      <span
        :class="[
          'status-badge',
          department.isActive ? 'is-active' : 'is-inactive',
        ]"
      >
        {{ department.isActive ? 'Active' : 'Inactive' }}
      </span>
    </p>
    <p><strong>Assigned At:</strong> {{ formatDate(department.createdAt) }}</p>
  </div>
</template>

<script setup lang="ts">
import type { DepartmentResponse } from '../../../types/schemas/department.schema';

const props = defineProps<{
  department: DepartmentResponse;
}>();

const formatDate = (isoString: string) => {
  const date = new Date(isoString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};
</script>

<style lang="scss" scoped>
@use 'DepartmentDetailCard.scss';
</style>
