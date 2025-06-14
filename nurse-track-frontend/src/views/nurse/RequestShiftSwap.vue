<template>
  <div class="shift-schedule-view">
    <h1>Request Shift Change</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :departments="departments"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
    />

    <hr v-if="selectedDepartmentId" />

    <div v-if="selectedDepartmentId" class="shift-selection-container">
      <div class="shift-list-section">
        <h2>Your Shifts (To Offer)</h2>
        <p v-if="myShiftsLoading">Loading your shifts...</p>
        <p v-else-if="myShiftsError">{{ myShiftsError }}</p>
        <ul v-else-if="myAvailableShifts.length > 0">
          <li
            v-for="shift in myAvailableShifts"
            :key="shift.id"
            :class="{ selected: offeredShift?.id === shift.id }"
            @click="selectOfferedShift(shift)"
          >
            {{ formatShiftDisplay(shift) }}
          </li>
        </ul>
        <p v-else>No upcoming shifts found in this department to offer.</p>
      </div>

      <div class="shift-list-section">
        <h2>Other Nurses' Shifts (To Desire)</h2>
        <p v-if="otherNursesShiftsLoading">Loading other shifts...</p>
        <p v-else-if="otherNursesShiftsError">{{ otherNursesShiftsError }}</p>
        <ul v-else-if="otherAvailableShifts.length > 0">
          <li
            v-for="shift in otherAvailableShifts"
            :key="shift.id"
            :class="{ selected: desiredShift?.id === shift.id }"
            @click="selectDesiredShift(shift)"
          >
            {{ formatShiftDisplay(shift) }}
          </li>
        </ul>
        <p v-else>No upcoming shifts from other nurses in this department.</p>
      </div>
    </div>

    <hr v-if="selectedDepartmentId" />

    <form @submit.prevent="submitShiftChangeRequest" class="request-form">
      <div class="form-display-summary" v-if="offeredShift || desiredShift">
        <h3>Request Summary:</h3>
        <p v-if="offeredShift">
          **Offering:** {{ formatShiftDisplay(offeredShift) }}
        </p>
        <p v-if="desiredShift">
          **Desiring:** {{ formatShiftDisplay(desiredShift) }}
        </p>
        <p
          v-if="
            offeredShift &&
            desiredShift &&
            offeredShift.nurse.id !== desiredShift.nurse.id
          "
        >
          **With Nurse:** {{ desiredShift.nurse.firstname }}
          {{ desiredShift.nurse.lastname }}
        </p>
      </div>
      <Input
        id="shift-reason"
        label="Reason for Swap"
        type="text"
        v-model="reason"
        required
      />
      <button type="submit" :disabled="!offeredShift || !desiredShift">
        Request Shift Change
      </button>
    </form>

    <hr />

    <section>
      <h2>My Shift Change Requests</h2>
      <div v-if="nurseStore.shiftChangeRequests.length > 0">
        <ul>
          <li v-for="req in nurseStore.shiftChangeRequests" :key="req.id">
            Offered: Shift {{ req.offeredShiftId }} ({{
              req.requestingNurse.firstname
            }}), Desired: Shift {{ req.desiredShiftId }} ({{
              req.receivingNurse.firstname
            }}) - {{ req.status }}
          </li>
        </ul>
      </div>
      <p v-else>No shift change requests found.</p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue';
import { useNurseStore } from '../../stores/nurse.store';
import { useAuthStore } from '../../stores/auth.store';
import { useNotifications } from '../../composables/useNotifications';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';
import Input from '../../components/ui/Input.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import { RequestStatus } from '../../types/enums/status.enum';
import type { ShiftResponse } from '../../types/schemas/shifts.schema';

const nurseStore = useNurseStore();

const authStore = useAuthStore();
const { showError, showSuccess } = useNotifications();

const nurseId = ref<number | null>(null);

const offeredShift = ref<ShiftResponse | null>(null);
const desiredShift = ref<ShiftResponse | null>(null);
const reason = ref('');

const myShiftsLoading = ref(false);
const myShiftsError = ref<string | null>(null);
const otherNursesShiftsLoading = ref(false);
const otherNursesShiftsError = ref<string | null>(null);

