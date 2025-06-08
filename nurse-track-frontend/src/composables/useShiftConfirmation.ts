import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';
import { useNotifications } from './useNotifications';
import type { ShiftResponse } from '../types/schemas/shifts.schema';

export function useShiftConfirmation(
  selectedDepartmentId: Ref<number | null>,
  fetchShifts: (departmentId: number) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();
  const { showSuccess, showError, showInfo } = useNotifications();

  const showConfirmModal: Ref<boolean> = ref(false);
  const confirmMessage: Ref<string> = ref('');
  const shiftToCancel: Ref<ShiftResponse | null> = ref(null);

  const handleShiftCancelledConfirmation = (shift: ShiftResponse) => {
    shiftToCancel.value = shift;
    const shiftDate = shift.shiftDate.split('T')[0];
    const nurseName = `${shift.nurse.firstname} ${shift.nurse.lastname}`;
    confirmMessage.value = `¿Estás seguro de que quieres cancelar el turno del ${shiftDate} para ${nurseName}?`;
    showConfirmModal.value = true;
  };

  async function executeShiftCancellation() {
    if (!selectedDepartmentId.value) {
      showError('No se ha seleccionado un departamento.');
      cancelShiftCancellationOperation();
      return;
    }
    if (!shiftToCancel.value) {
      showError('No se ha especificado un turno para cancelar.');
      cancelShiftCancellationOperation();
      return;
    }

    try {
      await supervisorStore.cancelShift(
        selectedDepartmentId.value,
        shiftToCancel.value.id,
      );
      showSuccess('¡Turno cancelado exitosamente!');
      await fetchShifts(selectedDepartmentId.value);
    } catch (error: any) {
      showError(
        `Error al cancelar el turno: ${error.message || 'Error desconocido'}`,
      );
    } finally {
      shiftToCancel.value = null;
      showConfirmModal.value = false;
    }
  }

  const cancelShiftCancellationOperation = () => {
    shiftToCancel.value = null;
    showConfirmModal.value = false;
    showInfo('Operación de cancelación de turno cancelada.');
  };

  return {
    showConfirmModal,
    confirmMessage,
    handleShiftCancelledConfirmation,
    executeShiftCancellation,
    cancelShiftCancellationOperation,
  };
}
