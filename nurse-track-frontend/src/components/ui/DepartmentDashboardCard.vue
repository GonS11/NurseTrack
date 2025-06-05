<template>
  <div class="department-summary-card">
    <h3>{{ department.name }}</h3>
    <p class="location">{{ department.location }}</p>
    <div class="card-actions">
      <button
        @click="emit('manage-staff', department.id)"
        class="btn btn-staff"
      >
        Manage Staff
      </button>
      <button
        @click="emit('manage-schedule', department.id)"
        class="btn btn-schedule"
      >
        View Schedule
      </button>
      <button
        @click="emit('manage-requests', department.id)"
        class="btn btn-requests"
      >
        Manage Requests
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { DepartmentResponse } from '../../types/schemas/department.schema';

const props = defineProps<{
  department: DepartmentResponse;
}>();

const emit = defineEmits<{
  (e: 'manage-staff', departmentId: number): void;
  (e: 'manage-schedule', departmentId: number): void;
  (e: 'manage-requests', departmentId: number): void;
}>();
</script>

<style lang="scss" scoped>
@use 'DepartmentDashboardCard.scss';
</style>
