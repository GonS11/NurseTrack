<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ item ? 'Edit User' : 'New User' }}</h2>

      <form @submit.prevent="submitForm">
        <div class="form-fields">
          <Input
            id="firstname"
            label="First Name"
            type="text"
            v-model="formData.firstname"
            placeholder="Enter firstname"
            :error="errors.firstname"
            required
          />

          <Input
            id="lastname"
            label="Last Name"
            type="text"
            v-model="formData.lastname"
            placeholder="Enter lastname"
            :error="errors.lastname"
            required
          />

          <template v-if="!item">
            <Input
              id="username"
              label="Username"
              type="text"
              v-model="formData.username"
              placeholder="Enter username"
              :error="errors.username"
              required
            />

            <Input
              id="email"
              label="Email"
              type="email"
              v-model="formData.email"
              placeholder="Enter email"
              :error="errors.email"
              required
            />

            <Input
              id="password"
              label="Password"
              type="password"
              v-model="formData.password"
              placeholder="Enter password"
              :error="errors.password"
              required
            />
          </template>

          <InputSelect
            id="role"
            label="Role"
            v-model="formData.role"
            text="Select a role"
            :entities="roleOptions"
            item-key="id"
            item-value="id"
            option-value="name"
            :error="errors.role"
            :placeholder-value="null"
            required
          />

          <Input
            v-if="[UserRole.NURSE, UserRole.SUPERVISOR].includes(formData.role as UserRole)"
            id="licenseNumber"
            label="License Number"
            type="text"
            v-model="formData.licenseNumber"
            placeholder="Enter license number"
            :error="errors.licenseNumber"
            required
          />

          <InputSelect
            v-if="item"
            id="isActive"
            label="Status"
            v-model="formData.isActive"
            text="Select status"
            :entities="[
              { id: true, name: 'Active' },
              { id: false, name: 'Inactive' },
            ]"
            item-key="id"
            item-value="id"
            option-value="name"
            :error="errors.isActive"
            :placeholder-value="null"
            required
          />
        </div>

        <div class="form-actions">
          <button type="button" @click="close" class="btn btn-secondary">
            Cancel
          </button>
          <button type="submit" class="btn btn-primary">
            {{ item ? 'Update' : 'Create' }}
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
import {
  UserRole,
  UserRoleData,
  type UserRoleConfig,
} from '../../../types/enums/user-role.enum';
import { useFormErrors } from '../../../utils/formValidation';
import Input from '../Input.vue';
import InputSelect from '../InputSelect.vue';
import { useNotifications } from '../../../composables/useNotifications';

const { showError } = useNotifications();
const props = defineProps<{
  isOpen: boolean;
  item: UserResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

// Define initialFormData para permitir 'null' en el rol si es una nueva creación
const initialFormData: Omit<CreateUserRequest, 'id' | 'createdAt'> &
  Partial<UpdateUserRequest> = {
  firstname: '',
  lastname: '',
  role: UserRole.NURSE,
  licenseNumber: undefined,
  username: '',
  email: '',
  password: '',
  isActive: true,
};

const formData = reactive<typeof initialFormData>({ ...initialFormData });

const { errors, mapZodErrors, clearErrors } = useFormErrors({
  firstname: null,
  lastname: null,
  username: null,
  email: null,
  password: null,
  role: null,
  licenseNumber: null,
  isActive: null,
});

const roleOptions = computed(() => {
  const options = Object.values(UserRole).map((role: UserRole) => {
    const config: UserRoleConfig = UserRoleData[role];
    return {
      id: role,
      name: config.displayName,
    };
  });
  return options;
});

const resetForm = () => {
  Object.assign(formData, initialFormData);
  clearErrors();
};

watch(
  () => props.item,
  (newItem) => {
    if (newItem) {
      Object.assign(formData, {
        firstname: newItem.firstname,
        lastname: newItem.lastname,
        role: newItem.role,
        licenseNumber: newItem.licenseNumber || undefined,
        username: newItem.username,
        email: newItem.email,
        password: '',
        isActive: newItem.isActive,
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

watch(
  () => formData.role,
  (newRole) => {
    // Asegurarse de que el casting sea seguro si newRole puede ser null
    if (![UserRole.NURSE, UserRole.SUPERVISOR].includes(newRole as UserRole)) {
      formData.licenseNumber = undefined;
      if (errors.licenseNumber) {
        errors.licenseNumber = null;
      }
    }
  },
);

const submitForm = () => {
  clearErrors();

  const cleanData: any = { ...formData };

  // Convertir null a undefined para Zod si el campo no es opcional
  if (cleanData.role === null) {
    cleanData.role = undefined;
  }

  if (props.item) {
    delete cleanData.username;
    delete cleanData.email;

    if (cleanData.password === '') {
      delete cleanData.password;
    }

    if (typeof cleanData.isActive === 'string') {
      cleanData.isActive = cleanData.isActive === 'true';
    }
  }

  for (const key in cleanData) {
    if (cleanData[key] === '') {
      cleanData[key] = undefined;
    }
  }

  const schema = props.item ? UserSchemas.update : UserSchemas.create;
  const result = schema.safeParse(cleanData);

  if (result.success) {
    emit('submit', result.data);
  } else {
    mapZodErrors(result.error);
    showError('Please correct the errors.');
  }
};

const close = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
