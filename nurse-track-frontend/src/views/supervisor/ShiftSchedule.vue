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
          :shifts="supervisorStore.shifts"
          :is-loading="isLoadingShifts"
          :is-interactive="true"
          @date-selected="handleDateSelectedForShift"
          @shift-selected="handleShiftSelectedToEdit"
        />
      </div>

      <p v-else-if="isLoadingDepartments">Loading departments...</p>
      <p v-else-if="errorShifts">{{ errorShifts }}</p>
      <p v-else>Please select a department to view its shift schedule.</p>
    </section>

    <ShiftFormModal
      v-model:is-open="isShiftModalOpen"
      :is-edit-mode="isEditMode"
      :initial-shift-date="selectedDateForModal"
      :shift-to-edit="shiftToEdit"
      :available-nurses="supervisorStore.nurseAssignments"
      :shift-templates="shiftTemplatesStore.shiftTemplates"
      @shift-saved="handleShiftSaved"
      @shift-cancel-requested="handleShiftCancelledConfirmation"
    />

    <ConfirmModal
      v-model="showConfirmModal"
      :message="confirmMessage"
      @confirmed="executeShiftCancellation"
      @cancelled="cancelShiftCancellationOperation"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useShiftTemplatesStore } from '../../stores/shiftTemplate.store';
import { useAuthStore } from '../../stores/auth.store';
import type {
  CreateShiftRequest,
  UpdateShiftRequest,
  ShiftResponse,
} from '../../types/schemas/shifts.schema';
import MonthlyCalendar from '../../components/ui/MonthlyCalendar.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import { useShiftConfirmation } from '../../composables/useShiftConfirmation';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import { useNotifications } from '../../composables/useNotifications';
import ShiftFormModal from '../../components/ui/modals/ShiftFormModal.vue';

const supervisorStore = useSupervisorStore();
const shiftTemplatesStore = useShiftTemplatesStore();
const authStore = useAuthStore();
const notifications = useNotifications();

const isLoadingShifts = ref(false);
const errorShifts = ref<string | null>(null);

const isShiftModalOpen = ref(false);
const isEditMode = ref(false);
const selectedDateForModal = ref<string>('');
const shiftToEdit = ref<ShiftResponse | null>(null);

const todayString = new Date().toISOString().split('T')[0];

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
    await shiftTemplatesStore.getShiftTemplates();
  } catch (error: any) {
    notifications.showError(error.message || 'Error loading department data.');
    errorShifts.value = error.message || 'Error loading department data.';

    console.error(`Error fetching data for department ${departmentId}:`, error);
  } finally {
    isLoadingShifts.value = false;
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
  showAlertMessage: notifications.displayNotification,
});

const {
  showConfirmModal,
  confirmMessage,
  handleShiftCancelledConfirmation,
  executeShiftCancellation,
  cancelShiftCancellationOperation,
} = useShiftConfirmation(
  selectedDepartmentId,
  notifications.displayNotification,
  fetchDepartmentShifts,
);

const handleDateSelectedForShift = (date: string) => {
  if (date < todayString) {
    notifications.showWarning('Cannot create a shift in the past.');
    return;
  }

  selectedDateForModal.value = date;
  isEditMode.value = false;
  shiftToEdit.value = null;
  isShiftModalOpen.value = true;
};

const handleShiftSelectedToEdit = (shift: ShiftResponse) => {
  const shiftDateOnly = shift.shiftDate.split('T')[0];

  if (shiftDateOnly < todayString) {
    notifications.showWarning('Cannot edit a shift that is in the past.');
    return;
  }

  selectedDateForModal.value = shiftDateOnly;
  isEditMode.value = true;
  shiftToEdit.value = shift;
  isShiftModalOpen.value = true;
};

const handleShiftSaved = async (payload: {
  type: 'create' | 'update';
  data: CreateShiftRequest | UpdateShiftRequest;
  shiftId?: number;
}) => {
  if (!selectedDepartmentId.value) {
    notifications.showWarning('Department not selected to save shift.');
    return;
  }

  if (payload.type === 'create') {
    (payload.data as CreateShiftRequest).departmentId =
      selectedDepartmentId.value;

    if (!authStore.user || !authStore.user.id) {
      notifications.showError(
        'You must be logged in as a supervisor to create a shift.',
      );
      return;
    }

    (payload.data as CreateShiftRequest).createdById = authStore.user.id;
  } else {
    (payload.data as UpdateShiftRequest).departmentId =
      selectedDepartmentId.value;
  }

  try {
    if (payload.type === 'create') {
      await supervisorStore.createShift(
        selectedDepartmentId.value,
        payload.data as CreateShiftRequest,
      );

      notifications.showSuccess('Shift created successfully!');
    } else if (payload.type === 'update' && payload.shiftId) {
      await supervisorStore.updateShift(
        selectedDepartmentId.value,
        payload.shiftId,
        payload.data as UpdateShiftRequest,
      );

      notifications.showSuccess('Shift updated successfully!');
    }

    await fetchDepartmentShifts(selectedDepartmentId.value);
  } catch (error: any) {
    notifications.showError(
      error.message ||
        `Error ${payload.type === 'create' ? 'creating' : 'updating'} shift.`,
    );

    console.error(
      `Error ${payload.type === 'create' ? 'creating' : 'updating'} shift:`,
      error,
    );
  }
};

// Observar cuando cambia el departamento seleccionado para recargar los turnos
watch(
  selectedDepartmentId,
  (newDeptId) => {
    if (newDeptId) {
      fetchDepartmentShifts(newDeptId);
    } else {
      supervisorStore.shifts = [];
      supervisorStore.nurseAssignments = [];
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
@use 'SupervisorViews.scss';
</style>
