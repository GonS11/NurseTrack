<template>
  <div class="shift-schedule-view">
    <h1>Shift Schedule Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="notifications.showError"
    />

    <hr />

    <section class="shift-calendar-section">
      <h2>Monthly Shifts</h2>

      <div v-if="currentDepartment">
        <MonthlyCalendar
          :department-id="currentDepartment.id"
          :shifts="supervisorStore.shifts"
          :is-loading="isLoadingShifts"
          @shift-updated="handleShiftUpdated"
          @shift-cancelled="handleShiftCancelledConfirmation"
          @shift-created="handleShiftCreated"
        />
      </div>

      <p v-else-if="isLoadingDepartments">Loading departments...</p>
      <p v-else-if="errorShifts">{{ errorShifts }}</p>
      <p v-else>Please select a department to view its shift schedule.</p>
    </section>

    <ConfirmModal
      v-model="showConfirmModal"
      :message="confirmMessage"
      @confirmed="executeShiftCancellation"
      @cancelled="cancelShiftCancellationOperation"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import type {
  CreateShiftRequest,
  UpdateShiftRequest,
} from '../../types/schemas/shifts.schema';
import MonthlyCalendar from '../../components/ui/MonthlyCalendar.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';

import { useShiftConfirmation } from '../../composables/useShiftConfirmation';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import { useNotifications } from '../../composables/useNotifications';

const supervisorStore = useSupervisorStore();
const notifications = useNotifications();

const isLoadingShifts = ref(false);
const errorShifts = ref<string | null>(null);

async function fetchDepartmentShifts(departmentId: number | null) {
  if (departmentId === null) {
    console.warn('Attempting to fetch shifts with a null department ID.');
    supervisorStore.shifts = [];
    supervisorStore.nurseAssignments = [];
    isLoadingShifts.value = false;
    return;
  }

  isLoadingShifts.value = true;
  errorShifts.value = null;
  try {
    await supervisorStore.getDepartmentShifts(departmentId);
    await supervisorStore.getDepartmentNurses(departmentId);
  } catch (error: any) {
    notifications.showError(error.message || 'Error loading department data.');
    errorShifts.value = error.message || 'Error loading department data.';
    console.error(`Error fetching data for department ${departmentId}:`, error);
  } finally {
    isLoadingShifts.value = false;
  }
}

async function handleShiftOperation(
  operation: () => Promise<any>,
  successMessage: string,
  errorMessage: string,
  fetchAfterSuccess: boolean = true,
) {
  errorShifts.value = null;
  try {
    await operation();

    notifications.showSuccess(successMessage);
    if (fetchAfterSuccess && selectedDepartmentId.value) {
      await fetchDepartmentShifts(selectedDepartmentId.value);
    }
  } catch (error: any) {
    notifications.showError(error.message);
    errorShifts.value = error.message || errorMessage;
    console.error('Error in shift operation:', error);
  }
}

const onDepartmentSelectedForShifts = async (departmentId: number | null) => {
  if (departmentId !== null) {
    errorShifts.value = null;
    await fetchDepartmentShifts(departmentId);
  } else {
    supervisorStore.shifts = [];
    supervisorStore.nurseAssignments = [];
    isLoadingShifts.value = false;
    errorShifts.value = null;
  }
};

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  currentDepartment,
} = useDepartmentSelection({
  onDepartmentSelected: onDepartmentSelectedForShifts,
  showAlertMessage: notifications.showMessage,
});

const {
  showConfirmModal,
  confirmMessage,
  handleShiftCancelledConfirmation,
  executeShiftCancellation,
  cancelShiftCancellationOperation,
} = useShiftConfirmation(
  selectedDepartmentId,
  notifications.showMessage,
  fetchDepartmentShifts,
);

const handleShiftUpdated = async (payload: {
  shiftId: number;
  request: UpdateShiftRequest;
}) => {
  if (!selectedDepartmentId.value) {
    notifications.showWarning('Department not selected to update shift.');
    return;
  }
  await handleShiftOperation(
    () =>
      supervisorStore.updateShift(
        selectedDepartmentId.value!,
        payload.shiftId,
        payload.request,
      ),
    'Shift updated successfully!',
    'Error updating shift',
  );
};

const handleShiftCreated = async (request: CreateShiftRequest) => {
  if (!selectedDepartmentId.value) {
    notifications.showWarning('Department not selected to create shift.');
    return;
  }
  await handleShiftOperation(
    () => supervisorStore.createShift(selectedDepartmentId.value!, request),
    'Shift created successfully!',
    'Error creating shift',
  );
};
</script>

<style lang="scss" scoped>
@use 'SupervisorViews.scss';
</style>
