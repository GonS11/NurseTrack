<template>
  <ManagamentComponent
    title="Departments Management"
    :headers="departmentHeaders"
    :data="departments"
    :actions="departmentActions"
    :modal-component="DepartmentModal"
    create-button-label="New Department"
    :fetch-data="getAllDepartments"
    :create-item="createDepartment"
    :update-item="updateDepartment"
    item-id-key="id"
    ref="managamentComponentRef"
  />
  <ConfirmModal
    v-model="showConfirmModal"
    :message="confirmMessage"
    @confirmed="handleDeleteDepartment"
    @cancelled="showInfo('Operation cancelled')"
  />
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import DepartmentModal from '../../components/ui/modals/DepartmentModal.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import { useAuthStore } from '../../stores/auth.store';
import { useAdminStore } from '../../stores/admin.store';
import { useNotifications } from '../../composables/useNotifications';
import type {
  CreateDepartmentRequest,
  UpdateDepartmentRequest,
  DepartmentResponse,
} from '../../types/schemas/department.schema';
import { type TableAction } from '../../components/ui/Table.vue';

const authStore = useAuthStore();
const adminStore = useAdminStore();
const { showSuccess, showError, showInfo } = useNotifications();

const managamentComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const departmentToDelete = ref<DepartmentResponse | null>(null);

function askDeleteDepartment(department: DepartmentResponse) {
  confirmMessage.value = `Are you sure you want to delete department "${department.name}"?`;
  departmentToDelete.value = department;
  showConfirmModal.value = true;
}

async function handleDeleteDepartment() {
  if (!departmentToDelete.value) return;
  try {
    await adminStore.deleteDepartment(departmentToDelete.value.id);
    showSuccess('Department deleted successfully!');
  } catch (error: any) {
    showError(error);
  } finally {
    showConfirmModal.value = false;
    departmentToDelete.value = null;
  }
}

const departments = computed(() => adminStore.departments);

const departmentHeaders = [
  { key: 'name', label: 'Name' },
  { key: 'location', label: 'Location' },
];

const departmentActions = ref<TableAction<DepartmentResponse>[]>([
  {
    label: 'Edit',
    icon: 'edit',
    class: 'edit',
    handler: (department: DepartmentResponse) => {
      managamentComponentRef.value?.openUpdateModal(department);
    },
  },
  {
    label: 'Toggle State',
    icon: (department: DepartmentResponse) =>
      department.isActive ? 'lock' : 'lock_open',
    handler: async (department: DepartmentResponse) => {
      try {
        if (department.isActive) {
          await adminStore.desactivateUser(department.id);
          showSuccess('Department deactivated successfully!');
        } else {
          await adminStore.activateUser(department.id);
          showSuccess('Department activated successfully!');
        }
      } catch (error: any) {
        showError(error);
      }
      // NO recargues aquí, la store debe hacerlo si es necesario
    },
    condition: () => authStore.isAdmin,
  },
  {
    label: 'Delete',
    icon: 'delete',
    class: 'danger',
    handler: askDeleteDepartment,
    condition: () => authStore.isAdmin,
  },
]);

const getAllDepartments = async (page: number) => {
  try {
    await adminStore.getAllDepartments(page);
  } catch (error: any) {
    showError(error);
  }
};

const createDepartment = async (formData: CreateDepartmentRequest) => {
  try {
    await adminStore.createDepartment(formData);
    showSuccess('Department created successfully!');
  } catch (error: any) {
    showError(error);
  }
};

const updateDepartment = async (
  id: number,
  formData: UpdateDepartmentRequest,
) => {
  try {
    await adminStore.updateDepartment(id, formData);
    showSuccess('Department updated successfully!');
  } catch (error: any) {
    showError(error);
  }
};

onMounted(() => {
  getAllDepartments(0);
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
