import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';
import { useNotifications } from './useNotifications';

export function useNurseConfirmation(
  selectedDepartmentId: Ref<number | null>,
  fetchNurses: (departmentId: number) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();
  const { showSuccess, showError, showInfo } = useNotifications();

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
      showError('Could not remove nurse. Missing data.');
      return;
    }

    try {
      await supervisorStore.removeNurseFromDepartment(
        selectedDepartmentId.value,
        nurseIdToRemove.value,
      );
      showSuccess('Nurse removed successfully!');
      await fetchNurses(selectedDepartmentId.value);
    } catch (error: any) {
      showError(`Error removing nurse: ${error.message || 'Unknown error'}`);
      console.error('Error deleting nurse:', error);
    } finally {
      nurseIdToRemove.value = null;
      showConfirmModal.value = false;
    }
  };

  const cancelRemoveNurse = () => {
    nurseIdToRemove.value = null;
    showConfirmModal.value = false;
    showInfo('Nurse removal operation cancelled.');
  };

  return {
    showConfirmModal,
    confirmMessage,
    handleRemoveNurseConfirmation,
    handleRemoveNurse,
    cancelRemoveNurse,
  };
}
