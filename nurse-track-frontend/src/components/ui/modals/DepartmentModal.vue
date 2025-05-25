<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ item ? 'Edit Department' : 'New Department' }}</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label>Department name</label>
          <input v-model="formData.name" />
          <div v-if="errors.name" class="error">
            {{ errors.name[0] }}
          </div>
        </div>

        <div class="form-group">
          <label>Department location</label>
          <input v-model="formData.location" />
          <div v-if="errors.location" class="error">
            {{ errors.location[0] }}
          </div>
        </div>

        <!--IsActive quitado de aqui aunque se siga mandando su valor en actualizacion-->

        <div class="form-actions">
          <button type="button" @click="close">Cancel</button>
          <button type="submit">
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

//item es department
const props = defineProps<{
  isOpen: boolean;
  item: DepartmentResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

// Define el estado inicial del formulario con valores por defecto
const initialFormData: Omit<CreateDepartmentRequest, 'id' | 'createdAt'> &
  Partial<UpdateDepartmentRequest> = {
  name: '',
  location: '',
  isActive: true,
};

const formData = reactive<typeof initialFormData>({ ...initialFormData });
const errors = reactive<Record<string, string[]>>({});

// createUserMode ya no es necesario, puedes usar `!props.department` directamente
// const createUserMode = computed(() => !props.department);

const resetForm = () => {
  Object.assign(formData, initialFormData); // Restablece a los valores iniciales
  Object.keys(errors).forEach((k) => delete errors[k]); // Limpia errores
};

watch(
  () => props.item,
  (newDepartment) => {
    if (newDepartment) {
      // Si hay un department, estamos editando, poblar el formulario
      Object.assign(formData, {
        name: newDepartment.name,
        location: newDepartment.location,
        isActive: newDepartment.isActive,
      });
    } else {
      resetForm();
    }
  },
  { immediate: true }, // Ejecutar al montar el componente
);

watch(
  () => props.isOpen,
  (newValue) => {
    if (!newValue) {
      // Si el modal se cierra, resetea el formulario y los errores
      resetForm();
    }
  },
);

const submitForm = () => {
  Object.keys(errors).forEach((k) => delete errors[k]);

  const cleanData: any = { ...formData };

  // Si no tiene props.item, es un CREATE, NO añadir isActive
  // Esto está bien, ya lo habías añadido y era correcto.
  if (!props.item) {
    delete cleanData.isActive;
  } else {
    //Solo limpiar campos vacios pero no isActive
    if (cleanData.name === '') cleanData.name = undefined;
    if (cleanData.location === '') cleanData.location = undefined;
  }
  // Si estás en modo edición (props.item existe) y name/location quedaron undefined
  // después de las líneas anteriores, asegúrate de que no se envíen campos nulos
  // a la API si no fueron modificados.
  // Zod se encargará de esto con `.optional()` y `.partial()`.

  const schema = props.item
    ? DepartmentSchemas.update
    : DepartmentSchemas.create;

  const result = schema.safeParse(cleanData);

  if (result.success) {
    emit('submit', result.data);
  } else {
    const flattened = result.error.flatten();
    console.error('Validation errors:', flattened.fieldErrors);
    console.error('Form errors:', flattened.formErrors);
    Object.assign(errors, flattened.fieldErrors);
  }
};

const close = () => {
  emit('close');
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
