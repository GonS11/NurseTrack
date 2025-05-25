<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>Assign Supervisor</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="department">Department:</label>
          <select id="department" v-model="formData.departmentId">
            <option :value="0" disabled>Select a department</option>
            <option
              v-for="department in unassignedDepartments"
              :key="department.id"
              :value="department.id"
            >
              {{ department.name }}
            </option>
          </select>
          <div v-if="errors.departmentId" class="error">
            {{ errors.departmentId[0] }}
          </div>
        </div>

        <div class="form-group">
          <label for="supervisor">Supervisor:</label>
          <select id="supervisor" v-model="formData.supervisorId">
            <option :value="0" disabled>Select a supervisor</option>
            <option
              v-for="supervisor in availableSupervisors"
              :key="supervisor.id"
              :value="supervisor.id"
            >
              {{ supervisor.firstname }} {{ supervisor.lastname }} ({{
                supervisor.email
              }})
            </option>
          </select>
          <div v-if="errors.supervisorId" class="error">
            {{ errors.supervisorId[0] }}
          </div>
        </div>

        <div class="form-actions">
          <button type="button" @click="closeModal" class="btn btn-secondary">
            Cancel
          </button>
          <button type="submit" class="btn btn-primary">Assign</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, ref } from 'vue';
import { useAdminStore } from '../../../stores/admin.store';
import {
  AssignmentSchemas,
  type AssignSupervisorRequest,
} from '../../../types/schemas/assignments.schema';
import type { DepartmentResponse } from '../../../types/schemas/department.schema';
import type { UserResponse } from '../../../types/schemas/user.schema';
import { UserRole } from '../../../types/enums/user-role.enum';

const props = defineProps<{
  isOpen: boolean;
  item?: any;
}>();

const emit = defineEmits(['close', 'submit']);

const adminStore = useAdminStore();

const initialFormData: AssignSupervisorRequest = {
  departmentId: 0,
  supervisorId: 0,
};

const formData = reactive<AssignSupervisorRequest>({ ...initialFormData });
const errors = reactive<Record<string, string[]>>({});

const unassignedDepartments = ref<DepartmentResponse[]>([]);
const availableSupervisors = ref<UserResponse[]>([]);

const fetchDropdownData = async () => {
  try {
    // Departments that are currently unassigned to a supervisor.
    await adminStore.getUnassignedDepartmentsForSupervisor();
    unassignedDepartments.value = adminStore.departments.content;

    // A supervisor can be assigned to multiple departments.
    await adminStore.getAllUsers();
    availableSupervisors.value = adminStore.users.content.filter(
      (user) => user.role === UserRole.SUPERVISOR,
    );

    // The following lines were for the old logic where a supervisor could only have one department.
    // They are now removed because a supervisor can be assigned to multiple departments.
    // const currentlyAssignedSupervisorIds = new Set(
    //   adminStore.supervisorAssignments.content.map((sa) => sa.supervisor.id),
    // );
    // availableSupervisors.value = allSupervisors.filter(
    //   (user) => !currentlyAssignedSupervisorIds.has(user.id)
    // );
  } catch (error) {
    console.error('Error fetching dropdown data:', error);
    // You might want to set an error message for the user here.
  }
};

const resetForm = () => {
  Object.assign(formData, initialFormData);
  Object.keys(errors).forEach((k) => delete errors[k]);
};

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      resetForm();
      fetchDropdownData();
    }
  },
  { immediate: true },
);

const submitForm = () => {
  Object.keys(errors).forEach((k) => delete errors[k]);

  const result = AssignmentSchemas.assignSupervisor.safeParse(formData);

  if (result.success) {
    emit('submit', result.data);
    closeModal();
  } else {
    const flattened = result.error.flatten();
    Object.assign(errors, flattened.fieldErrors);
    console.error('Validation errors:', flattened.fieldErrors);
  }
};

const closeModal = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
