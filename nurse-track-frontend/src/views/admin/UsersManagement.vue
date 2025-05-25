<template>
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
    <template #cell-name="{ item }">
      {{ item.firstname }} {{ item.lastname }}
    </template>
    <template #header-actions>
      <button class="btn-primary" @click="openModalForCreate">
        <span class="material-icons">add</span>
        New User
      </button>
    </template>
  </Table>

  <UserModal
    :isOpen="showModal"
    :user="selectedUser"
    @close="closeModal"
    @submit="handleModalSubmit"
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

// --- Stores and Reactive State ---
const authStore = useAuthStore(); // Access the authentication store for admin checks
const adminStore = useAdminStore(); // Access the admin store for user data and actions

const showModal = ref(false); // Controls the visibility of the UserModal
const selectedUser = ref<UserResponse | null>(null); // Stores the user data for editing, or null for creating

// --- Table Configuration ---
// Defines the columns for the user table
const headers = [
  { key: 'name', label: 'Name' }, // Uses custom slot for full name
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Role' },
  {
    key: 'isActive',
    label: 'State',
    format: (value: boolean) => (value ? 'Active' : 'Inactive'), // Formats boolean to readable text
  },
];

// Defines the actions available for each row in the table
const userActions = computed(
  () =>
    [
      {
        label: 'Edit',
        icon: 'edit',
        class: 'edit', // CSS class for styling
        handler: (user: UserResponse) => openModalForEdit(user), // Opens modal to edit this user
      },
      {
        label: 'Toggle State',
        icon: (user: UserResponse) => (user.isActive ? 'lock' : 'lock_open'), // Dynamic icon based on user's active state
        handler: async (user: UserResponse) => {
          if (user.isActive) {
            await adminStore.desactivateUser(user.id);
          } else {
            await adminStore.activateUser(user.id);
          }
          // Refresh the user list after state change to show updated status
          adminStore.getAllUsers(adminStore.usersPage.number);
        },
        condition: () => authStore.isAdmin, // Only visible if the current user is an admin
      },
      {
        label: 'Delete',
        icon: 'delete',
        class: 'danger', // CSS class for styling
        handler: async (user: UserResponse) => {
          if (
            confirm(`Are you sure you want to delete user "${user.username}"?`)
          ) {
            await adminStore.deleteUser(user.id);
            // Refresh the user list after deletion to keep pagination correct
            adminStore.getAllUsers(adminStore.usersPage.number);
          }
        },
        condition: () => authStore.isAdmin, // Only visible if the current user is an admin
      },
    ] as TableAction<UserResponse>[], // Type assertion for clarity
);

// Access user data from the admin store
const users = computed(() => adminStore.usersPage);

// --- Pagination Handling ---
// Called when the page changes in the Table component
const handlePageChange = (newPage: number) => {
  adminStore.getAllUsers(newPage); // Fetch users for the new page
};

// --- Modal Management ---
// Opens the modal for creating a new user
const openModalForCreate = () => {
  selectedUser.value = null; // No user selected, indicates create mode
  showModal.value = true;
};

// Opens the modal for editing an existing user
const openModalForEdit = (user: UserResponse) => {
  selectedUser.value = user; // Set the user to be edited
  showModal.value = true;
};

// Closes the modal and resets the selected user
const closeModal = () => {
  showModal.value = false;
  selectedUser.value = null; // Clear selected user after closing
};

// Handles the form submission from the UserModal (create or update)
const handleModalSubmit = async (
  formData: CreateUserRequest | UpdateUserRequest,
) => {
  try {
    if (!selectedUser.value) {
      // If no user is selected, it's a create operation
      await adminStore.createUser(formData as CreateUserRequest);
    } else {
      // If a user is selected, it's an update operation
      await adminStore.updateUser(
        selectedUser.value.id,
        formData as UpdateUserRequest,
      );
    }
    closeModal(); // Close modal on successful submission
    // Refresh the user list to show the new/updated user and ensure data consistency
    adminStore.getAllUsers(adminStore.usersPage.number);
  } catch (error) {
    // Show an error message if the operation fails
    alert('Error saving user. Please check the data and try again.');
    // Do not close the modal, allowing the user to correct errors
  }
};

// --- Lifecycle Hook ---
// Fetches the initial page of users when the component is mounted
onMounted(() => {
  adminStore.getAllUsers(0); // Load the first page (page 0)
});
</script>

<style lang="scss" scoped>
@use 'UsersManagement.scss'; // Imports specific styles for this component
</style>
