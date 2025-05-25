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

// --- Stores y estado reactivo ---
const authStore = useAuthStore();
const adminStore = useAdminStore();

// componente ManagementComponen (Para llamar a sus funciones)
const managamentComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

// Datos de la tabla
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
          // Llama a la función expuesta de EntityManagement
          managamentComponentRef.value?.openUpdateModal(department);
        },
      },
      {
        label: 'Toggle State',
        icon: (department: DepartmentResponse) =>
          department.isActive ? 'lock' : 'lock_open',
        handler: async (department: DepartmentResponse) => {
          if (department.isActive) {
            await adminStore.desactivateDepartment(department.id);
          } else {
            await adminStore.activeDepartment(department.id);
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
            await adminStore.deleteDepartment(department.id);
            await getAllDepartments(departments.value.number); // Refresca la tabla después de borrar
          }
        },
        condition: () => authStore.isAdmin,
      },
    ] as TableAction<DepartmentResponse>[],
);

// Metodos de CRUD para pasarle al componente
const getAllDepartments = async (page: number) => {
  await adminStore.getAllDepartments(page);
};

const createDepartment = async (formData: CreateDepartmentRequest) => {
  await adminStore.createDepartment(formData);
};

const updateDepartment = async (
  id: number,
  formData: UpdateDepartmentRequest,
) => {
  // Asegúrate de que el ID sea string aquí también
  await adminStore.updateDepartment(id, formData);
};
//Hook de ciclo de vida
onMounted(() => {
  getAllDepartments(0); //Cargar la primera pagina al montar
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
