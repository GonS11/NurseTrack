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
import type {
  AssignNurseRequest,
  NurseDepartmentResponse,
} from '../../types/schemas/assignments.schema';
import type { DepartmentResponse } from '../../types/schemas/department.schema';
import { type TableAction } from '../../components/ui/Table.vue';
import { useAdminStore } from '../../stores/admin.store';

const adminStore = useAdminStore();

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

  // Transform the map into an array of objects suitable for ManagamentComponent
  const result: (DepartmentResponse & {
    assignedNurses: NurseDepartmentResponse[];
  })[] = [];
  groupedNurses.forEach((nurses, departmentId) => {
    const departmentInfo = nurses[0].department; // Assuming department info is consistent for all nurses in that department
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

const nurseAssignmentActions = computed(
  () =>
    [
      {
        label: 'Manage Nurses',
        icon: 'group',
        class: 'info',
        handler: (
          department: DepartmentResponse & {
            assignedNurses: NurseDepartmentResponse[];
          },
        ) => {
          // Open a different modal or navigate to a detail view to manage nurses for this department
          managementComponentRef.value?.openUpdateModal(department); // Reusing openUpdateModal, but it will be for managing nurses
        },
      },
    ] as TableAction<
      DepartmentResponse & { assignedNurses: NurseDepartmentResponse[] }
    >[],
);

const getAllDepartmentsWithNurseInfo = async (page: number) => {
  // You might need a more specific API call here that fetches departments
  // along with their assigned nurses, or you'll have to make multiple calls
  // and then process the data.
  // For now, let's fetch all nurse assignments and then group them.
  await adminStore.getAllNurseAssignments(page);
  // If you only want to show departments that have nurses assigned,
  // the current `departmentsWithNurses` computed property will work.
  // If you want to show ALL departments and indicate if they have nurses,
  // you'd need to fetch all departments separately and then merge the nurse data.
  // Given the current backend endpoint, `getAllNurseAssignments` seems to be the way to go for the main table.
};

const assignNurseToDepartment = async (formData: AssignNurseRequest) => {
  await adminStore.assignNurseToDepartment(formData);
};

//Hook de ciclo de vida
onMounted(() => {
  getAllDepartmentsWithNurseInfo(0); //Cargar la primera pagina al montar
});
</script>

<style lang="scss" scoped>
@use 'AdminView.scss';
</style>
