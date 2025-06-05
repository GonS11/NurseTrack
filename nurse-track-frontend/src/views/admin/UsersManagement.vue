<template>
  <ManagamentComponent
    title="Users Management"
    :headers="userHeaders"
    :data="users"
    :actions="userActions"
    :modal-component="UserModal"
    create-button-label="New User"
    :fetch-data="getAllUsers"
    :create-item="createUser"
    :update-item="updateUser"
    item-id-key="id"
    ref="managamentComponentRef"
  >
  </ManagamentComponent>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import UserModal from '../../components/ui/modals/UserModal.vue';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';
import { type TableAction } from '../../components/ui/Table.vue';
import { useAuthStore } from '../../services';
import { useAdminStore } from '../../stores/admin.store';
import { useNotifications } from '../../composables/useNotifications';

// --- Stores y estado reactivo ---
const authStore = useAuthStore();
const adminStore = useAdminStore();

// componente ManagementComponen (Para llamar a sus funciones)
const managamentComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

// Notificaciones
const { showSuccess, showError } = useNotifications();

// Datos de la tabla
const users = computed(() => adminStore.users);

const userHeaders = [
  {
    key: 'name',
    label: 'Name',
    // Usa la función format directamente aquí. Recibe el item completo.
    format: (item: UserResponse) => `${item.firstname} ${item.lastname}`,
  },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Role' },
  {
    key: 'isActive',
    label: 'State',
    // Usa la función format para traducir el booleano
    format: (item: UserResponse) => (item.isActive ? 'Active' : 'Inactive'),
  },
];

const userActions = computed(
  () =>
    [
      {
        label: 'Edit',
        icon: 'edit',
        class: 'edit',
        handler: (user: UserResponse) => {
          // Llama a la función expuesta de EntityManagement
          managamentComponentRef.value?.openUpdateModal(user);
        },
      },
      {
        label: 'Toggle State',
        icon: (user: UserResponse) => (user.isActive ? 'lock' : 'lock_open'),
        handler: async (user: UserResponse) => {
          if (user.isActive) {
            try {
              await adminStore.desactivateUser(user.id);
              showSuccess('User deactivated successfully!');
            } catch (error: any) {
              showError(error);
            }
          } else {
            try {
              await adminStore.activateUser(user.id);
              showSuccess('User activated successfully!');
            } catch (error: any) {
              showError(error);
            }
          }
          await getAllUsers(users.value.number); // Refresca la tabla después del toggle
        },
        condition: () => authStore.isAdmin,
      },
      {
        label: 'Delete',
        icon: 'delete',
        class: 'danger',
        handler: async (user: UserResponse) => {
          if (
            confirm(`Are you sure you want to delete user "${user.username}"?`)
          ) {
            try {
              await adminStore.deleteUser(user.id);
              showSuccess('User deleted successfully!');
              await getAllUsers(users.value.number); // Refresca la tabla después de borrar
            } catch (error: any) {
              showError(error);
            }
          }
        },
        condition: () => authStore.isAdmin,
      },
    ] as TableAction<UserResponse>[],
);

// Métodos de CRUD para pasarle al componente
const getAllUsers = async (page: number) => {
  try {
    await adminStore.getAllUsers(page);
  } catch (error: any) {
    showError(error);
  }
};

const createUser = async (formData: CreateUserRequest) => {
  try {
    await adminStore.createUser(formData);
    showSuccess('User created successfully!');
  } catch (error: any) {
    showError(error);
  }
};

const updateUser = async (id: number, formData: UpdateUserRequest) => {
  try {
    await adminStore.updateUser(id, formData);
    showSuccess('User updated successfully!');
  } catch (error: any) {
    showError(error);
  }
};
//Hook de ciclo de vida
onMounted(() => {
  getAllUsers(0); //Cargar la primera pagina al montar
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
