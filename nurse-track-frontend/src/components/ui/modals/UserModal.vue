<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal">
      <h2>{{ item ? 'Edit User' : 'New User' }}</h2>
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

        <template v-if="!item">
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

        <div v-if="item" class="form-group">
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
  UserSchemas,
  type CreateUserRequest,
  type UpdateUserRequest,
  type UserResponse,
} from '../../../types/schemas/user.schema';
import { UserRole, UserRoleData } from '../../../types/enums/user-role.enum';

//item es user
const props = defineProps<{
  isOpen: boolean;
  item: UserResponse | null;
}>();

const emit = defineEmits(['close', 'submit']);

// Define el estado inicial del formulario con valores por defecto
const initialFormData: Omit<CreateUserRequest, 'id' | 'createdAt'> &
  Partial<UpdateUserRequest> = {
  firstname: '',
  lastname: '',
  role: UserRole.NURSE,
  licenseNumber: undefined,
  username: '',
  email: '',
  password: '',
  isActive: true, // Por defecto activo para nuevas creaciones
};

const formData = reactive<typeof initialFormData>({ ...initialFormData });
const errors = reactive<Record<string, string[]>>({});

// createUserMode ya no es necesario, puedes usar `!props.item` directamente
// const createUserMode = computed(() => !props.item);

const resetForm = () => {
  Object.assign(formData, initialFormData); // Restablece a los valores iniciales
  Object.keys(errors).forEach((k) => delete errors[k]); // Limpia errores
};

watch(
  () => props.item, // <--- Cambiado de 'user' a 'item'
  (newItem) => {
    if (newItem) {
      // Si hay un item, estamos editando, poblar el formulario
      Object.assign(formData, {
        firstname: newItem.firstname,
        lastname: newItem.lastname,
        role: newItem.role,
        licenseNumber: newItem.licenseNumber || undefined, // Asegúrate de que undefined si es null
        username: newItem.username, // Solo para visualización o si se permitiera editar
        email: newItem.email, // Solo para visualización o si se permitiera editar
        password: '', // Nunca precargues la contraseña
        isActive: newItem.isActive,
      });
      // Para un junior, podrías simplificar quitando username y email si no se editan en el update
      // o dejándolos en el formulario como deshabilitados si solo son de lectura en modo edición.
      // Aquí los he dejado, pero la validación Zod se encargará de si son obligatorios o no en cada modo.
    } else {
      // Si no hay item, es una creación, resetear el formulario
      resetForm();
    }
  },
  { immediate: true }, // Ejecutar al montar el componente
);

watch(
  () => props.isOpen,
  (newVal) => {
    if (!newVal) {
      // Si el modal se cierra, resetea el formulario y los errores
      resetForm();
    }
  },
);

const submitForm = () => {
  Object.keys(errors).forEach((k) => delete errors[k]); // Limpiar errores anteriores

  // Preparar los datos para la validación/envío
  const cleanData: any = { ...formData };
  // Eliminar campos que solo aplican a 'crear' cuando estamos 'editando'
  // o campos vacíos que Zod debería tratar como `undefined` para `partial()`
  if (props.item) {
    // Si estamos editando
    delete cleanData.username;
    delete cleanData.email;
    delete cleanData.password;
  }
  // Convertir campos vacíos a undefined para Zod.optional() o Zod.partial()
  for (const key in cleanData) {
    if (cleanData[key] === '') {
      cleanData[key] = undefined;
    }
  }

  // Determinar qué esquema Zod usar (crear o actualizar)
  const schema = props.item ? UserSchemas.update : UserSchemas.create;
  const result = schema.safeParse(cleanData); // Validar los datos

  if (result.success) {
    // Si la validación es exitosa, emitir el evento 'submit' con los datos validados
    emit('submit', result.data);
  } else {
    // Si la validación falla, poblar el objeto de errores para mostrarlos en el formulario
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
@use 'Modal.scss'; // Mantén tu importación de estilos
</style>
