import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';

export function useShiftConfirmation(
  selectedDepartmentId: Ref<number | null>,
  showAlertMessage: (
    message: string,
    type: 'info' | 'success' | 'warning' | 'error',
    autoClose?: boolean | number,
  ) => void,
  fetchShifts: (departmentId: number | null) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();

  const showConfirmModal: Ref<boolean> = ref(false);
  const confirmMessage: Ref<string> = ref('');
  const shiftToCancelId: Ref<number | null> = ref(null);

  const handleShiftCancelledConfirmation = (shiftId: number) => {
    shiftToCancelId.value = shiftId;
    confirmMessage.value = 'Are you sure you want to cancel this shift?';
    showConfirmModal.value = true;
  };

  const executeShiftCancellation = async () => {
    if (!selectedDepartmentId.value || shiftToCancelId.value === null) {
      showAlertMessage('Could not cancel shift. Missing data.', 'error');
      return;
    }

    try {
      await supervisorStore.cancelShift(
        selectedDepartmentId.value,
        shiftToCancelId.value,
      );
      showAlertMessage('Shift cancelled successfully!', 'success');
      await fetchShifts(selectedDepartmentId.value);
    } catch (error: any) {
      showAlertMessage(
        `Error canceling shift: ${error.message || 'Unknown error'}`,
        'error',
      );
      console.error('Error canceling shift:', error);
    } finally {
      shiftToCancelId.value = null;
      showConfirmModal.value = false;
    }
  };

  const cancelShiftCancellationOperation = () => {
    shiftToCancelId.value = null;
    showConfirmModal.value = false;
    showAlertMessage('Shift cancellation operation cancelled.', 'info');
  };

  return {
    showConfirmModal,
    confirmMessage,
    handleShiftCancelledConfirmation,
    executeShiftCancellation,
    cancelShiftCancellationOperation,
  };
}
