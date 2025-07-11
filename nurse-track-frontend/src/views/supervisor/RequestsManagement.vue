<template>
  <div class="request-management">
    <h1>Request Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :departments="departments"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
    />

    <hr />

    <RequestSection
      title="Vacation Requests"
      :requests="supervisorStore.vacationRequests"
      :is-loading="isLoadingVacation"
      :error="errorVacation"
      :selected-department-id="selectedDepartmentId"
      request-type="vacation"
      @fetch-requests="fetchVacationRequests"
      @approve-request="confirmApproveVacation"
      @reject-request="openRejectVacationModal"
    >
      <template #request-card-content="{ request }">
        <p>
          <strong>Nurse:</strong> {{ request.requestingNurse.firstname }}
          {{ request.requestingNurse.lastname }}
        </p>
        <p>
          <strong>Period:</strong> {{ formatDate(request.startDate) }} to
          {{ formatDate(request.endDate) }} ({{ request.durationDays }} days)
        </p>
        <p><strong>Reason:</strong> {{ request.reason }}</p>
        <p v-if="request.reviewedNotes">
          <strong>Notes:</strong> {{ request.reviewedNotes }}
        </p>
        <p v-if="request.reviewedBy">
          <strong>Reviewed By:</strong> {{ request.reviewedBy.firstname }}
          {{ request.reviewedBy.lastname }}
        </p>
      </template>
    </RequestSection>

    <hr />

    <RequestSection
      title="Shift Change Requests"
      :requests="supervisorStore.shiftChangeRequests"
      :is-loading="isLoadingShiftChange"
      :error="errorShiftChange"
      :selected-department-id="selectedDepartmentId"
      request-type="shiftChange"
      @fetch-requests="fetchShiftChangeRequests"
      @approve-request="confirmApproveShiftChange"
      @reject-request="openRejectShiftChangeModal"
    >
      <template #request-card-content="{ request }">
        <p>
          <strong>Requesting Nurse:</strong>
          {{ request.requestingNurse.firstname }}
          {{ request.requestingNurse.lastname }}
        </p>
        <p><strong>Offered Shift ID:</strong> {{ request.offeredShiftId }}</p>
        <p>
          <strong>Receiving Nurse:</strong>
          {{ request.receivingNurse.firstname }}
          {{ request.receivingNurse.lastname }}
        </p>
        <p><strong>Desired Shift ID:</strong> {{ request.desiredShiftId }}</p>
        <p><strong>Reason:</strong> {{ request.reason }}</p>
        <p v-if="request.reviewedNotes">
          <strong>Notes:</strong> {{ request.reviewedNotes }}
        </p>
        <p v-if="request.reviewedBy">
          <strong>Reviewed By:</strong> {{ request.reviewedBy.firstname }}
          {{ request.reviewedBy.lastname }}
        </p>
      </template>
    </RequestSection>

    <RejectNotesModal
      v-model:show="showRejectModal"
      v-model:notes="rejectNotes"
      @confirmed="
        currentRequestType === 'vacation'
          ? handleRejectVacation()
          : handleRejectShiftChange()
      "
      @cancelled="closeRejectModal"
    />

    <ConfirmModal
      v-model="showApproveConfirmModal"
      :message="approveConfirmMessage"
      @confirmed="executeApproveRequest"
      @cancelled="cancelApproveOperation"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import { useAuthStore } from '../../stores/auth.store';
import { useRequestActions } from '../../composables/useRequestActions';
import { formatDate } from '../../utils/helpers';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import RequestSection from '../../components/requests/RequestSection.vue';
import RejectNotesModal from '../../components/requests/RejectNotesModal.vue';
import { useNotifications } from '../../composables/useNotifications';

const supervisorStore = useSupervisorStore();
const { showError, showWarning } = useNotifications();
const authStore = useAuthStore();
const authenticatedUserId = ref<number | null>(authStore.user?.id ?? null);

const isLoadingVacation = ref(false);
const isLoadingShiftChange = ref(false);
const errorVacation = ref<string | null>(null);
const errorShiftChange = ref<string | null>(null);

async function fetchVacationRequests(
  departmentId: number | null,
  all: boolean = false,
) {
  if (departmentId === null) {
    showWarning('Trying to get vacation requests with a null department ID.');
    supervisorStore.vacationRequests = [];
    isLoadingVacation.value = false;
    return;
  }

  isLoadingVacation.value = true;
  errorVacation.value = null;

  try {
    if (all) {
      await supervisorStore.getAllVacationRequests(departmentId);
    } else {
      await supervisorStore.getPendingVacationRequests(departmentId);
    }
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(backendMsg || error.message || 'Error loading data');
  } finally {
    isLoadingVacation.value = false;
  }
}

async function fetchShiftChangeRequests(
  departmentId: number | null,
  all: boolean = false,
) {
  if (departmentId === null) {
    showWarning(
      'Trying to get shift change requests with a null department ID.',
    );
    supervisorStore.shiftChangeRequests = [];
    isLoadingShiftChange.value = false;
    return;
  }

  isLoadingShiftChange.value = true;
  errorShiftChange.value = null;

  try {
    if (all) {
      await supervisorStore.getAllShiftChangeRequests(departmentId);
    } else {
      await supervisorStore.getPendingShiftChangeRequests(departmentId);
    }
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(backendMsg || error.message || 'Error loading data');
    errorShiftChange.value = error.message;
  } finally {
    isLoadingShiftChange.value = false;
  }
}

const onDepartmentSelectedForRequests = async (departmentId: number) => {
  errorVacation.value = null;
  errorShiftChange.value = null;

  await fetchVacationRequests(departmentId);
  await fetchShiftChangeRequests(departmentId);
};

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  departments,
} = useDepartmentSelection({
  store: supervisorStore,
  fetchDepartmentsAction: supervisorStore.getAllMyDepartments,
  onDepartmentSelected: onDepartmentSelectedForRequests,
  userId: authenticatedUserId,
});

const {
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
} = useRequestActions(
  selectedDepartmentId,
  fetchVacationRequests,
  fetchShiftChangeRequests,
);
</script>

<style scoped lang="scss">
@use 'SupervisorViews.scss';
</style>
