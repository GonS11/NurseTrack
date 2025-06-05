import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';

export function useNurseConfirmation(
  selectedDepartmentId: Ref<number | null>,
  showAlertMessage: (
    message: string,
    type: 'info' | 'success' | 'warning' | 'error',
    autoClose?: boolean | number,
  ) => void,
  fetchNurses: (departmentId: number) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();

  const showConfirmModal: Ref<boolean> = ref(false);
  const confirmMessage: Ref<string> = ref('');
  const nurseIdToRemove: Ref<number | null> = ref(null);

  const handleRemoveNurseConfirmation = (nurseId: number) => {
    nurseIdToRemove.value = nurseId;
    confirmMessage.value =
      'Are you sure you want to remove this nurse from the department?';
    showConfirmModal.value = true;
  };

  const handleRemoveNurse = async () => {
    if (!nurseIdToRemove.value || !selectedDepartmentId.value) {
      showAlertMessage('Could not remove nurse. Missing data.', 'error');
      return;
    }

    try {
      await supervisorStore.removeNurseFromDepartment(
        selectedDepartmentId.value,
        nurseIdToRemove.value,
      );
      showAlertMessage('Nurse removed successfully!', 'success');
      await fetchNurses(selectedDepartmentId.value);
    } catch (error: any) {
      showAlertMessage(
        `Error removing nurse: ${error.message || 'Unknown error'}`,
        'error',
      );
      console.error('Error deleting nurse:', error);
    } finally {
      nurseIdToRemove.value = null;
      showConfirmModal.value = false;
    }
  };

  const cancelRemoveNurse = () => {
    nurseIdToRemove.value = null;
    showConfirmModal.value = false;
    showAlertMessage('Nurse removal operation cancelled.', 'info');
  };

  return {
    showConfirmModal,
    confirmMessage,
    handleRemoveNurseConfirmation,
    handleRemoveNurse,
    cancelRemoveNurse,
  };
}
