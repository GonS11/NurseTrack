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
  />
  <ConfirmModal
    v-model="showConfirmModal"
    :message="confirmMessage"
    @confirmed="handleDeleteUser"
    @cancelled="showInfo('Operation cancelled')"
  />
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import UserModal from '../../components/ui/modals/UserModal.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import { useAuthStore } from '../../stores/auth.store';
import { useAdminStore } from '../../stores/admin.store';
import { useNotifications } from '../../composables/useNotifications';
import type {
  CreateUserRequest,
  UpdateUserRequest,
  UserResponse,
} from '../../types/schemas/user.schema';
import { type TableAction } from '../../components/ui/Table.vue';

const authStore = useAuthStore();
const adminStore = useAdminStore();
const { showSuccess, showError, showInfo } = useNotifications();

const managamentComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const userToDelete = ref<UserResponse | null>(null);

function askDeleteUser(user: UserResponse) {
  confirmMessage.value = `Are you sure you want to delete user "${user.username}"?`;
  userToDelete.value = user;
  showConfirmModal.value = true;
}

async function handleDeleteUser() {
  if (!userToDelete.value) return;
  try {
    await adminStore.deleteUser(userToDelete.value.id);
    showSuccess('User deleted successfully!');
  } catch (error: any) {
    showError(error);
  } finally {
    showConfirmModal.value = false;
    userToDelete.value = null;
  }
}

const users = computed(() => adminStore.users);

const userHeaders = [
  {
    key: 'name',
    label: 'Name',
    format: (item: UserResponse) => `${item.firstname} ${item.lastname}`,
  },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Role' },
  {
    key: 'isActive',
    label: 'State',
    format: (item: UserResponse) => (item.isActive ? 'Active' : 'Inactive'),
  },
];

const userActions = ref<TableAction<UserResponse>[]>([
  {
    label: 'Edit',
    icon: 'edit',
    class: 'edit',
    handler: (user: UserResponse) => {
      managamentComponentRef.value?.openUpdateModal(user);
    },
  },
  {
    label: 'Toggle State',
    icon: (user: UserResponse) => (user.isActive ? 'lock' : 'lock_open'),
    handler: async (user: UserResponse) => {
      try {
        if (user.isActive) {
          await adminStore.desactivateUser(user.id);
          showSuccess('User deactivated successfully!');
        } else {
          await adminStore.activateUser(user.id);
          showSuccess('User activated successfully!');
        }
      } catch (error: any) {
        showError(error);
      }
    },
    condition: () => authStore.isAdmin,
  },
  {
    label: 'Delete',
    icon: 'delete',
    class: 'danger',
    handler: askDeleteUser,
    condition: () => authStore.isAdmin,
  },
]);

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

onMounted(() => {
  getAllUsers(0);
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
