<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ createUserMode ? 'New User' : 'Edit User' }}</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label>First Name</label>
          <input v-model="formData.firstname" />
          <div v-if="errors.firstname" class="error">
            {{ errors.firstname[0] }}
          </div>
        </div>

        <div class="form-group">
          <label>Last Name</label>
          <input v-model="formData.lastname" />
          <div v-if="errors.lastname" class="error">
            {{ errors.lastname[0] }}
          </div>
        </div>

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

        <div
          v-if="[UserRole.NURSE, UserRole.SUPERVISOR].includes(formData.role)"
          class="form-group"
        >
          <label>License Number</label>
          <input v-model="formData.licenseNumber" />
          <div v-if="errors.licenseNumber" class="error">
            {{ errors.licenseNumber[0] }}
          </div>
        </div>

        <div v-if="!createUserMode" class="form-group">
          <label>Status</label>
          <select v-model="formData.isActive">
            <option :value="true">Active</option>
            <option :value="false">Inactive</option>
          </select>
          <div v-if="errors.isActive" class="error">
            {{ errors.isActive[0] }}
          </div>
        </div>

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
  UserSchemas, // <--- Make sure this import is present!
  type CreateUserRequest,
  type UpdateUserRequest,
  type UserResponse,
} from '../../../types/schemas/user.schema';
import { UserRole, UserRoleData } from '../../../types/enums/user-role.enum';

const props = defineProps<{
  isOpen: boolean;
  user: UserResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

const formData = reactive<
  Omit<CreateUserRequest, 'id' | 'createdAt'> & Partial<UpdateUserRequest>
>({
  firstname: '',
  lastname: '',
  role: UserRole.NURSE,
  licenseNumber: undefined,
  username: '',
  email: '',
  password: '',
  isActive: true,
});

const errors = reactive<Record<string, string[]>>({});
const createUserMode = computed(() => !props.user);

const resetForm = () => {
  Object.assign(formData, {
    firstname: '',
    lastname: '',
    role: UserRole.NURSE,
    licenseNumber: undefined,
    username: '',
    email: '',
    password: '',
    isActive: true,
  });
  Object.keys(errors).forEach((k) => delete errors[k]);
};

watch(
  () => props.user,
  (user) => {
    if (user) {
      Object.assign(formData, {
        firstname: user.firstname,
        lastname: user.lastname,
        role: user.role,
        licenseNumber: user.licenseNumber || undefined,
        username: user.username,
        email: user.email,
        password: '',
        isActive: user.isActive,
      });
    } else {
      resetForm();
    }
  },
  { immediate: true },
);

watch(
  () => props.isOpen,
  (newVal) => {
    if (!newVal) {
      resetForm();
    }
  },
);

const submitForm = () => {
  Object.keys(errors).forEach((k) => delete errors[k]); // Clear previous errors

  // Clean empty fields before validation (crucial for partial updates)
  const cleanData: any = { ...formData };
  // Only remove empty strings if they are not required by the current schema.
  // For Zod's .partial() and .optional(), it handles undefined correctly.
  // If a field is empty string, it should ideally be explicitly `undefined` for partials
  // or validated as an empty string if required.
  // Given your schemas, let's convert empty strings for optional fields to undefined.
  for (const key in cleanData) {
    if (cleanData[key] === '') {
      cleanData[key] = undefined; // Convert empty strings to undefined for optional fields
    }
  }

  // Determine which Zod schema to use (create or update)
  const schema = createUserMode.value ? UserSchemas.create : UserSchemas.update;
  const result = schema.safeParse(cleanData); // Validate the cleaned data

  if (result.success) {
    // If validation passes, emit the 'submit' event with the parsed data
    emit('submit', result.data);
  } else {
    // If validation fails, populate the errors object for display
    const flattened = result.error.flatten();
    Object.assign(errors, flattened.fieldErrors);
    console.error('Validation errors:', flattened.fieldErrors);
  }
};

const close = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
