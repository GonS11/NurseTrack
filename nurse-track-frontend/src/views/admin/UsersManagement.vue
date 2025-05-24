<template>
  <!-- ALL USERS -->
  <Table
    title="Users Management"
    :headers="headers"
    :data="users.content"
    :actions="userActions"
    :show-pagination="true"
    :current-page="users.number"
    :total-pages="users.totalPages"
    :total-items="users.totalElements"
    @page-change="handlePageChange"
  >
    <!--Columnas personalizadas-->
    <template #cell-name="{ item }">
      {{ item.firstname }} {{ item.lastname }}
    </template>
    <template #header-actions>
      <button class="btn-primary" @click="openModal(null)">
        <span class="material-icons">add</span>
        New User
      </button>
    </template>
  </Table>

  <!-- MODAL create/update USER-->
  <UserModal
    :isOpen="showModal"
    :user="selectedUser"
    @close="closeModal"
    @submit="handleSubmit"
  />
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import Table, { type TableAction } from '../../components/ui/Table.vue';
import { useAuthStore } from '../../services';
import { useAdminStore } from '../../stores/admin.store';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';
import UserModal from '../../components/ui/modals/UserModal.vue';

const authStore = useAuthStore();
const adminStore = useAdminStore();
const showModal = ref(false);
const selectedUser = ref<UserResponse | null>(null);

// Columnas
const headers = [
  {
    key: 'name',
    label: 'Name',
  },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Role' },
  {
    key: 'isActive',
    label: 'State',
    format: (v: boolean) => (v ? 'Active' : 'Inactive'),
  },
];

// Acciones de fila (especificamos el tipo genérico)
const userActions = computed(
  () =>
    [
      {
        label: 'Edit',
        icon: 'edit',
        class: 'edit',
        handler: (u: UserResponse) => openModal(u),
      },
      {
        label: 'State',
        icon: (u: UserResponse) => (u.isActive ? 'lock' : 'lock_open'),
        handler: (u: UserResponse) => {
          if (u.isActive) desactivate(u.id);
          else activate(u.id);
        },
        condition: () => authStore.isAdmin,
      },
      {
        label: 'Delete',
        icon: 'delete',
        class: 'danger',
        handler: (u: UserResponse) => remove(u.id),
        condition: () => authStore.isAdmin,
      },
    ] as TableAction<UserResponse>[],
);

// Datos y paginación
const users = computed(() => adminStore.users);

const handlePageChange = (newPage: number) => {
  adminStore.getAllUsers(newPage);
};

//MODAL create/update
const openModal = (user: UserResponse | null) => {
  selectedUser.value = user;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedUser.value = null;
};

const handleSubmit = async (
  formData: CreateUserRequest | UpdateUserRequest,
) => {
  if (!selectedUser.value) {
    await adminStore.createUser(formData as CreateUserRequest);
    // Recargar última página si es necesario
    const isLastPage = users.value.number === users.value.totalPages - 1;
    adminStore.getAllUsers(isLastPage ? users.value.number : 0);
  } else {
    await adminStore.updateUser(
      selectedUser.value.id,
      formData as UpdateUserRequest,
    );
    adminStore.getAllUsers(users.value.number);
  }
  closeModal();
};

// Actions
const activate = async (id: number) => {
  await adminStore.activeUser(id);
};

const desactivate = async (id: number) => {
  await adminStore.desactivateUser(id);
};

const remove = async (id: number) => {
  await adminStore.deleteUser(id);
};

// Al montar, carga la primera página
onMounted(() => {
  adminStore.getAllUsers(0);
});
</script>

<style lang="scss" scoped>
@use 'UsersManagement.scss';
</style>
