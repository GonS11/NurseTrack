<template>
  <div>
    <h1>Gestión de Departamentos</h1>

    <form @submit.prevent="create" class="inline-form">
      <input v-model="newName" placeholder="Nombre de departamento" required />
      <button type="submit">➕ Crear</button>
    </form>

    <table>
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Activo</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="d in departments.content" :key="d.id">
          <td>{{ d.name }}</td>
          <td>{{ d.isActive ? 'Sí' : 'No' }}</td>
          <td>
            <button @click="toggle(d)">
              {{ d.isActive ? 'Desactivar' : 'Activar' }}
            </button>
            <button @click="del(d.id)">🗑️</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAdminStore } from '../../stores/admin.store';
import type { CreateDepartmentRequest } from '../../types/schemas/department.schema';

const admin = useAdminStore();
const departments = admin.departments;

const newName = ref<string>('');

onMounted(() => {
  admin.getAllDepartments();
});

async function create() {
  const payload: CreateDepartmentRequest = {
    name: newName.value,
    location: newName.value,
  };
  await admin.createDepartment(payload);
  newName.value = '';
}

function toggle(dep: { id: number; isActive: boolean }) {
  if (dep.isActive) {
    admin.desactivateDepartment(dep.id);
  } else {
    admin.activeDepartment(dep.id);
  }
}

function del(id: number) {
  if (confirm('Eliminar departamento?')) {
    admin.deleteDepartment(id);
  }
}
</script>

<style scoped>
.inline-form {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  padding: 0.5rem;
  border: 1px solid #ddd;
}
</style>
