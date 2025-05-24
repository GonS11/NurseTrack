<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ createUserMode ? 'New User' : 'Edit User' }}</h2>
      <form @submit.prevent="submitForm">
        <!-- First Name -->
        <div class="form-group">
          <label>First Name</label>
          <input v-model="formData.firstname" />
          <div v-if="errors.firstname" class="error">
            {{ errors.firstname[0] }}
          </div>
        </div>

        <!-- Last Name -->
        <div class="form-group">
          <label>Last Name</label>
          <input v-model="formData.lastname" />
          <div v-if="errors.lastname" class="error">
            {{ errors.lastname[0] }}
          </div>
        </div>

        <!-- Create-only fields -->
        <template v-if="createUserMode">
          <div class="form-group">
            <label>Username</label>
            <input v-model="formData.username" />
            <div v-if="errors.username" class="error">
              {{ errors.username[0] }}
            </div>
          </div>

          <div class="form-group">
            <label>Email</label>
            <input v-model="formData.email" type="email" />
            <div v-if="errors.email" class="error">
              {{ errors.email[0] }}
            </div>
          </div>

          <div class="form-group">
            <label>Password</label>
            <input v-model="formData.password" type="password" />
            <div v-if="errors.password" class="error">
              {{ errors.password[0] }}
            </div>
          </div>
        </template>

        <!-- Edit-only Status -->
        <template v-else>
          <div class="form-group">
            <label>Status</label>
            <select v-model="formData.isActive">
              <option :value="true">Active</option>
              <option :value="false">Inactive</option>
            </select>
          </div>
        </template>

        <!-- Role -->
        <div class="form-group">
          <label>Role</label>
          <select v-model="formData.role">
            <option disabled value="">Select role</option>
            <option
              v-for="role in Object.values(UserRole)"
              :key="role"
              :value="role"
            >
              {{ UserRoleData[role].displayName }}
            </option>
          </select>
          <div v-if="errors.role" class="error">
            {{ errors.role[0] }}
          </div>
        </div>

        <!-- License -->
        <div
          v-if="
            createUserMode &&
            (formData.role === UserRole.NURSE ||
              formData.role === UserRole.SUPERVISOR)
          "
          class="form-group"
        >
          <label>License Number</label>
          <input v-model="formData.licenseNumber" />
          <div v-if="errors.licenseNumber" class="error">
            {{ errors.licenseNumber[0] }}
          </div>
        </div>

        <!-- Form actions -->
        <div class="form-actions">
          <button type="button" @click="close">Cancel</button>
          <button type="submit">
            {{ createUserMode ? 'Create' : 'Update' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, computed } from 'vue';
import {
  UserSchemas,
  type CreateUserRequest,
  type UpdateUserRequest,
  type UserResponse,
} from '../../../types/schemas/user.schema';
import { UserRole, UserRoleData } from '../../../types/enums/user-role.enum';
import type { ZodError } from 'zod';

const props = defineProps<{
  isOpen: boolean;
  user: UserResponse | null;
}>();
const emit = defineEmits(['close', 'submit']);

const createUserMode = computed(() => !props.user);

// Form data
const formData = reactive<
  Omit<CreateUserRequest, 'id' | 'createdAt'> & Partial<UpdateUserRequest>
>({
  firstname: '',
  lastname: '',
  role: UserRole.NURSE,
  licenseNumber: '',
  username: '',
  email: '',
  password: '',
  isActive: true,
});

// Errors object: field -> array of messages
const errors = reactive<Record<string, string[]>>({});

// Reset or fill form when prop.user changes
watch(
  () => props.user,
  (user) => {
    Object.keys(errors).forEach((k) => delete errors[k]);
    if (user) {
      Object.assign(formData, {
        firstname: user.firstname,
        lastname: user.lastname,
        role: user.role,
        licenseNumber: user.licenseNumber || '',
        isActive: user.isActive,
        // no update for username/email/password
      });
    } else {
      Object.assign(formData, {
        firstname: '',
        lastname: '',
        username: '',
        email: '',
        password: '',
        role: UserRole.NURSE,
        licenseNumber: '',
        isActive: true,
      });
    }
  },
  { immediate: true },
);

// On submit, validate and show errors
const submitForm = () => {
  // Clear previous errors
  Object.keys(errors).forEach((k) => delete errors[k]);

  // Choose schema
  const schema = createUserMode.value ? UserSchemas.create : UserSchemas.update;

  const result = schema.safeParse(formData);
  if (result.success) {
    emit('submit', result.data);
    close();
  } else {
    const err = result.error as ZodError;
    // Populate errors[field] = [messages...]
    err.flatten().fieldErrors; // Record<string, string[]>
    Object.assign(errors, err.flatten().fieldErrors);
  }
};

const close = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
