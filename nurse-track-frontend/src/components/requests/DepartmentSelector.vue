<template>
  <div class="department-selector">
    <label for="department-select">Select Department:</label>
    <select
      id="department-select"
      :value="selectedDepartmentId"
      @change="updateSelectedDepartment"
      :disabled="isLoadingDepartments"
    >
      <option v-if="isLoadingDepartments" value="" disabled>
        Loading departments...
      </option>
      <option
        v-if="!isLoadingDepartments && supervisorStore.departments.length === 0"
        value=""
        disabled
      >
        No departments found
      </option>
      <option
        v-for="dept in supervisorStore.departments"
        :key="dept.id"
        :value="dept.id"
      >
        {{ dept.name }} ({{ dept.location }})
      </option>
    </select>
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';

const supervisorStore = useSupervisorStore();

const props = defineProps({
  selectedDepartmentId: {
    type: Number as PropType<number | null>,
    default: null,
  },
  isLoadingDepartments: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits<{
  (e: 'update:selectedDepartmentId', id: number | null): void;
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
