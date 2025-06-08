<script setup lang="ts">
import { computed, watch } from 'vue';
import { useFormModal } from '../../../composables/useFormModal';
import { UserSchemas, type CreateUserRequest, type UpdateUserRequest, type UserResponse } from '../../../types/schemas/user.schema';
import { UserRole, UserRoleData, type UserRoleConfig } from '../../../types/enums/user-role.enum';
import Input from '../Input.vue';
import InputSelect from '../InputSelect.vue';

const props = defineProps<{
  isOpen: boolean;
  item: UserResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

const initialFormData: Omit<CreateUserRequest, 'id' | 'createdAt'> &
  Partial<UpdateUserRequest> = {
  firstname: '',
  lastname: '',
  role: null as any,
  licenseNumber: undefined,
  username: '',
  email: '',
  password: '',
  isActive: true,
};

const validate = (data: any) => {
  const schema = props.item ? UserSchemas.update : UserSchemas.create;
  const result = schema.safeParse(data);
  if (result.success) return { success: true, data: result.data };
  return { success: false, errors: result.error.flatten().fieldErrors };
};

const { formData, errors, resetForm, submitForm } = useFormModal(initialFormData, validate);

const roleOptions = computed(() => {
  return Object.values(UserRole).map((role: UserRole) => {
    const config: UserRoleConfig = UserRoleData[role];
    return { id: role, name: config.displayName };
  });
});

const roleOptionText = (entity: { id: UserRole; name: string }) => entity.name;

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
  { immediate: true }
);

watch(
  () => props.isOpen,
  (newVal) => {
    if (!newVal) resetForm();
  }
);

watch(
  () => formData.role,
  (newRole) => {
    if (![UserRole.NURSE, UserRole.SUPERVISOR].includes(newRole as UserRole)) {
      formData.licenseNumber = undefined;
      if (errors.licenseNumber) errors.licenseNumber = null;
    }
  }
);

const onSubmit = () => submitForm(emit);

const close = () => emit('close');
</script>

<template>
  <div v-if="isOpen" class="modal">
    <form @submit.prevent="onSubmit">
      <Input
        id="firstname"
        label="First Name"
        v-model="formData.firstname"
        :error="errors.firstname"
        required
      />
      <Input
        id="lastname"
        label="Last Name"
        v-model="formData.lastname"
        :error="errors.lastname"
        required
      />
      <Input
        id="username"
        label="Username"
        v-model="formData.username"
        :error="errors.username"
        required
      />
      <Input
        id="email"
        label="Email"
        v-model="formData.email"
        :error="errors.email"
        required
      />
      <Input
        id="password"
        label="Password"
        type="password"
        v-model="formData.password"
        :error="errors.password"
        required
      />
      <InputSelect
        id="role"
        label="Role"
        v-model="formData.role"
        :entities="roleOptions"
        :option-value="roleOptionText"
        :error="errors.role"
        required
        itemKey="id"
        itemValue="id"
      />
      <Input
        v-if="[UserRole.NURSE, UserRole.SUPERVISOR].includes(formData.role as UserRole)"
        id="licenseNumber"
        label="License Number"
        v-model="formData.licenseNumber"
        :error="errors.licenseNumber"
        required
      />
      <InputSelect
        v-if="item"
        id="isActive"
        label="Status"
        v-model="formData.isActive"
        :entities="[{ id: true, name: 'Active' }, { id: false, name: 'Inactive' }]"
        :option-value="(e:any)=>e.name"
        :error="errors.isActive"
        required
        itemKey="id"
        itemValue="id"
      />
      <div class="form-actions">
        <button type="button" @click="close" class="btn btn-secondary">Cancel</button>
        <button type="submit" class="btn btn-primary">{{ item ? 'Update' : 'Create' }}</button>
      </div>
    </form>
  </div>
</template>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>