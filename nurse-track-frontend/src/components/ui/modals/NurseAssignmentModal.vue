<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ title }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="department">Department</label>
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
          <ul v-if="assignedNurses.length > 0" class="nurse-list">
            <li v-for="assignment in assignedNurses" :key="assignment.nurse.id">
              <span>
                {{ assignment.nurse.firstname }}
                {{ assignment.nurse.lastname }} ({{
                  assignment.nurse.username
                }})
              </span>
              <button
                type="button"
                class="button button-danger button-sm"
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
            <label for="newNurse">Select Nurse to Assign</label>
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
          </div>
          <div class="form-actions assign-actions">
            <button
              type="button"
              class="button button-primary"
              @click="assignNurse"
              :disabled="!nurseToAssignId"
            >
              Assign
            </button>
          </div>
        </div>

        <div class="form-actions">
          <button type="button" @click="closeModal">Cancel</button>
        </div>
      </form>
    </div>
  </div>
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

const allDepartments = computed(() => adminStore.departments.content);

const fetchInitialData = async () => {
  try {
    await adminStore.getAllDepartments();
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
    await adminStore.getAllNursesByDepartment(selectedDepartmentId.value);
    assignedNurses.value = adminStore.nurseAssignments.content;

    await adminStore.getAllUsers();
    const allNurses = adminStore.users.content.filter(
      (user) => user.role === UserRole.NURSE,
    );
    const assignedNurseIds = new Set(
      assignedNurses.value.map((assignment) => assignment.nurse.id),
    );
    availableNurses.value = allNurses.filter(
      (nurse) => !assignedNurseIds.has(nurse.id),
    );
    nurseToAssignId.value = null;
  } catch (error) {
    console.error(
      'Error fetching department nurses or available nurses:',
      error,
    );
    assignedNurses.value = [];
    availableNurses.value = [];
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
      await fetchDepartmentNursesAndAvailableNurses();
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
      await fetchDepartmentNursesAndAvailableNurses();
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
    } else {
      // Reset state when modal closes
      selectedDepartmentId.value = null;
      assignedNurses.value = [];
      availableNurses.value = [];
      nurseToAssignId.value = null;
    }
  },
);

const handleSubmit = () => {
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
@use 'Modal.scss'; // This implicitly uses main.scss as well
@use '../../../styles/main.scss' as *; // Explicitly import if needed for direct variable access

.nurse-list {
  list-style: none;
  padding: 0;
  margin-top: $spacer * 3; // Using Sass variable directly

  li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacer * 0.5 0; // Using Sass variable
    border-bottom: 1px solid $gray-200; // Using Sass variable
    &:last-child {
      border-bottom: none;
    }

    .button-danger {
      margin-left: $spacer * 3; // Using Sass variable
    }
  }
}

.assign-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: $spacer * 3; // Using Sass variable
}

.button {
  padding: $btn-padding-y $btn-padding-x; // Use Sass variable directly
  border-radius: $btn-border-radius; // Use Sass variable directly
  font-size: $font-size-base; // Use Sass variable directly
  cursor: pointer;
  transition: $transition; // Use Sass variable directly

  &.button-primary {
    background-color: $primary; // Use Sass variable directly
    color: $white; // Use Sass variable directly
    border: 1px solid $primary; // Use Sass variable directly

    &:hover {
      background-color: $primary-hover; // Use Sass variable directly
      border-color: $primary-hover; // Use Sass variable directly
    }
  }

  &.button-secondary {
    background-color: $white;
    color: $gray-700;
    border: 1px solid $gray-300;

    &:hover {
      background-color: $gray-100;
    }
  }

  &.button-danger {
    background-color: $error;
    color: $white;
    border: 1px solid $error;

    &:hover {
      background-color: color.adjust(
        $error,
        $lightness: -10%
      ); // Or define $error-dark
      border-color: color.adjust($error, $lightness: -10%);
    }
  }

  &.button-sm {
    // You'd need to define these in your variables.scss if not already
    padding: $btn-padding-y * 0.75 $btn-padding-x * 0.75; // Example smaller padding
    font-size: $font-size-base * 0.875; // Example smaller font size
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}
</style>
