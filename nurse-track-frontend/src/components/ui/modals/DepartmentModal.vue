<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ item ? 'Edit Department' : 'New Department' }}</h2>

      <form @submit.prevent="submitForm">
        <div class="form-fields">
          <Input
            id="name"
            label="Name"
            type="text"
            v-model="formData.name"
            placeholder="Enter department name"
            :error="errors.name"
          />
          <Input
            id="location"
            label="Location"
            type="text"
            v-model="formData.location"
            placeholder="Enter department location"
            :error="errors.location"
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
import { reactive, watch } from 'vue';
import {
  DepartmentSchemas,
  type CreateDepartmentRequest,
  type DepartmentResponse,
  type UpdateDepartmentRequest,
} from '../../../types/schemas/department.schema';
import { useFormErrors } from '../../../utils/formValidation';
import Input from '../Input.vue';

const props = defineProps<{
  isOpen: boolean;
  item: DepartmentResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

const initialFormData: Omit<CreateDepartmentRequest, 'id' | 'createdAt'> &
  Partial<UpdateDepartmentRequest> = {
  name: '',
  location: '',
  isActive: true,
};

const formData = reactive<typeof initialFormData>({ ...initialFormData });
const { errors, mapZodErrors, clearErrors } = useFormErrors({
  name: null,
  location: null,
});

const resetForm = () => {
  Object.assign(formData, initialFormData);
  clearErrors();
};

watch(
  () => props.item,
  (newDepartment) => {
    if (newDepartment) {
      Object.assign(formData, {
        name: newDepartment.name,
        location: newDepartment.location,
        isActive: newDepartment.isActive,
      });
    } else {
      resetForm();
    }
  },
  { immediate: true },
);

watch(
  () => props.isOpen,
  (newValue) => {
    if (!newValue) {
      resetForm();
    }
  },
);

const submitForm = () => {
  clearErrors();

  const cleanData: any = { ...formData };

  if (!props.item) {
    delete cleanData.isActive;
  } else {
    //Solo limpiar campos vacios pero no isActive
    if (cleanData.name === '') cleanData.name = undefined;
    if (cleanData.location === '') cleanData.location = undefined;
  }

  const schema = props.item
    ? DepartmentSchemas.update
    : DepartmentSchemas.create;

  const result = schema.safeParse(cleanData);

  if (result.success) {
    emit('submit', result.data);
  } else {
    mapZodErrors(result.error);
    console.error('Validation errors:', result.error.flatten().fieldErrors);
  }
};

const close = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
