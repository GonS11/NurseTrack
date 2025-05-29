<template>
  <BaseModal :is-open="isOpen" :title="title" @close="closeModal">
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="department">Department:</label>
        <select
          id="department"
          v-model="selectedDepartmentId"
          @change="fetchDepartmentNursesAndAvailableNurses"
          required
        >
          <option value="" disabled>Select a department</option>
          <option
            v-for="department in allDepartments"
            :key="department.id"
            :value="department.id"
          >
            {{ department.name }}
          </option>
        </select>
      </div>

      <div v-if="selectedDepartmentId">
        <h3>Assigned Nurses:</h3>
        <ul v-if="assignedNurses.length > 0">
          <li v-for="assignment in assignedNurses" :key="assignment.nurse.id">
            {{ assignment.nurse.firstname }} {{ assignment.nurse.lastname }} ({{
              assignment.nurse.username
            }})
            <button
              type="button"
              class="btn btn-danger btn-sm"
              @click="
                removeNurse(assignment.department.id, assignment.nurse.id)
              "
            >
              Remove
            </button>
          </li>
        </ul>
        <p v-else>No nurses assigned to this department.</p>

        <h3>Assign New Nurse:</h3>
        <div class="form-group">
          <label for="newNurse">Select Nurse to Assign:</label>
          <select id="newNurse" v-model="nurseToAssignId">
            <option value="" disabled>Select a nurse</option>
            <option
              v-for="nurse in availableNurses"
              :key="nurse.id"
              :value="nurse.id"
            >
              {{ nurse.firstname }} {{ nurse.lastname }} ({{ nurse.email }})
            </option>
          </select>
          <button
            type="button"
            class="btn btn-primary btn-sm"
            @click="assignNurse"
            :disabled="!nurseToAssignId"
          >
            Assign
          </button>
        </div>
      </div>

      <div class="modal-actions">
        <button type="button" @click="closeModal" class="btn btn-secondary">
          Close
        </button>
      </div>
    </form>
  </BaseModal>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue';
import type { DepartmentResponse } from '../../../types/schemas/department.schema';
import { useAdminStore } from '../../../stores/admin.store';
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../../../types/schemas/assignments.schema';
import type { UserResponse } from '../../../types/schemas/user.schema';
import { UserRole } from '../../../types/enums/user-role.enum';

const props = defineProps<{
  isOpen: boolean;
  title: string;
  item?: DepartmentResponse; // The department selected for managing nurses
}>();

const emit = defineEmits(['close', 'submit']);

const adminStore = useAdminStore();

const selectedDepartmentId = ref<number | null>(null);
const assignedNurses = ref<NurseDepartmentResponse[]>([]);
const availableNurses = ref<UserResponse[]>([]);
const nurseToAssignId = ref<number | null>(null);

const allDepartments = computed(() => adminStore.departments.content); // You might need to fetch all departments first

const fetchInitialData = async () => {
  try {
    await adminStore.getAllDepartments(); // Fetch all departments for the dropdown
    if (props.item) {
      selectedDepartmentId.value = props.item.id;
      await fetchDepartmentNursesAndAvailableNurses();
    }
  } catch (error) {
    console.error('Error fetching initial data:', error);
  }
};

const fetchDepartmentNursesAndAvailableNurses = async () => {
  if (!selectedDepartmentId.value) {
    assignedNurses.value = [];
    availableNurses.value = [];
    return;
  }
  try {
    // Fetch nurses currently assigned to the selected department
    await adminStore.getAllNursesByDepartment(selectedDepartmentId.value);
    assignedNurses.value = adminStore.nurseAssignments.content;

    // Fetch all potential nurses and filter out those already assigned
    await adminStore.getAllUsers(); // Assuming this fetches all users
    const allNurses = adminStore.users.content.filter(
      (user) => user.role === UserRole.NURSE,
    );
    const assignedNurseIds = new Set(
      assignedNurses.value.map((assignment) => assignment.nurse.id),
    );
    availableNurses.value = allNurses.filter(
      (nurse) => !assignedNurseIds.has(nurse.id),
    );
    nurseToAssignId.value = null; // Reset selection
  } catch (error) {
    console.error(
      'Error fetching department nurses or available nurses:',
      error,
    );
    assignedNurses.value = []; // Reset on error
    availableNurses.value = []; // Reset on error
  }
};

const assignNurse = async () => {
  if (selectedDepartmentId.value && nurseToAssignId.value) {
    const request: AssignNurseRequest = {
      departmentId: selectedDepartmentId.value,
      nurseId: nurseToAssignId.value,
    };
    try {
      await adminStore.assignNurseToDepartment(request);
      await fetchDepartmentNursesAndAvailableNurses(); // Re-fetch to update lists
    } catch (error) {
      console.error('Error assigning nurse:', error);
    }
  }
};

const removeNurse = async (departmentId: number, nurseId: number) => {
  if (
    confirm('Are you sure you want to remove this nurse from the department?')
  ) {
    try {
      await adminStore.removeNurseFromDepartment(departmentId, nurseId);
      await fetchDepartmentNursesAndAvailableNurses(); // Re-fetch to update lists
    } catch (error) {
      console.error('Error removing nurse:', error);
    }
  }
};

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      fetchInitialData();
    }
  },
);

const handleSubmit = () => {
  // This modal doesn't have a single "submit" for a new item,
  // rather it manages assignments directly.
  // You can emit a generic 'refresh' or just rely on state updates.
  closeModal();
};

const closeModal = () => {
  emit('close');
};

onMounted(() => {
  if (props.isOpen) {
    fetchInitialData();
  }
});
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
