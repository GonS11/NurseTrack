<template>
  <div class="supervisor-department-staff-view">
    <h1>Department and Staff Management</h1>

    <div
      v-if="supervisorStore.departments.length > 1"
      class="department-selector"
    >
      <label for="select-department">Select Department:</label>
      <select
        id="select-department"
        v-model="selectedDepartmentId"
        @change="onDepartmentChange"
      >
        <option
          v-for="dept in supervisorStore.departments"
          :key="dept.id"
          :value="dept.id"
        >
          {{ dept.name }} ({{ dept.location }})
        </option>
      </select>
    </div>

    <section class="department-details-section">
      <h2>Department Details</h2>
      <div v-if="currentDepartment" class="department-card">
        <DepartmentDetailCard :department="currentDepartment" />
      </div>
      <p v-else-if="isLoadingDepartments">Loading department details...</p>
      <p v-else-if="localError">{{ localError }}</p>
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
          @remove-nurse="handleRemoveNurse"
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

    <div v-if="localError" class="error-message">
      {{ localError }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useAuthStore } from '../../services'; // To get the supervisor's ID if needed
import DepartmentDetailCard from '../../components/ui/supervisor/DepartmentDetailCard.vue';
import NurseTable from '../../components/ui/supervisor/NurseTable.vue';
import AddNurseToDepartmentForm from '../../components/ui/supervisor/AddNurseToDepartmentForm.vue';

const supervisorStore = useSupervisorStore();
const authStore = useAuthStore(); // To access authStore.currentUser.id

const isLoadingDepartments = ref(false);
const isLoadingNurses = ref(false);
const localError = ref(''); // Local error state for this view

// Reactive variable to hold the currently selected department ID
const selectedDepartmentId = ref<number | null>(null);

// Computed property for the currently displayed department object
const currentDepartment = computed(() => {
  return supervisorStore.departments.find(
    (dept) => dept.id === selectedDepartmentId.value,
  );
});

// --- Lifecycle Hook ---
onMounted(async () => {
  localError.value = ''; // Clear any previous errors
  isLoadingDepartments.value = true;
  try {
    // Fetch all departments assigned to the logged-in supervisor
    // If your service needs supervisorId (e.g., useSupervisorDepartmentService.getAllMyDepartments(authStore.currentUser.id)), pass it here
    await supervisorStore.getAllMyDepartments();

    // Set the first department as selected by default, if any
    if (supervisorStore.departments.length > 0) {
      selectedDepartmentId.value = supervisorStore.departments[0].id;
    }
  } catch (error: any) {
    localError.value =
      error.message || 'Failed to load supervisor departments.';
    console.error('Error in onMounted (SupervisorDepartmentAndStaff):', error);
  } finally {
    isLoadingDepartments.value = false;
  }
});

// --- Watcher for selectedDepartmentId ---
// React to changes in the selected department
watch(
  selectedDepartmentId,
  async (newDeptId) => {
    if (newDeptId) {
      localError.value = ''; // Clear errors
      isLoadingNurses.value = true;
      try {
        await supervisorStore.getDepartmentNurses(newDeptId);
      } catch (error: any) {
        localError.value =
          error.message || 'Failed to load nurses for selected department.';
        console.error(
          `Error fetching nurses for department ${newDeptId}:`,
          error,
        );
      } finally {
        isLoadingNurses.value = false;
      }
    } else {
      // Clear nurses if no department is selected
      supervisorStore.nurseAssignments = [];
    }
  },
  { immediate: true },
); // Run immediately on component mount if selectedDepartmentId has an initial value

// --- Event Handlers ---
const onDepartmentChange = () => {
  // The watcher will handle fetching nurses when selectedDepartmentId changes
  // If `getMyDepartment` is for specific details not in the initial list (from getAllMyDepartments), call it here:
  // if (selectedDepartmentId.value) {
  //   supervisorStore.getMyDepartment(selectedDepartmentId.value);
  // }
};

const handleRemoveNurse = async (nurseId: number) => {
  if (
    confirm('Are you sure you want to remove this nurse from the department?')
  ) {
    localError.value = '';
    try {
      if (selectedDepartmentId.value) {
        await supervisorStore.removeNurseFromDepartment(
          selectedDepartmentId.value,
          nurseId,
        );
        // The store action already updates the `nurseAssignments` array, no need to re-fetch all nurses.
      }
    } catch (error: any) {
      localError.value = error.message || 'Failed to remove nurse.';
      console.error('Error removing nurse:', error);
    }
  }
};

const handleNurseAdded = async () => {
  // After a nurse is added, re-fetch the list to ensure the UI is updated
  if (selectedDepartmentId.value) {
    await supervisorStore.getDepartmentNurses(selectedDepartmentId.value);
  }
};
</script>

<style lang="scss" scoped>
@use 'SupervisorManagement.scss';
</style>
