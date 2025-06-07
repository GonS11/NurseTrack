<template>
  <div class="request-management">
    <h1>Request Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="notifications.showError"
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
      @approve-request="requestActions.confirmApproveVacation"
      @reject-request="requestActions.openRejectVacationModal"
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
      @approve-request="requestActions.confirmApproveShiftChange"
      @reject-request="requestActions.openRejectShiftChangeModal"
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
      v-model:show="requestActions.showRejectModal.value"
      v-model:notes="requestActions.rejectNotes.value"
      @confirmed="
        requestActions.currentRequestType.value === 'vacation'
          ? requestActions.handleRejectVacation()
          : requestActions.handleRejectShiftChange()
      "
      @cancelled="requestActions.closeRejectModal"
    />

    <ConfirmModal
      v-model="requestActions.showApproveConfirmModal.value"
      :message="requestActions.approveConfirmMessage.value"
      @confirmed="requestActions.executeApproveRequest"
      @cancelled="requestActions.cancelApproveOperation"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useRequestActions } from '../../composables/useRequestActions';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import { useNotifications } from '../../composables/useNotifications';
import { formatDate } from '../../utils/helpers';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import RequestSection from '../../components/requests/RequestSection.vue';
import RejectNotesModal from '../../components/requests/RejectNotesModal.vue';

const supervisorStore = useSupervisorStore();
const notifications = useNotifications();

const isLoadingVacation = ref(false);
const isLoadingShiftChange = ref(false);
const errorVacation = ref<string | null>(null);
const errorShiftChange = ref<string | null>(null);

async function fetchVacationRequests(
  departmentId: number | null,
  all: boolean = false,
) {
  if (departmentId === null) {
    console.warn(
      'Attempting to fetch vacation requests with a null department ID.',
    );
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
    notifications.showError(
      `Failed to load vacation requests: ${error.message || 'Unknown error'}`,
    );
    errorVacation.value = `Failed to load vacation requests: ${
      error.message || 'Unknown error'
    }`;
    console.error(
      `Error fetching vacation requests for department ${departmentId}:`,
      error,
    );
  } finally {
    isLoadingVacation.value = false;
  }
}

async function fetchShiftChangeRequests(
  departmentId: number | null,
  all: boolean = false,
) {
  if (departmentId === null) {
    console.warn(
      'Attempting to fetch shift change requests with a null department ID.',
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
    notifications.showError(
      `Failed to load shift change requests: ${
        error.message || 'Unknown error'
      }`,
    );
    errorShiftChange.value = `Failed to load shift change requests: ${
      error.message || 'Unknown error'
    }`;
    console.error(
      `Error fetching shift change requests for department ${departmentId}:`,
      error,
    );
  } finally {
    isLoadingShiftChange.value = false;
  }
}

const onDepartmentSelectedForRequests = async (departmentId: number | null) => {
  if (departmentId !== null) {
    errorVacation.value = null;
    errorShiftChange.value = null;

    await fetchVacationRequests(departmentId);
    await fetchShiftChangeRequests(departmentId);
  } else {
    supervisorStore.vacationRequests = [];
    supervisorStore.shiftChangeRequests = [];
    isLoadingVacation.value = false;
    isLoadingShiftChange.value = false;
    errorVacation.value = null;
    errorShiftChange.value = null;
  }
};

const { selectedDepartmentId, isLoadingDepartments, errorDepartments } =
  useDepartmentSelection({
    onDepartmentSelected: onDepartmentSelectedForRequests,
    // PASAMOS notifications.displayNotification AQUÍ
    showAlertMessage: notifications.displayNotification,
  });

const requestActions = useRequestActions(
  selectedDepartmentId,
  // PASAMOS notifications.displayNotification AQUÍ
  notifications.displayNotification,
  fetchVacationRequests,
  fetchShiftChangeRequests,
);
</script>

<style scoped lang="scss">
@use 'SupervisorViews.scss';
</style>
