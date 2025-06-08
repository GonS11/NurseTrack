<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ title }}</h2>

      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <InputSelect
            id="department"
            label="Department"
            :model-value="selectedDepartmentId"
            @update:model-value="updateSelectedDepartment"
            :text="'Select a department'"
            :entities="allDepartments"
            :item-key="'id'"
            :item-value="'id'"
            option-value="name"
            placeholder-value=""
          />
        </div>

        <div v-if="selectedDepartmentId" class="department-content">
          <h3>Assigned Nurses:</h3>

          <ul v-if="assignedNurses.length > 0" class="nurse-list">
            <li v-for="assignment in assignedNurses" :key="assignment.nurse.id">
              <span class="nurse-info">
                {{ assignment.nurse.firstname }}
                {{ assignment.nurse.lastname }} ({{
                  assignment.nurse.username
                }})
              </span>
              <button
                type="button"
                class="btn btn-danger btn-sm"
                @click="
                  handleRemoveNurseConfirmation(
                    assignment.department.id,
                    assignment.nurse.id,
                    assignment.nurse.firstname,
                    assignment.nurse.lastname,
                  )
                "
              >
                Remove
              </button>
            </li>
          </ul>

          <p v-else class="no-nurses">No nurses assigned to this department.</p>

          <h3>Assign New Nurse:</h3>
          <div class="form-group">
            <InputSelect
              id="newNurse"
              label="Select Nurse to Assign"
              :model-value="nurseToAssignId"
              @update:model-value="updateNurseToAssignId"
              :text="'Select a nurse'"
              :entities="availableNurses"
              :item-key="'id'"
              :item-value="'id'"
              :option-value="nurseOptionText"
              placeholder-value=""
            />
          </div>

          <div class="form-actions assign-actions">
            <button
              type="button"
              class="btn btn-primary"
              @click="assignNurse"
              :disabled="!nurseToAssignId"
            >
              Assign
            </button>
          </div>
        </div>

        <div class="form-actions">
          <button type="button" @click="closeModal" class="btn btn-secondary">
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>

  <ConfirmModal
    v-model="showConfirmModal"
    :message="confirmMessage"
    @confirmed="executeRemoveNurse"
    @cancelled="cancelRemoveNurseOperation"
  />
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import type { DepartmentResponse } from '../../../types/schemas/department.schema';
import { useAdminStore } from '../../../stores/admin.store';
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../../../types/schemas/assignments.schema';
import type { UserResponse } from '../../../types/schemas/user.schema';
import { UserRole } from '../../../types/enums/user-role.enum';
import InputSelect from '../InputSelect.vue';
import ConfirmModal from '../../common/ConfirmModal.vue';
import { useNotifications } from '../../../composables/useNotifications';

const { showError, showInfo } = useNotifications();
const props = defineProps<{
  isOpen: boolean;
  title: string;
  item?: DepartmentResponse;
}>();

const emit = defineEmits(['close', 'submit']);

const adminStore = useAdminStore();

const selectedDepartmentId = ref<number | null>(null);
const assignedNurses = ref<NurseDepartmentResponse[]>([]);
const availableNurses = ref<UserResponse[]>([]);
const nurseToAssignId = ref<number | null>(null);

const allDepartments = ref<DepartmentResponse[]>([]);

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const nurseToRemoveDetails = ref<{
  departmentId: number;
  nurseId: number;
} | null>(null);

const fetchInitialData = async () => {
  try {
    await adminStore.getAllDepartments();
    allDepartments.value = adminStore.departments.content;

    if (props.item) {
      selectedDepartmentId.value = props.item.id;
      await fetchDepartmentNursesAndAvailableNurses();
    }
  } catch (error: any) {
    showError('Error fetching initial data:', error);
  }
};

const fetchDepartmentNursesAndAvailableNurses = async () => {
  if (!selectedDepartmentId.value) return;

  try {
    await adminStore.getAllNursesByDepartment(selectedDepartmentId.value);
    assignedNurses.value = adminStore.nurseAssignments.content;

    await adminStore.getAllUsers();
    const allNurses = adminStore.users.content.filter(
      (user) => user.role === UserRole.NURSE,
    );

    const assignedNurseIds = new Set(
      assignedNurses.value.map((a) => a.nurse.id),
    );

    availableNurses.value = allNurses.filter(
      (nurse) => !assignedNurseIds.has(nurse.id),
    );

    nurseToAssignId.value = null;
  } catch (error: any) {
    showError('Error fetching nurses:', error);
    assignedNurses.value = [];
    availableNurses.value = [];
  }
};

