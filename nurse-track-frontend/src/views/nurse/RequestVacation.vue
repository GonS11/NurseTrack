<template>
  <div class="shift-schedule-view">
    <h1>Request Vacation</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :departments="departments"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
    />

    <hr v-if="selectedDepartmentId" />

    <form @submit.prevent="submitVacationRequest" class="request-form">
      <Input
        id="vacation-start"
        label="Start Date"
        type="date"
        v-model="vacationForm.startDate"
        required
      />
      <Input
        id="vacation-end"
        label="End Date"
        type="date"
        v-model="vacationForm.endDate"
        required
      />
      <Input
        id="vacation-reason"
        label="Reason"
        type="text"
        v-model="vacationForm.reason"
        required
      />
      <button type="submit">Request Vacation</button>
    </form>

    <hr />

    <section>
      <h2>My Vacation Requests</h2>
      <div v-if="nurseStore.vacationRequests.length > 0">
        <ul>
          <li v-for="req in nurseStore.vacationRequests" :key="req.id">
            {{ req.startDate }} to {{ req.endDate }} - {{ req.status }}
          </li>
        </ul>
      </div>
      <p v-else>No vacation requests found.</p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useNurseStore } from '../../stores/nurse.store';
import { useAuthStore } from '../../stores/auth.store';
import { useNotifications } from '../../composables/useNotifications';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import Input from '../../components/ui/Input.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import { RequestStatus } from '../../types/enums/status.enum';

const nurseStore = useNurseStore();
const authStore = useAuthStore();
const { showError, showSuccess } = useNotifications();

const nurseId = ref<number | null>(authStore.user?.id ?? null);

const vacationForm = ref({
  startDate: '',
  endDate: '',
  reason: '',
});

const isLoadingDepartments = ref(false);
const errorDepartments = ref<string | null>(null);

const onDepartmentSelected = async () => {};

const { selectedDepartmentId, departments } = useDepartmentSelection({
  store: nurseStore,
  fetchDepartmentsAction: nurseStore.getMyDepartments,
  onDepartmentSelected: onDepartmentSelected,
  userId: nurseId,
});

onMounted(async () => {
  if (!nurseId.value) {
    showError('User not authenticated. Cannot load vacation requests.');
    return;
  }
  try {
    await nurseStore.getMyVacationRequests(nurseId.value);
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(backendMsg || error.message || 'Error loading vacation requests');
  }
});

const supervisorId = computed(() => {
  if (
    !selectedDepartmentId.value ||
    !Array.isArray(nurseStore.departmentSupervisors)
  ) {
    return null;
  }
  const assignment = nurseStore.departmentSupervisors.find(
    (assign) => assign.department.id === selectedDepartmentId.value,
  );
  return assignment?.supervisor.id ?? null;
});

async function submitVacationRequest() {
  if (
    !vacationForm.value.startDate ||
    !vacationForm.value.endDate ||
    !vacationForm.value.reason.trim() ||
    !selectedDepartmentId.value ||
    !supervisorId.value
  ) {
    showError(
      'All fields are required, including department and its assigned supervisor.',
    );
    return;
  }
  if (!nurseId.value) {
    showError('User not authenticated');
    return;
  }
  const startDate = new Date(vacationForm.value.startDate);
  const endDate = new Date(vacationForm.value.endDate);

  if (endDate < startDate) {
    showError('End date must be after start date');
    return;
  }
  try {
    await nurseStore.createVacationRequest(nurseId.value, {
      startDate: vacationForm.value.startDate,
      endDate: vacationForm.value.endDate,
      reason: vacationForm.value.reason,
      status: RequestStatus.PENDING,
      requestingNurseId: nurseId.value,
      reviewedById: supervisorId.value,
    });

    showSuccess('Vacation request sent successfully!');

    vacationForm.value.startDate = '';
    vacationForm.value.endDate = '';
    vacationForm.value.reason = '';
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;

    showError(backendMsg || error.message || 'Error sending vacation request');
  }
}
</script>

<style lang="scss" scoped>
@use '../supervisor/SupervisorViews.scss';
</style>
