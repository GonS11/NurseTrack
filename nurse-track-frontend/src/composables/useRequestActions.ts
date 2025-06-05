import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';
import { RequestStatus } from '../types/enums/status.enum';
import type {
  VacationRequestResponse,
  ShiftChangeResponse,
  UpdateVacationRequest,
  UpdateShiftChangeRequest,
} from '../types/schemas/requests.schema';
import { formatDate } from '../utils/helpers';

export function useRequestActions(
  selectedDepartmentId: Ref<number | null>,
  showAlertMessage: (
    message: string,
    type: 'info' | 'success' | 'warning' | 'error',
    autoClose?: boolean | number,
  ) => void,
  fetchVacationRequests: (
    departmentId: number | null,
    all?: boolean,
  ) => Promise<void>,
  fetchShiftChangeRequests: (
    departmentId: number | null,
    all?: boolean,
  ) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();

  const showApproveConfirmModal: Ref<boolean> = ref(false);
  const approveConfirmMessage: Ref<string> = ref('');
  const requestToApprove: Ref<
    VacationRequestResponse | ShiftChangeResponse | null
  > = ref(null);
  const requestTypeToApprove: Ref<'vacation' | 'shiftChange' | null> =
    ref(null);

  const showRejectModal: Ref<boolean> = ref(false);
  const currentRequestType: Ref<'vacation' | 'shiftChange' | null> = ref(null);
  const currentRequestId: Ref<number | null> = ref(null);
  const rejectNotes: Ref<string> = ref('');

  function confirmApproveVacation(request: VacationRequestResponse) {
    requestToApprove.value = request;
    requestTypeToApprove.value = 'vacation';
    approveConfirmMessage.value = `Are you sure you want to approve this vacation request from ${
      request.requestingNurse.firstname
    } ${request.requestingNurse.lastname} for the period from ${formatDate(
      request.startDate,
    )} to ${formatDate(request.endDate)}?`;
    showApproveConfirmModal.value = true;
  }

  function confirmApproveShiftChange(request: ShiftChangeResponse) {
    requestToApprove.value = request;
    requestTypeToApprove.value = 'shiftChange';
    approveConfirmMessage.value = `Are you sure you want to approve this shift change request from ${request.requestingNurse.firstname} ${request.requestingNurse.lastname}?`;
    showApproveConfirmModal.value = true;
  }

  async function executeApproveRequest() {
    if (
      selectedDepartmentId.value === null ||
      !requestToApprove.value ||
      !requestTypeToApprove.value
    ) {
      console.error('Missing department ID or request data for approval.');
      showAlertMessage(
        'An internal error occurred. Please try again.',
        'error',
      );
      return;
    }

    try {
      if (requestTypeToApprove.value === 'vacation') {
        const updatePayload: UpdateVacationRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Approved by supervisor.',
        };
        await supervisorStore.approveVacationRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchVacationRequests(selectedDepartmentId.value, false);
        showAlertMessage('Vacation request approved successfully!', 'success');
      } else if (requestTypeToApprove.value === 'shiftChange') {
        const updatePayload: UpdateShiftChangeRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Approved by supervisor.',
        };
        await supervisorStore.approveShiftChangeRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchShiftChangeRequests(selectedDepartmentId.value, false);
        showAlertMessage(
          'Shift change request approved successfully!',
          'success',
        );
      }
    } catch (error: any) {
      showAlertMessage(
        `Error approving request: ${error.message || 'Unknown error'}`,
        'error',
      );
      console.error('Error approving request:', error);
    } finally {
      requestToApprove.value = null;
      requestTypeToApprove.value = null;
      showApproveConfirmModal.value = false;
    }
  }

  function cancelApproveOperation() {
    requestToApprove.value = null;
    requestTypeToApprove.value = null;
    showApproveConfirmModal.value = false;
    showAlertMessage('Approval operation cancelled.', 'info');
  }

  function openRejectVacationModal(requestId: number) {
    currentRequestType.value = 'vacation';
    currentRequestId.value = requestId;
    rejectNotes.value = '';
    showRejectModal.value = true;
  }

  async function handleRejectVacation() {
    if (!currentRequestId.value || rejectNotes.value.trim() === '') {
      showAlertMessage('Please provide rejection notes.', 'warning');
      return;
    }
    try {
      if (selectedDepartmentId.value === null) {
        showAlertMessage('No department selected.', 'error');
        closeRejectModal();
        return;
      }
      const updatePayload: UpdateVacationRequest = {
        status: RequestStatus.REJECTED,
        reviewedNotes: rejectNotes.value.trim(),
      };
      await supervisorStore.rejectVacationRequest(
        selectedDepartmentId.value,
        currentRequestId.value,
        updatePayload,
      );
      await fetchVacationRequests(selectedDepartmentId.value, false);
      showAlertMessage('Vacation request rejected successfully!', 'success');
      closeRejectModal();
    } catch (error: any) {
      showAlertMessage(
        `Error rejecting vacation request: ${error.message || 'Unknown error'}`,
        'error',
      );
      console.error('Error rejecting vacation request:', error);
    }
  }

  function openRejectShiftChangeModal(requestId: number) {
    currentRequestType.value = 'shiftChange';
    currentRequestId.value = requestId;
    rejectNotes.value = '';
    showRejectModal.value = true;
  }

  async function handleRejectShiftChange() {
    if (!currentRequestId.value || rejectNotes.value.trim() === '') {
      showAlertMessage('Please provide rejection notes.', 'warning');
      return;
    }
    try {
      if (selectedDepartmentId.value === null) {
        showAlertMessage('No department selected.', 'error');
        closeRejectModal();
        return;
      }
      const updatePayload: UpdateShiftChangeRequest = {
        status: RequestStatus.REJECTED,
        reviewedNotes: rejectNotes.value.trim(),
      };
      await supervisorStore.rejectShiftChangeRequest(
        selectedDepartmentId.value,
        currentRequestId.value,
        updatePayload,
      );
      await fetchShiftChangeRequests(selectedDepartmentId.value, false);
      showAlertMessage(
        'Shift change request rejected successfully!',
        'success',
      );
      closeRejectModal();
    } catch (error: any) {
      showAlertMessage(
        `Error rejecting shift change request: ${
          error.message || 'Unknown error'
        }`,
        'error',
      );
      console.error('Error rejecting shift change request:', error);
    }
  }

  function closeRejectModal() {
    showRejectModal.value = false;
    currentRequestType.value = null;
    currentRequestId.value = null;
    rejectNotes.value = '';
  }

  return {
    showApproveConfirmModal,
    approveConfirmMessage,
    requestToApprove,
    requestTypeToApprove,
    confirmApproveVacation,
    confirmApproveShiftChange,
    executeApproveRequest,
    cancelApproveOperation,
    showRejectModal,
    currentRequestType,
    currentRequestId,
    rejectNotes,
    openRejectVacationModal,
    handleRejectVacation,
    openRejectShiftChangeModal,
    handleRejectShiftChange,
    closeRejectModal,
  };
}
