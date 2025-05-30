<template>
  <div class="nurse-table-container">
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Assigned Since</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="assignment in nurses" :key="assignment.nurse.id">
          <td>
            {{ assignment.nurse.firstname }} {{ assignment.nurse.lastname }}
          </td>
          <td>{{ assignment.nurse.username }}</td>
          <td>{{ formatDate(assignment.assignedAt) }}</td>
          <td>
            <button
              @click="$emit('remove-nurse', assignment.nurse.id)"
              class="remove-btn danger"
            >
              <span class="material-icons">person_remove</span>
              <span class="button-text">Remove</span>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import type { NurseDepartmentResponse } from '../../../types/schemas/assignments.schema';

const props = defineProps<{
  nurses: NurseDepartmentResponse[];
}>();

const emit = defineEmits(['remove-nurse']);

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
@use 'NurseTable.scss';
</style>
