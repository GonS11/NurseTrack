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
  // Las funciones de fetch ahora esperan un 'number' como se usa en tus vistas, no 'number | null'
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
    approveConfirmMessage.value = `¿Estás seguro de que quieres aprobar esta solicitud de vacaciones de ${
      request.requestingNurse.firstname
    } ${request.requestingNurse.lastname} para el periodo del ${formatDate(
      request.startDate,
    )} al ${formatDate(request.endDate)}?`;
    showApproveConfirmModal.value = true;
  }

  function confirmApproveShiftChange(request: ShiftChangeResponse) {
    requestToApprove.value = request;
    requestTypeToApprove.value = 'shiftChange';
    approveConfirmMessage.value = `¿Estás seguro de que quieres aprobar esta solicitud de cambio de turno de ${request.requestingNurse.firstname} ${request.requestingNurse.lastname}?`;
    showApproveConfirmModal.value = true;
  }

  async function executeApproveRequest() {
    // Asegurarse de que el departamento esté seleccionado antes de continuar
    if (selectedDepartmentId.value === null) {
      showError('Departamento no seleccionado.');
      return;
    }
    if (!requestToApprove.value || !requestTypeToApprove.value) {
      showError('Ocurrió un error interno. Por favor, inténtalo de nuevo.');
      return;
    }

    try {
      if (requestTypeToApprove.value === 'vacation') {
        const updatePayload: UpdateVacationRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Aprobado por el supervisor.',
        };
        await supervisorStore.approveVacationRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchVacationRequests(selectedDepartmentId.value, false); // Pasa el ID directamente
        showSuccess('¡Solicitud de vacaciones aprobada exitosamente!');
      } else if (requestTypeToApprove.value === 'shiftChange') {
        const updatePayload: UpdateShiftChangeRequest = {
          status: RequestStatus.APPROVED,
          reviewedNotes: 'Aprobado por el supervisor.',
        };
        await supervisorStore.approveShiftChangeRequest(
          selectedDepartmentId.value,
          requestToApprove.value.id,
          updatePayload,
        );
        await fetchShiftChangeRequests(selectedDepartmentId.value, false); // Pasa el ID directamente
        showSuccess('¡Solicitud de cambio de turno aprobada exitosamente!');
      }
    } catch (error: any) {
      showError(
        `Error al aprobar la solicitud: ${
          error.message || 'Error desconocido'
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
    showInfo('Operación de aprobación cancelada.');
  }

  function openRejectVacationModal(requestId: number) {
    currentRequestType.value = 'vacation';
    currentRequestId.value = requestId;
    rejectNotes.value = '';
    showRejectModal.value = true;
  }

  async function handleRejectVacation() {
    if (rejectNotes.value.trim() === '') {
      showError('Por favor, proporciona notas de rechazo.');
      return;
    }
    // Asegurarse de que el departamento esté seleccionado y el ID de la solicitud exista
    if (selectedDepartmentId.value === null) {
      showError('Departamento no seleccionado.');
      closeRejectModal();
      return;
    }
    if (currentRequestId.value === null) {
      showError('ID de solicitud de vacaciones no encontrado.');
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
      await fetchVacationRequests(selectedDepartmentId.value, false); // Pasa el ID directamente
      showSuccess('¡Solicitud de vacaciones rechazada exitosamente!');
      closeRejectModal();
    } catch (error: any) {
      showError(
        `Error al rechazar la solicitud de vacaciones: ${
          error.message || 'Error desconocido'
        }`,
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
      showError('Por favor, proporciona notas de rechazo.');
      return;
    }
    // Asegurarse de que el departamento esté seleccionado y el ID de la solicitud exista
    if (selectedDepartmentId.value === null) {
      showError('Departamento no seleccionado.');
      closeRejectModal();
      return;
    }
    if (currentRequestId.value === null) {
      showError('ID de solicitud de cambio de turno no encontrado.');
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
      await fetchShiftChangeRequests(selectedDepartmentId.value, false); // Pasa el ID directamente
      showSuccess('¡Solicitud de cambio de turno rechazada exitosamente!');
      closeRejectModal();
    } catch (error: any) {
      showError(
        `Error al rechazar la solicitud de cambio de turno: ${
          error.message || 'Error desconocido'
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
