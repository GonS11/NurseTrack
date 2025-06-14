<template>
  <div class="supervisor-department-staff-view">
    <h1>Department and Staff Management</h1>

    <DepartmentSelector
      v-model:selected-department-id="selectedDepartmentId"
      :departments="departments"
      :is-loading-departments="isLoadingDepartments"
      :error-departments="errorDepartments"
      @error-alert="showError"
    />

    <hr />

    <section class="department-details-section">
      <h2>Department Details</h2>
      <div v-if="currentDepartment" class="department-card">
        <DepartmentDetailCard :department="currentDepartment" />
      </div>
      <p v-else-if="isLoadingDepartments">
        Cargando detalles del departamento...
      </p>
      <p v-else-if="errorDepartmentDetails">{{ errorDepartmentDetails }}</p>
      <p v-else>
        Por favor, selecciona un departamento o no hay departamento asignado.
      </p>
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
      <p v-else-if="isLoadingNurses">Cargando enfermeras...</p>
      <p
        v-else-if="
          currentDepartment && supervisorStore.nurseAssignments.length === 0
        "
      >
        Aún no hay enfermeras asignadas a este departamento.
      </p>
      <p v-else>Selecciona un departamento para ver sus enfermeras.</p>

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
import { useAuthStore } from '../../stores/auth.store';

const supervisorStore = useSupervisorStore();
const { showError, showWarning, showSuccess } = useNotifications();
const authStore = useAuthStore();
const authenticatedUserId = ref<number | null>(authStore.user?.id ?? null);

const isLoadingNurses = ref(false);
const errorDepartmentDetails = ref<string | null>(null);

async function fetchDepartmentNurses(departmentId: number) {
  isLoadingNurses.value = true;
  errorDepartmentDetails.value = null;
  try {
    await supervisorStore.getDepartmentNurses(departmentId);
  } catch (error: any) {
    const backendMsg = error?.response?.data?.message;
    showError(backendMsg || error.message || 'Error loading data');
  } finally {
    isLoadingNurses.value = false;
  }
}

const onDepartmentSelectedForStaff = async (departmentId: number) => {
  errorDepartmentDetails.value = null;
  await fetchDepartmentNurses(departmentId);
};

const {
  selectedDepartmentId,
  isLoadingDepartments,
  errorDepartments,
  currentDepartment,
  departments,
} = useDepartmentSelection({
  store: supervisorStore,
  fetchDepartmentsAction: supervisorStore.getAllMyDepartments,
  onDepartmentSelected: onDepartmentSelectedForStaff,
  userId: authenticatedUserId,
});

const {
  showConfirmModal,
  confirmMessage,
  handleRemoveNurseConfirmation,
  handleRemoveNurse,
  cancelRemoveNurse,
} = useNurseConfirmation(selectedDepartmentId, fetchDepartmentNurses);

const handleNurseAdded = async () => {
  if (selectedDepartmentId.value) {
    showSuccess('Nurse added successfully!');
    await fetchDepartmentNurses(selectedDepartmentId.value);
  } else {
    showWarning('Nurse could not be added. Department not selected.');
  }
};
</script>

<style lang="scss" scoped>
@use 'SupervisorViews.scss';
</style>
