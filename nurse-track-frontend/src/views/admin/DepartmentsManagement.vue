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
  >
  </ManagamentComponent>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import { type TableAction } from '../../components/ui/Table.vue';
import { useAuthStore } from '../../services';
import { useAdminStore } from '../../stores/admin.store';
import type {
  CreateDepartmentRequest,
  DepartmentResponse,
  UpdateDepartmentRequest,
} from '../../types/schemas/department.schema';
import DepartmentModal from '../../components/ui/modals/DepartmentModal.vue';
import { useNotifications } from '../../composables/useNotifications';

const authStore = useAuthStore();
const adminStore = useAdminStore();

const managamentComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const { showSuccess, showError } = useNotifications();

const departments = computed(() => adminStore.departments);

const departmentHeaders = [
  { key: 'name', label: 'Name' },
  { key: 'location', label: 'Location' },
];

const departmentActions = computed(
  () =>
    [
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
          if (department.isActive) {
            try {
              await adminStore.desactivateDepartment(department.id);
              showSuccess('Department deactivated successfully!');
            } catch (error: any) {
              showError(error);
            }
          } else {
            try {
              await adminStore.activeDepartment(department.id);
              showSuccess('Department activated successfully!');
            } catch (error: any) {
              showError(error);
            }
          }
          await getAllDepartments(departments.value.number);
        },
        condition: () => authStore.isAdmin,
      },
      {
        label: 'Delete',
        icon: 'delete',
        class: 'danger',
        handler: async (department: DepartmentResponse) => {
          if (
            confirm(
              `Are you sure you want to delete user "${department.name}"?`,
            )
          ) {
            try {
              await adminStore.deleteDepartment(department.id);
              showSuccess('Department deleted successfully!');
              await getAllDepartments(departments.value.number);
            } catch (error: any) {
              showError(error);
            }
          }
        },
        condition: () => authStore.isAdmin,
      },
    ] as TableAction<DepartmentResponse>[],
);

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
