<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>Assign Supervisor</h2>

      <form @submit.prevent="submitForm">
        <div class="form-fields">
          <InputSelect
            id="department"
            label="Department"
            :modelValue="formData.departmentId"
            @update:modelValue="updateDepartmentId"
            :text="'Select a department'"
            :entities="unassignedDepartments"
            :item-key="'id'"
            :item-value="'id'"
            option-value="name"
            :error="errors.departmentId"
            :placeholder-value="0"
          />

          <InputSelect
            id="supervisor"
            label="Supervisor"
            :modelValue="formData.supervisorId"
            @update:modelValue="updateSupervisorId"
            :text="'Select a supervisor'"
            :entities="availableSupervisors"
            :item-key="'id'"
            :item-value="'id'"
            :option-value="supervisorOptionText"
            :error="errors.supervisorId"
            :placeholder-value="0"
          />
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
import InputSelect from '../InputSelect.vue';
import { useFormErrors } from '../../../utils/formValidation';
import { useNotifications } from '../../../composables/useNotifications';

const { showError } = useNotifications();

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

const { errors, mapZodErrors, clearErrors } = useFormErrors({
  departmentId: null,
  supervisorId: null,
});

const unassignedDepartments = ref<DepartmentResponse[]>([]);
const availableSupervisors = ref<UserResponse[]>([]);

const fetchDropdownData = async () => {
  try {
    await adminStore.getUnassignedDepartmentsForSupervisor();
    await adminStore.getAllUsers();

    // Manejo seguro de datos (arreglo vs estructura paginada)
    unassignedDepartments.value = Array.isArray(adminStore.departments)
      ? adminStore.departments
      : adminStore.departments?.content || [];

    const users = Array.isArray(adminStore.users)
      ? adminStore.users
      : adminStore.users?.content || [];

    availableSupervisors.value = users.filter(
      (user) => user.role === UserRole.SUPERVISOR,
    );
  } catch (error: any) {
    showError('Error fetching dropdown data:', error);
  }
};

const resetForm = () => {
  Object.assign(formData, initialFormData);
  clearErrors();
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

const updateDepartmentId = (value: string | number | null) => {
  formData.departmentId = value ? Number(value) : 0;
};

const updateSupervisorId = (value: string | number | null) => {
  formData.supervisorId = value ? Number(value) : 0;
};

const supervisorOptionText = (supervisor: UserResponse) => {
  return `${supervisor.firstname} ${supervisor.lastname} (${supervisor.email})`;
};

const submitForm = async () => {
  clearErrors();

  // Convertir a número si es cadena
  const payload = {
    departmentId:
      typeof formData.departmentId === 'string'
        ? parseInt(formData.departmentId)
        : formData.departmentId,
    supervisorId:
      typeof formData.supervisorId === 'string'
        ? parseInt(formData.supervisorId)
        : formData.supervisorId,
  };

  const result = AssignmentSchemas.assignSupervisor.safeParse(payload);

  if (result.success) {
    emit('submit', result.data);
    closeModal();
  } else {
    mapZodErrors(result.error);
    showError('Please correct the errors.');
  }
};

const closeModal = () => {
  resetForm();
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