const updateSelectedDepartment = (value: string | number | null) => {
  selectedDepartmentId.value = value ? Number(value) : null;
  if (selectedDepartmentId.value) {
    fetchDepartmentNursesAndAvailableNurses();
  } else {
    assignedNurses.value = [];
    availableNurses.value = [];
  }
};

const updateNurseToAssignId = (value: string | number | null) => {
  nurseToAssignId.value = value ? Number(value) : null;
};

const nurseOptionText = (nurse: UserResponse) => {
  return `${nurse.firstname} ${nurse.lastname} (${nurse.email})`;
};

const assignNurse = async () => {
  if (!selectedDepartmentId.value || !nurseToAssignId.value) return;

  const request: AssignNurseRequest = {
    departmentId: selectedDepartmentId.value,
    nurseId: nurseToAssignId.value,
  };

  try {
    await adminStore.assignNurseToDepartment(request);
    await fetchDepartmentNursesAndAvailableNurses();
  } catch (error: any) {
    showError('Error assigning nurse:', error);
  }
};

const handleRemoveNurseConfirmation = (
  departmentId: number,
  nurseId: number,
  nurseFirstName: string,
  nurseLastName: string,
) => {
  nurseToRemoveDetails.value = { departmentId, nurseId };
  confirmMessage.value = `Are you sure you want to remove nurse "${nurseFirstName} ${nurseLastName}" from this department?`;
  showConfirmModal.value = true;
};

const executeRemoveNurse = async () => {
  if (!nurseToRemoveDetails.value) return;

  const { departmentId, nurseId } = nurseToRemoveDetails.value;

  try {
    await adminStore.removeNurseFromDepartment(departmentId, nurseId);
    await fetchDepartmentNursesAndAvailableNurses();
  } catch (error: any) {
    showError('Error removing nurse:', error);
  } finally {
    nurseToRemoveDetails.value = null;
  }
};

const cancelRemoveNurseOperation = () => {
  nurseToRemoveDetails.value = null;
  showInfo('Nurse removal cancelled by user.');
};

watch(
  () => props.isOpen,
  (isOpen) => {
    if (isOpen) {
      fetchInitialData();
    } else {
      selectedDepartmentId.value = null;
      assignedNurses.value = [];
      availableNurses.value = [];
      nurseToAssignId.value = null;
      showConfirmModal.value = false;
      nurseToRemoveDetails.value = null;
    }
  },
);

const handleSubmit = () => closeModal();
const closeModal = () => emit('close');
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
@use '../../../styles/main.scss' as *;

.department-content {
  margin-top: $spacer-4;
  padding-top: $spacer-4;
  border-top: 1px solid $gray-200;

  h3 {
    color: $text-primary;
    margin-bottom: $spacer-3;
    font-size: 1.1rem;
    font-weight: 600;
  }
}

.nurse-list {
  list-style: none;
  padding: 0;
  margin: $spacer-3 0;
  background: $gray-100;
  border-radius: $btn-border-radius;
  border: 1px solid $gray-200;
  padding: $spacer-2;
  max-height: 200px;
  overflow-y: auto;

  li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacer-2 $spacer-3;
    border-bottom: 1px solid $gray-200;
    transition: $transition;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      background-color: $gray-100;
    }
  }

  .nurse-info {
    flex-grow: 1;
    color: $text-primary;
  }

  .btn-sm {
    margin-left: $spacer-3;
    padding: $spacer-1 $spacer-3;
    font-size: 0.875rem;
  }
}

.no-nurses {
  color: $gray-600;
  font-style: italic;
  text-align: center;
  padding: $spacer-3;
  background: $gray-100;
  border-radius: $btn-border-radius;
}

.assign-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: $spacer-4;

  .btn {
    min-width: 120px;
    padding: $spacer-2 $spacer-4;
  }
}

.form-actions {
  margin-top: $spacer-4;
  display: flex;
  justify-content: flex-end;
  gap: $spacer-3;

  .btn {
    min-width: 120px;
    padding: $spacer-2 $spacer-4;
  }
}

@include responsive(phone) {
  .form-actions {
    flex-direction: column;
    gap: $spacer-2;

    .btn {
      width: 100%;
    }
  }

  .assign-actions {
    .btn {
      width: 100%;
    }
  }
}
</style>