watch(
  () => authStore.user?.id,
  (newId) => {
    if (newId) {
      nurseId.value = newId;
    }
  },
  { immediate: true },
);

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  departments,
} = useDepartmentSelection({
  store: nurseStore,
  fetchDepartmentsAction: nurseStore.getMyDepartments,
  onDepartmentSelected: async (departmentId: number) => {
    if (nurseId.value) {
      myShiftsLoading.value = true;
      myShiftsError.value = null;
      try {
        await nurseStore.getDepartmentShifts(nurseId.value, departmentId);
      } catch (error: any) {
        myShiftsError.value = error.message || 'Failed to load your shifts.';
        showError(myShiftsError.value || 'Failed to load your shifts.');
      } finally {
        myShiftsLoading.value = false;
      }

      otherNursesShiftsLoading.value = true;
      otherNursesShiftsError.value = null;
      try {
        await nurseStore.getAllShiftsForDepartment(departmentId);
      } catch (error: any) {
        otherNursesShiftsError.value =
          error.message || "Failed to load other nurses' shifts.";
        showError(
          otherNursesShiftsError.value || 'Failed to load your shifts.',
        );
      } finally {
        otherNursesShiftsLoading.value = false;
      }
    }
  },
  userId: nurseId,
});

const myAvailableShifts = computed<ShiftResponse[]>(() => {
  if (!nurseId.value) return [];
  return nurseStore.shifts
    .filter(
      (s) =>
        s.nurse.id === nurseId.value &&
        s.status === 'SCHEDULED' &&
        new Date(s.shiftDate) >= new Date(new Date().setHours(0, 0, 0, 0)),
    )
    .sort(
      (a, b) =>
        new Date(a.shiftDate).getTime() - new Date(b.shiftDate).getTime(),
    );
});

const otherAvailableShifts = computed<ShiftResponse[]>(() => {
  if (!nurseId.value) return [];
  const allShifts = nurseStore.allDepartmentShifts ?? [];
  return allShifts
    .filter(
      (s) =>
        s.nurse.id !== nurseId.value &&
        s.status === 'SCHEDULED' &&
        new Date(s.shiftDate) >= new Date(new Date().setHours(0, 0, 0, 0)),
    )
    .sort(
      (a, b) =>
        new Date(a.shiftDate).getTime() - new Date(b.shiftDate).getTime(),
    );
});

function selectOfferedShift(shift: ShiftResponse) {
  offeredShift.value = shift;
}

function selectDesiredShift(shift: ShiftResponse) {
  desiredShift.value = shift;
}

function formatShiftDisplay(shift: ShiftResponse | null): string {
  if (!shift) return 'No shift selected';
  const shiftDate = new Date(shift.shiftDate);
  const formattedDate = shiftDate.toLocaleDateString('es-ES', {
    weekday: 'short',
    month: 'numeric',
    day: 'numeric',
  });
  const nurseName = shift.nurse
    ? `${shift.nurse.firstname} ${shift.nurse.lastname}`
    : 'N/A';
  return `${formattedDate} - ${shift.shiftTemplate.name} (${nurseName}) - ID: ${shift.id}`;
}

onMounted(async () => {
  if (!nurseId.value) {
    showError('User not authenticated. Cannot load shift change requests.');
    return;
  }
  try {
    await nurseStore.getMyShiftChangeRequests(nurseId.value);
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(
      backendMsg || error.message || 'Error loading shift change requests',
    );
  }
});

async function submitShiftChangeRequest() {
  if (!offeredShift.value || !desiredShift.value || !reason.value.trim()) {
    showError(
      'Please select both an offered and a desired shift, and provide a reason.',
    );
    return;
  }
  if (!nurseId.value) {
    showError('User not authenticated. Cannot send shift change request.');
    return;
  }

  if (offeredShift.value.department.id !== desiredShift.value.department.id) {
    showError('Shift changes must be within the same department.');
    return;
  }

  if (offeredShift.value.nurse.id === desiredShift.value.nurse.id) {
    showError(
      'You cannot swap a shift with yourself. If you want to give away a shift, please check your options or contact your supervisor.',
    );
    return;
  }

  const payload = {
    requestingNurseId: nurseId.value,
    offeredShiftId: offeredShift.value.id,
    receivingNurseId: desiredShift.value.nurse.id,
    desiredShiftId: desiredShift.value.id,
    reason: reason.value,
    status: RequestStatus.PENDING,
  };

  try {
    await nurseStore.createShiftChangeRequest(nurseId.value, payload);
    showSuccess('Shift change request sent successfully!');
    offeredShift.value = null;
    desiredShift.value = null;
    reason.value = '';
    if (selectedDepartmentId.value && nurseId.value) {
      await nurseStore.getDepartmentShifts(
        nurseId.value,
        selectedDepartmentId.value,
      );
      await nurseStore.getAllShiftsForDepartment(selectedDepartmentId.value);
    }
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(
      backendMsg || error.message || 'Error sending shift change request',
    );
  }
}
</script>

<style lang="scss" scoped>
@use '../supervisor/SupervisorViews.scss';
</style>
