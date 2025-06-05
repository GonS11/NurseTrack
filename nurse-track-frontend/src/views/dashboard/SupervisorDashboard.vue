<template>
  <div class="supervisor-dashboard">
    <h1>Supervisor Dashboard</h1>

    <p v-if="isLoadingDepartments" class="loading-message">
      Loading your departments...
    </p>
    <p
      v-else-if="supervisorStore.departments.length === 0"
      class="no-departments-message"
    >
      You are not currently assigned to any departments.
    </p>

    <div v-else class="departments-grid">
      <DepartmentDashboardCard
        v-for="department in supervisorStore.departments"
        :key="department.id"
        :department="department"
        @manage-staff="goToStaffManagement"
        @manage-schedule="goToShiftSchedule"
        @manage-requests="goToRequestManagement"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useSupervisorStore } from '../../stores/supervisor.store';
import DepartmentDashboardCard from '../../components/ui/DepartmentDashboardCard.vue';
import { useNotifications } from '../../composables/useNotifications';

const supervisorStore = useSupervisorStore();
const router = useRouter();
const notifications = useNotifications();

const isLoadingDepartments = ref(false);
const errorDepartments = ref<string | null>(null);

onMounted(async () => {
  isLoadingDepartments.value = true;
  errorDepartments.value = null;
  try {
    await supervisorStore.getAllMyDepartments();
  } catch (error: any) {
    const errorMessage = error.message || 'Failed to load departments.';
    errorDepartments.value = errorMessage;
    notifications.showError(errorMessage);
    console.error('Error fetching departments for dashboard:', error);
  } finally {
    isLoadingDepartments.value = false;
  }
});

const goToStaffManagement = (departmentId: number) => {
  router.push({
    name: 'supervisor-management',
    query: { departmentId: departmentId.toString() },
  });
};

const goToShiftSchedule = (departmentId: number) => {
  router.push({
    name: 'supervisor-shifts',
    query: { departmentId: departmentId.toString() },
  });
};

const goToRequestManagement = (departmentId: number) => {
  router.push({
    name: 'supervisor-requests',
    query: { departmentId: departmentId.toString() },
  });
};
</script>

<style lang="scss" scoped>
@use 'SupervisorDashboard.scss';
</style>
