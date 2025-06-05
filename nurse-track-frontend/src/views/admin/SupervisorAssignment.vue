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

const adminStore = useAdminStore();

const managementComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const { showSuccess, showError } = useNotifications();

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
        handler: async (assignment: SupervisorDepartmentResponse) => {
          try {
            if (
              confirm(
                `Are you sure you want to remove supervisor ${assignment.supervisor.firstname} ${assignment.supervisor.lastname} from department ${assignment.department.name}?`,
              )
            ) {
              await removeSupervisor(assignment.department.id);
              await getAllSupervisorAssignments(
                supervisorAssignments.value.number, // Refresca la tabla
              );
              showSuccess('Supervisor assignment removed successfully!');
            }
          } catch (error: any) {
            showError(error);
          }
        },
      },
    ] as TableAction<SupervisorDepartmentResponse>[],
);

// Métodos de CRUD para pasarle al componente
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
  } catch (error: any) {
    showError(error);
  }
};

onMounted(() => {
  getAllSupervisorAssignments(0);
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
