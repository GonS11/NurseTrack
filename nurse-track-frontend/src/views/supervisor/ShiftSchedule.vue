<template>
  <div class="shift-schedule-view">
    <h1>Shift Schedule Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
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

      <p v-else-if="isLoadingDepartments">Cargando departamentos...</p>
      <p v-else-if="errorShifts">{{ errorShifts }}</p>
      <p v-else>
        Por favor, selecciona un departamento para ver su horario de turnos.
      </p>
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
import { ref } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useShiftTemplatesStore } from '../../stores/shiftTemplate.store';
import MonthlyCalendar from '../../components/ui/MonthlyCalendar.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import { useShiftConfirmation } from '../../composables/useShiftConfirmation';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import { useNotifications } from '../../composables/useNotifications';
import ShiftFormModal from '../../components/ui/modals/ShiftFormModal.vue';
import type {
  ShiftResponse,
  CreateShiftRequest,
  UpdateShiftRequest,
} from '../../types/schemas/shifts.schema';

const supervisorStore = useSupervisorStore();
const shiftTemplatesStore = useShiftTemplatesStore();
const { showError, showWarning, showSuccess } = useNotifications();

const isLoadingShifts = ref(false);
const errorShifts = ref<string | null>(null);

const isShiftModalOpen = ref(false);
const isEditMode = ref(false);
const selectedDateForModal = ref<string>('');
const shiftToEdit = ref<ShiftResponse | null>(null);

const todayString = new Date().toISOString().split('T')[0];

async function fetchDepartmentShifts(departmentId: number | null) {
  if (!departmentId) return;

  isLoadingShifts.value = true;
  errorShifts.value = null;

  try {
    await supervisorStore.getDepartmentShifts(departmentId);
    await supervisorStore.getDepartmentNurses(departmentId);
    await shiftTemplatesStore.getShiftTemplates();
  } catch (error: any) {
    showError(error.message);
    errorShifts.value = error.message;
  } finally {
    isLoadingShifts.value = false;
  }
}

const onDepartmentSelectedForShifts = async (departmentId: number) => {
  errorShifts.value = null;

  await fetchDepartmentShifts(departmentId);
};

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  currentDepartment,
} = useDepartmentSelection({
  onDepartmentSelected: onDepartmentSelectedForShifts,
});

const {
  showConfirmModal,
  confirmMessage,
  handleShiftCancelledConfirmation,
  executeShiftCancellation,
  cancelShiftCancellationOperation,
} = useShiftConfirmation(selectedDepartmentId, fetchDepartmentShifts);

const handleDateSelectedForShift = (date: string) => {
  if (date < todayString) {
    showWarning('No puedes crear un turno en el pasado.');
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
    showWarning('No puedes editar un turno pasado.');
    return;
  }

  selectedDateForModal.value = shiftDateOnly;
  isEditMode.value = true;
  shiftToEdit.value = shift;
  isShiftModalOpen.value = true;
};

type ShiftSavedPayload = {
  type: 'create' | 'update';
  data: CreateShiftRequest | UpdateShiftRequest;
  shiftId?: number;
};

const handleShiftSaved = async (payload: ShiftSavedPayload) => {
  if (!selectedDepartmentId.value) {
    showWarning('Selecciona un departamento para guardar el turno.');
    return;
  }

  if (payload.type === 'create') {
    payload.data.departmentId = selectedDepartmentId.value;

    await supervisorStore.createShift(
      selectedDepartmentId.value,
      payload.data as CreateShiftRequest,
    );

    showSuccess('¡Turno creado correctamente!');
  } else if (payload.type === 'update' && payload.shiftId) {
    payload.data.departmentId = selectedDepartmentId.value;

    await supervisorStore.updateShift(
      selectedDepartmentId.value,
      payload.shiftId,
      payload.data as UpdateShiftRequest,
    );

    showSuccess('¡Turno actualizado correctamente!');
  }

  await fetchDepartmentShifts(selectedDepartmentId.value);

  isShiftModalOpen.value = false;
};
</script>

<style lang="scss" scoped>
@use 'SupervisorViews.scss';
</style>
