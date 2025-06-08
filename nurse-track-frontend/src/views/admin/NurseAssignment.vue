<template>
  <ManagamentComponent
    title="Nurse Assignments by Department"
    :headers="nurseAssignmentHeaders"
    :data="departmentsWithNurses"
    :actions="nurseAssignmentActions"
    :modal-component="NurseAssignmentModal"
    create-button-label="Assign Nurse to Department"
    :fetch-data="getAllDepartmentsWithNurseInfo"
    :create-item="assignNurseToDepartment"
    item-id-key="id"
    ref="managementComponentRef"
  >
  </ManagamentComponent>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import ManagamentComponent from '../../components/common/ManagamentComponent.vue';
import NurseAssignmentModal from '../../components/ui/modals/NurseAssignmentModal.vue';
import { useAdminStore } from '../../stores/admin.store';
import { useNotifications } from '../../composables/useNotifications';
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../../types/schemas/assignments.schema';
import type { DepartmentResponse } from '../../types/schemas/department.schema';
import { type TableAction } from '../../components/ui/Table.vue';

const adminStore = useAdminStore();
const { showSuccess, showError } = useNotifications();

const managementComponentRef = ref<InstanceType<
  typeof ManagamentComponent
> | null>(null);

const departmentsWithNurses = computed(() => {
  const groupedNurses = new Map<number, NurseDepartmentResponse[]>();
  adminStore.nurseAssignments.content.forEach((assignment) => {
    if (!groupedNurses.has(assignment.department.id)) {
      groupedNurses.set(assignment.department.id, []);
    }
    groupedNurses.get(assignment.department.id)?.push(assignment);
  });

  const result: (DepartmentResponse & {
    assignedNurses: NurseDepartmentResponse[];
  })[] = [];
  groupedNurses.forEach((nurses, _departmentId) => {
    const departmentInfo = nurses[0].department;
    result.push({
      ...departmentInfo,
      assignedNurses: nurses,
    });
  });

  return {
    content: result,
    number: adminStore.nurseAssignments.number,
    totalPages: adminStore.nurseAssignments.totalPages,
    totalElements: adminStore.nurseAssignments.totalElements,
    size: adminStore.nurseAssignments.size,
  };
});

const nurseAssignmentHeaders = [
  { key: 'name', label: 'Department Name' },
  {
    key: 'assignedNurses',
    label: 'Assigned Nurses',
    format: (
      item: DepartmentResponse & { assignedNurses: NurseDepartmentResponse[] },
    ) =>
      item.assignedNurses.length > 0
        ? item.assignedNurses
            .map((n) => `${n.nurse.firstname} ${n.nurse.lastname}`)
            .join(', ')
        : 'No nurses assigned',
  },
];

const nurseAssignmentActions = ref([
  {
    label: 'Manage Nurses',
    icon: 'group',
    class: 'info',
    handler: (
      department: DepartmentResponse & {
        assignedNurses: NurseDepartmentResponse[];
      },
    ) => {
      managementComponentRef.value?.openUpdateModal(department);
    },
  },
] as TableAction<DepartmentResponse & { assignedNurses: NurseDepartmentResponse[] }>[]);

const getAllDepartmentsWithNurseInfo = async (page: number) => {
  try {
    await adminStore.getAllNurseAssignments(page);
  } catch (error: any) {
    showError(error);
  }
};

const assignNurseToDepartment = async (formData: AssignNurseRequest) => {
  try {
    await adminStore.assignNurseToDepartment(formData);
    showSuccess('Nurse assigned to department successfully!');
  } catch (error: any) {
    showError(error);
  }
};

onMounted(() => {
  getAllDepartmentsWithNurseInfo(0);
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
