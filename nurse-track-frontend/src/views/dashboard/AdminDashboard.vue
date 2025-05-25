<template>
  <div>
    <h1>Admin Dashboard</h1>
    <header></header>
    <div class="cards">
      <div class="card">
        <h2>Usuarios</h2>
        <p>{{ usersPage.totalElements }}</p>
      </div>
      <div class="card">
        <h2>Departamentos</h2>
        <p>{{ departments.totalElements }}</p>
      </div>
      <div class="card">
        <h2>Asign. enfermeras</h2>
        <p>{{ nurseAssignments.totalElements }}</p>
      </div>
      <div class="card">
        <h2>Asign. supervisores</h2>
        <p>{{ supervisorAssignments.totalElements }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useAdminStore } from '../../stores/admin.store';

const admin = useAdminStore();

// Extrae refs reactivas
const { usersPage, departments, nurseAssignments, supervisorAssignments } =
  storeToRefs(admin);

onMounted(async () => {
  await admin.getAllUsers();
  await admin.getAllDepartments();
  await admin.getAllNurseAssignments();
  await admin.getAllSupervisorAssignments();
});
</script>

<style lang="scss" scoped>
@use './AdminDashboard.scss';
</style>
