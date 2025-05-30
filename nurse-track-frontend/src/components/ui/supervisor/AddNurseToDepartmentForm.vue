<template>
  <form @submit.prevent="handleSubmit" class="add-nurse-form">
    <div class="form-group">
      <label for="nurse-select">Select Nurse:</label>
      <select id="nurse-select" v-model="selectedNurseId" required>
        <option value="" disabled>Select a nurse</option>
        <option
          v-for="nurse in availableNurses"
          :key="nurse.id"
          :value="nurse.id"
        >
          {{ nurse.firstname }} {{ nurse.lastname }} ({{ nurse.email }})
        </option>
      </select>
    </div>
    <button
      type="submit"
      :disabled="isSubmitting || !selectedNurseId"
      class="add-nurse-btn"
    >
      <span v-if="isSubmitting" class="loading-spinner"></span>
      <span v-else class="material-icons">person_add</span> Add Nurse
    </button>
    <p v-if="localError" class="error-message">{{ localError }}</p>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
  </form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useSupervisorStore } from '../../../stores/supervisor.store';
import { useSupervisorDepartmentService } from '../../../services/supervisor/supervisorDepartment.service'; // Import the supervisor service
import type { UserResponse } from '../../../types/schemas/user.schema';
// REMOVED: import { useAdminUserService } from '../../../services/admin/adminUser.service';
// REMOVED: import { UserRole } from '../../../types/enums/user-role.enum'; // No longer needed for client-side filtering by role

const props = defineProps<{
  departmentId: number;
}>();

const emit = defineEmits(['nurse-added']);

const supervisorStore = useSupervisorStore();

const selectedNurseId = ref<number | null>(null);
const availableNurses = ref<UserResponse[]>([]);
const isSubmitting = ref(false);
const localError = ref('');
const successMessage = ref('');

// Fetch all nurses, then filter out those already in the current department
const fetchAvailableNurses = async () => {
  localError.value = '';
  try {
    // OLD: const allUsersPage = await useAdminUserService.getAllUsers(0, 100, 'id');
    // OLD: const allNurses = allUsersPage.content.filter(
    // OLD:   (user) => user.role === UserRole.NURSE,
    // OLD: );

    // NEW: Call the supervisor-specific service to get all nurses
    const allNurses =
      await useSupervisorDepartmentService.getAllNursesForAssignment();

    // Get nurses already assigned to this department from supervisorStore
    const assignedNurseIds = supervisorStore.nurseAssignments.map(
      (assign) => assign.nurse.id,
    );

    // Filter out nurses already assigned to the current department
    availableNurses.value = allNurses.filter(
      (nurse) => !assignedNurseIds.includes(nurse.id),
    );

    // If a nurse was previously selected but is no longer available (e.g., got assigned), clear selection
    if (
      selectedNurseId.value &&
      !availableNurses.value.some((n) => n.id === selectedNurseId.value)
    ) {
      selectedNurseId.value = null;
    }
  } catch (error: any) {
    localError.value = error.message || 'Failed to fetch available nurses.';
    console.error('Error fetching available nurses:', error);
  }
};

const handleSubmit = async () => {
  if (!selectedNurseId.value || !props.departmentId) {
    localError.value = 'Please select a nurse.';
    return;
  }

  isSubmitting.value = true;
  localError.value = '';
  successMessage.value = '';

  try {
    await supervisorStore.addNurseToDepartment(props.departmentId, {
      departmentId: props.departmentId,
      nurseId: selectedNurseId.value,
    });
    successMessage.value = 'Nurse added successfully!';
    selectedNurseId.value = null; // Clear selection
    emit('nurse-added'); // Notify parent to refresh nurse list
    await fetchAvailableNurses(); // Refresh available nurses for dropdown
  } catch (error: any) {
    localError.value =
      error.response?.data?.message || error.message || 'Failed to add nurse.';
  } finally {
    isSubmitting.value = false;
  }
};

// Watch for changes in departmentId or nurseAssignments in the store to re-fetch available nurses
watch(
  () => [props.departmentId, supervisorStore.nurseAssignments],
  () => {
    if (props.departmentId) {
      fetchAvailableNurses();
    }
  },
  { deep: true, immediate: true },
); // Deep watch for nurseAssignments array changes
</script>

<style lang="scss" scoped>
@use 'AddNurseToDepartmentForm.scss';
</style>
