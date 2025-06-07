<template>
  <ManagamentComponent
    title="Supervisor Assignments"
    :headers="supervisorAssignmentHeaders"
    :data="supervisorAssignments"
    :actions="supervisorAssignmentActions"
    :modal-component="SupervisorAssignmentModal"
    create-button-label="Assign Supervisor"
    :fetch-data="getAllSupervisorAssignments"
    :create-item="assignSupervisor"
    item-id-key="department.id"
    ref="managementComponentRef"
  >
  </ManagamentComponent>

  <ConfirmModal
    v-model="showConfirmModal"
    :message="confirmMessage"
    @confirmed="executeRemoveSupervisor"
    @cancelled="cancelRemoveSupervisorOperation"
  />
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import type {
  AssignSupervisorRequest,
  SupervisorDepartmentResponse,
} from '../../types/schemas/assignments.schema';
import { type TableAction } from '../../components/ui/Table.vue';
import { useAdminStore } from '../../stores/admin.store';
import SupervisorAssignmentModal from '../../components/ui/modals/SupervisorAssignmentModal.vue';
import { useNotifications } from '../../composables/useNotifications';
import ConfirmModal from '../../components/common/ConfirmModal.vue';

const adminStore = useAdminStore();

const managementComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const { showSuccess, showError } = useNotifications();

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const assignmentToRemoveDetails = ref<{
  departmentId: number;
  supervisorName: string;
  departmentName: string;
} | null>(null);

const supervisorAssignments = computed(() => adminStore.supervisorAssignments);

const supervisorAssignmentHeaders = [
  {
    key: 'departmentName',
    label: 'Department Name',
    format: (item: SupervisorDepartmentResponse) => item.department.name,
  },
  {
    key: 'supervisorName',
    label: 'Supervisor',
    format: (item: SupervisorDepartmentResponse) =>
      `${item.supervisor.firstname} ${item.supervisor.lastname}`,
  },
  {
    key: 'assignedAt',
    label: 'Assigned At',
    format: (item: SupervisorDepartmentResponse) =>
      new Date(item.assignedAt).toLocaleDateString(),
  },
];

const supervisorAssignmentActions = computed(
  () =>
    [
      {
        label: 'Remove Assignment',
        icon: 'person_remove',
        class: 'danger',
        handler: (assignment: SupervisorDepartmentResponse) => {
          handleRemoveSupervisorConfirmation(
            assignment.department.id,
            assignment.supervisor.firstname,
            assignment.supervisor.lastname,
            assignment.department.name,
          );
        },
      },
    ] as TableAction<SupervisorDepartmentResponse>[],
);

const getAllSupervisorAssignments = async (page: number) => {
  try {
    await adminStore.getAllSupervisorAssignments(page);
  } catch (error: any) {
    showError(error);
  }
};

const assignSupervisor = async (formData: AssignSupervisorRequest) => {
  try {
    await adminStore.assignSupervisorToDepartment(formData);
    showSuccess('Supervisor assigned successfully!');
  } catch (error: any) {
    showError(error);
  }
};

const removeSupervisor = async (departmentId: number) => {
  try {
    await adminStore.removeSupervisorFromDepartment(departmentId);
    showSuccess('Supervisor assignment removed successfully!');
  } catch (error: any) {
    showError('Failed to remove supervisor assignment: ' + error.message);
    throw error;
  }
};

const handleRemoveSupervisorConfirmation = (
  departmentId: number,
  supervisorFirstName: string,
  supervisorLastName: string,
  departmentName: string,
) => {
  assignmentToRemoveDetails.value = {
    departmentId,
    supervisorName: `${supervisorFirstName} ${supervisorLastName}`,
    departmentName,
  };
  confirmMessage.value = `Are you sure you want to remove supervisor "${assignmentToRemoveDetails.value.supervisorName}" from department "${assignmentToRemoveDetails.value.departmentName}"?`;
  showConfirmModal.value = true;
};

const executeRemoveSupervisor = async () => {
  if (!assignmentToRemoveDetails.value) return;

  const { departmentId } = assignmentToRemoveDetails.value;

  try {
    await removeSupervisor(departmentId);
    await getAllSupervisorAssignments(supervisorAssignments.value.number);
  } catch (error: any) {
    console.error('Error executing supervisor removal:', error);
  } finally {
    assignmentToRemoveDetails.value = null;
    showConfirmModal.value = false;
  }
};

const cancelRemoveSupervisorOperation = () => {
  assignmentToRemoveDetails.value = null;
  showConfirmModal.value = false;
  console.log('Supervisor removal operation cancelled by user.');
};

onMounted(() => {
  getAllSupervisorAssignments(0);
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
