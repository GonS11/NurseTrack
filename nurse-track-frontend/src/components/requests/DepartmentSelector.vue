<template>
  <div class="department-selector">
    <label for="department-select">Select Department:</label>
    <select
      id="department-select"
      :value="selectedDepartmentId"
      @change="updateSelectedDepartment"
      :disabled="isLoadingDepartments || departments.length === 0"
    >
      <option value="" disabled>
        {{
          isLoadingDepartments
            ? 'Loading departments...'
            : 'Select a department'
        }}
      </option>
      <option v-if="errorDepartments" disabled>
        {{ errorDepartments }}
      </option>
      <option v-for="dept in departments" :key="dept.id" :value="dept.id">
        {{ dept.name }}
      </option>
    </select>
    <p
      v-if="
        departments.length === 0 && !isLoadingDepartments && !errorDepartments
      "
    >
      No departments available.
    </p>
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import type { DepartmentResponse } from '../../types/schemas/department.schema';

const props = defineProps({
  selectedDepartmentId: {
    type: Number as PropType<number | null>,
    default: null,
  },
  departments: {
    type: Array as PropType<DepartmentResponse[]>,
    required: true,
  },
  isLoadingDepartments: {
    type: Boolean,
    default: false,
  },
  errorDepartments: {
    type: String as PropType<string | null>,
    default: null,
  },
});

const emit = defineEmits<{
  (e: 'update:selectedDepartmentId', id: number | null): void;
  (e: 'error-alert', message: string): void;
}>();

function updateSelectedDepartment(event: Event) {
  const target = event.target as HTMLSelectElement;
  const value = target.value === '' ? null : Number(target.value);
  emit('update:selectedDepartmentId', value);
}
</script>

<style scoped lang="scss">
@use 'DepartmentSelector.scss';
</style>
