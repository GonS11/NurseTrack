<template>
  <div class="supervisor-department-staff-view">
    <h1>Department and Staff Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="notifications.showError"
    />

    <hr />

    <section class="department-details-section">
      <h2>Department Details</h2>
      <div v-if="currentDepartment" class="department-card">
        <DepartmentDetailCard :department="currentDepartment" />
      </div>
      <p v-else-if="isLoadingDepartments">Loading department details...</p>
      <p v-else-if="errorDepartmentDetails">{{ errorDepartmentDetails }}</p>
      <p v-else>Please select a department or no department is assigned.</p>
    </section>

    <hr class="section-divider" />

    <section class="nurses-section">
      <h2>Nurses in Department</h2>
      <div
        v-if="currentDepartment && supervisorStore.nurseAssignments.length > 0"
      >
        <NurseTable
          :nurses="supervisorStore.nurseAssignments"
          @remove-nurse="handleRemoveNurseConfirmation"
        />
      </div>
      <p v-else-if="isLoadingNurses">Loading nurses...</p>
      <p
        v-else-if="
          currentDepartment && supervisorStore.nurseAssignments.length === 0
        "
      >
        No nurses assigned to this department yet.
      </p>
      <p v-else>Select a department to view its nurses.</p>

      <div v-if="currentDepartment" class="add-nurse-section">
        <h3>Add Nurse to Department</h3>
        <AddNurseToDepartmentForm
          :department-id="currentDepartment.id"
          @nurse-added="handleNurseAdded"
        />
      </div>
    </section>

    <hr class="section-divider" />

    <ConfirmModal
      v-model="showConfirmModal"
      :message="confirmMessage"
      @confirmed="handleRemoveNurse"
      @cancelled="cancelRemoveNurse"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import DepartmentDetailCard from '../../components/ui/supervisor/DepartmentDetailCard.vue';
import NurseTable from '../../components/ui/supervisor/NurseTable.vue';
import AddNurseToDepartmentForm from '../../components/ui/supervisor/AddNurseToDepartmentForm.vue';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import DepartmentSelector from '../../components/requests/DepartmentSelector.vue';
import { useNotifications } from '../../composables/useNotifications';
import { useNurseConfirmation } from '../../composables/useNurseConfirmation';
import { useDepartmentSelection } from '../../composables/useDepartmentSelection';

const supervisorStore = useSupervisorStore();
const notifications = useNotifications();

const isLoadingNurses = ref(false);
const errorDepartmentDetails = ref<string | null>(null);

async function fetchDepartmentNurses(departmentId: number | null) {
  if (departmentId === null) {
    console.warn('Attempting to fetch nurses with a null department ID.');
    supervisorStore.nurseAssignments = [];
    isLoadingNurses.value = false;
    return;
  }

  isLoadingNurses.value = true;
  errorDepartmentDetails.value = null;
  try {
    await supervisorStore.getDepartmentNurses(departmentId);
  } catch (error: any) {
    notifications.showError(
      error.message || 'Error loading department nurses.',
    );
    errorDepartmentDetails.value =
      error.message || 'Error loading department nurses.';
    console.error(
      `Error fetching nurses for department ${departmentId}:`,
      error,
    );
  } finally {
    isLoadingNurses.value = false;
  }
}

const onDepartmentSelectedForStaff = async (departmentId: number | null) => {
  if (departmentId !== null) {
    errorDepartmentDetails.value = null;
    await fetchDepartmentNurses(departmentId);
  } else {
    supervisorStore.nurseAssignments = [];
    isLoadingNurses.value = false;
    errorDepartmentDetails.value = null;
  }
};

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  currentDepartment,
} = useDepartmentSelection({
  onDepartmentSelected: onDepartmentSelectedForStaff,
  showAlertMessage: notifications.showMessage,
});

const {
  showConfirmModal,
  confirmMessage,
  handleRemoveNurseConfirmation,
  handleRemoveNurse,
  cancelRemoveNurse,
} = useNurseConfirmation(
  selectedDepartmentId,
  notifications.showMessage,
  fetchDepartmentNurses,
);

const handleNurseAdded = async () => {
  if (selectedDepartmentId.value) {
    notifications.showSuccess('Nurse added successfully!');
    await fetchDepartmentNurses(selectedDepartmentId.value);
  } else {
    notifications.showWarning('Could not add nurse. Department not selected.');
  }
};
</script>

<style lang="scss" scoped>
@use 'SupervisorViews.scss';
</style>
