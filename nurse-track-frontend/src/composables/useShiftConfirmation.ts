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
    confirmMessage.value = `Are you sure you want to cancel the ${shiftDate} to ${nurseName}?`;
    showConfirmModal.value = true;
  };

  async function executeShiftCancellation() {
    if (!selectedDepartmentId.value) {
      showError('A department has not been selected.');
      cancelShiftCancellationOperation();
      return;
    }
    if (!shiftToCancel.value) {
      showError('No shift has been specified for cancellation.');
      cancelShiftCancellationOperation();
      return;
    }

    try {
      await supervisorStore.cancelShift(
        selectedDepartmentId.value,
        shiftToCancel.value.id,
      );
      showSuccess('Shift successfully cancelled!');
      await fetchShifts(selectedDepartmentId.value);
    } catch (error: any) {
      const backendMsg = error?.response?.data?.message;
      showError(
        `Error when canceling the shift: ${
          backendMsg || error.message || 'Unknown error'
        }`,
      );
    } finally {
      shiftToCancel.value = null;
      showConfirmModal.value = false;
    }
  }

  const cancelShiftCancellationOperation = () => {
    shiftToCancel.value = null;
    showConfirmModal.value = false;
    showInfo('Shift cancellation operation cancelled.');
  };

  return {
    showConfirmModal,
    confirmMessage,
    handleShiftCancelledConfirmation,
    executeShiftCancellation,
    cancelShiftCancellationOperation,
  };
}
