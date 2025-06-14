import { ref, type Ref } from 'vue';
import { useSupervisorStore } from '../stores/supervisor.store';
import { useNotifications } from './useNotifications';
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
  fetchVacationRequests: (departmentId: number, all?: boolean) => Promise<void>,
  fetchShiftChangeRequests: (
    departmentId: number,
    all?: boolean,
  ) => Promise<void>,
) {
  const supervisorStore = useSupervisorStore();
  const { showSuccess, showError, showInfo } = useNotifications();

  const showApproveConfirmModal = ref(false);
  const approveConfirmMessage = ref('');
  const requestToApprove = ref<
    VacationRequestResponse | ShiftChangeResponse | null
  >(null);
  const requestTypeToApprove = ref<'vacation' | 'shiftChange' | null>(null);

  const showRejectModal = ref(false);
  const currentRequestType = ref<'vacation' | 'shiftChange' | null>(null);
  const currentRequestId = ref<number | null>(null);
  const rejectNotes = ref('');

  function confirmApproveVacation(request: VacationRequestResponse) {
    requestToApprove.value = request;
    requestTypeToApprove.value = 'vacation';
    approveConfirmMessage.value = `Are you sure you want to approve this vacation request from ${
      request.requestingNurse.firstname
    } ${request.requestingNurse.lastname} for the period of ${formatDate(
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
    if (selectedDepartmentId.value === null) {
      showError('Department not selected.');
      return;
    }
    if (!requestToApprove.value || !requestTypeToApprove.value) {
      showError('An internal error occurred. Please try again.');
      return;
    }

    try {
      if (requestTypeToApprove.value === 'vacation') {
        const updatePayload: UpdateVacationRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Approved by the supervisor.',
        };
        await supervisorStore.approveVacationRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchVacationRequests(selectedDepartmentId.value, false);
        showSuccess('Vacation request successfully approved!');
      } else if (requestTypeToApprove.value === 'shiftChange') {
        const updatePayload: UpdateShiftChangeRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Approved by the supervisor.',
        };
        await supervisorStore.approveShiftChangeRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchShiftChangeRequests(selectedDepartmentId.value, false);
        showSuccess('Shift change request successfully approved!');
      }
    } catch (error: any) {
      const backendMsg = error?.response?.data?.message;
      showError(
        `Error in approving the application: ${
          backendMsg || error.message || 'Unknown error'
        }`,
      );
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
    showInfo('Approval operation cancelled.');
  }

  function openRejectVacationModal(requestId: number) {
    currentRequestType.value = 'vacation';
    currentRequestId.value = requestId;
    rejectNotes.value = '';
    showRejectModal.value = true;
  }

  async function handleRejectVacation() {
    if (rejectNotes.value.trim() === '') {
      showError('Please provide rejection notes.');
      return;
    }

    if (selectedDepartmentId.value === null) {
      showError('Department not selected.');
      closeRejectModal();
      return;
    }

    if (currentRequestId.value === null) {
      showError('Vacation request ID not found.');
      closeRejectModal();
      return;
    }

    try {
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

      showSuccess('Vacation request successfully rejected!');
      closeRejectModal();
    } catch (error: any) {
      showError(
        `Error rejecting vacation request: ${error.message || 'Unknown error'}`,
      );
    }
  }

  function openRejectShiftChangeModal(requestId: number) {
    currentRequestType.value = 'shiftChange';
    currentRequestId.value = requestId;
    rejectNotes.value = '';
    showRejectModal.value = true;
  }

  async function handleRejectShiftChange() {
    if (rejectNotes.value.trim() === '') {
      showError('Please provide rejection notes.');
      return;
    }

    if (selectedDepartmentId.value === null) {
      showError('Department not selected.');
      closeRejectModal();
      return;
    }
    if (currentRequestId.value === null) {
      showError('Shift change request ID not found.');
      closeRejectModal();
      return;
    }

    try {
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
      showSuccess('Shift change request successfully rejected!');
      closeRejectModal();
    } catch (error: any) {
      showError(
        `Error rejecting shift change request: ${
          error.message || 'Unknown error'
        }`,
      );
    }
  }

  function closeRejectModal() {
    showRejectModal.value = false;
    currentRequestType.value = null;
    currentRequestId.value = null;
    rejectNotes.value = '';
  }

  return {
    approveConfirmMessage,
    showApproveConfirmModal,
    confirmApproveVacation,
    confirmApproveShiftChange,
    executeApproveRequest,
    cancelApproveOperation,
    showRejectModal,
    currentRequestType,
    rejectNotes,
    openRejectVacationModal,
    handleRejectVacation,
    openRejectShiftChangeModal,
    handleRejectShiftChange,
    closeRejectModal,
  };
}
