<template>
  <div class="shift-schedule-view">
    <h1>My Schedule</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :departments="departments"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
    />

    <hr />

    <section class="shift-calendar-section">
      <h2>Monthly Shifts</h2>

      <div v-if="currentDepartment">
        <MonthlyCalendar
          :shifts="nurseStore.shifts"
          :is-loading="isLoadingShifts"
          :is-interactive="false"
        />
      </div>

      <p v-else-if="isLoadingDepartments">Loading departments...</p>
      <p v-else-if="errorDepartments">{{ errorDepartments }}</p>
      <p
        v-else-if="
          departments.length === 0 && !isLoadingDepartments && !errorDepartments
        "
      >
        No departments found for your account.
      </p>
      <p v-else>Please select a department to view your shifts.</p>
    </section>

    <hr />

    <div class="requests-container">
      <section class="requests-container--section">
        <router-link to="/nurse/nurse-vacation" class="btn-primary">
          Request Vacation
        </router-link>
      </section>

      <section class="requests-container--section">
        <router-link to="/nurse/request-shift-swap" class="btn-primary">
          Request Shift Change
        </router-link>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useNurseStore } from '../../stores/nurse.store';
import { useAuthStore } from '../../stores/auth.store';
import { useNotifications } from '../../composables/useNotifications';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import MonthlyCalendar from '../../components/ui/MonthlyCalendar.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';

const nurseStore = useNurseStore();
const authStore = useAuthStore();
const { showError } = useNotifications();

const isLoadingShifts = ref(false);
const errorShifts = ref<string | null>(null);
const authenticatedUserId = ref<number | null>(null);

// 1. Define loadDepartmentShifts BEFORE useDepartmentSelection
const loadDepartmentShifts = async (departmentId: number) => {
  if (!authenticatedUserId.value || !departmentId) {
    console.warn(
      'Skipping loadDepartmentShifts: authenticatedUserId or departmentId is missing.',
    );
    return;
  }
  isLoadingShifts.value = true;
  errorShifts.value = null;
  try {
    await nurseStore.getDepartmentShifts(
      authenticatedUserId.value,
      departmentId,
    );
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(backendMsg || error.message || 'Error loading shifts');
    errorShifts.value = error.message;
  } finally {
    isLoadingShifts.value = false;
  }
};

// 2. Now call useDepartmentSelection
const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  currentDepartment,
  departments,
} = useDepartmentSelection({
  store: nurseStore,
  fetchDepartmentsAction: nurseStore.getMyDepartments,
  onDepartmentSelected: loadDepartmentShifts,
  userId: authenticatedUserId,
});

// 3. Watch for authenticatedUserId and force department loading if needed
watch(
  () => authenticatedUserId.value,
  async (newUserId) => {
    if (newUserId) {
      try {
        await nurseStore.getMyDepartments(newUserId);
        // Si hay departamentos, selecciona el primero por defecto
        if (nurseStore.departments.length > 0 && !selectedDepartmentId.value) {
          selectedDepartmentId.value = nurseStore.departments[0].id;
        }
      } catch (error: any) {
        const backendMsg = error?.response?.data?.message;
        showError(
          backendMsg || error.message || 'Error loading initial departments.',
        );
      }
    }
  },
  { immediate: true },
);

onMounted(async () => {
  if (authStore.user?.id) {
    authenticatedUserId.value = authStore.user.id;

    // Vacation and shift change requests don't depend on department selection
    try {
      await nurseStore.getMyVacationRequests(authenticatedUserId.value);
      await nurseStore.getMyShiftChangeRequests(authenticatedUserId.value);
    } catch (error: any) {
      const backendMsg = error?.response?.data?.message;
      showError(backendMsg || error.message || 'Error loading requests');
    }
  } else {
    showError('User not authenticated. Cannot load schedule.');
  }
});
</script>

<style lang="scss" scoped>
@use '../supervisor/SupervisorViews.scss';
@use './MySchedule.scss';
</style>
