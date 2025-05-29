<template>
  <div class="admin-dashboard">
    <div class="dashboard-header">
      <h1 class="dashboard-title">Administration Panel</h1>

      <div class="stats-cards">
        <DashboardCard
          title="Users"
          :value="users.totalElements"
          icon="person"
          link="admin-users"
          color="primary"
        />

        <DashboardCard
          title="Departments"
          :value="departments.totalElements"
          icon="business"
          link="admin-departments"
          color="secondary"
        />

        <DashboardCard
          title="Nurse Assignments"
          :value="nurseAssignments.totalElements"
          icon="local_hospital"
          link="nurse-assignment"
          color="warning"
        />

        <DashboardCard
          title="Supervisor Assignments"
          :value="supervisorAssignments.totalElements"
          icon="supervised_user_circle"
          link="supervisor-assignment"
          color="info"
        />
      </div>
    </div>

    <div class="admin-insights">
      <h2>Resume</h2>
      <div class="insights-grid">
        <!--Departamentos sin super-->
        <DashboardCardContent
          v-if="departmentsWithoutSupervisorCount > 0"
          type="warning"
          :title="`Departments without Supervisor (${departmentsWithoutSupervisorCount})`"
        >
          <p>Assign missing supervisors:</p>
          <RouterLink
            class="link-warning"
            :to="{ name: 'supervisor-assignment' }"
          >
            Go to Supervisors
          </RouterLink>
        </DashboardCardContent>

        <!--Departamentos sin enfermera-->
        <DashboardCardContent
          v-if="departmentsWithoutNursesCount > 0"
          type="warning"
          :title="`Departments without Nurses (${departmentsWithoutNursesCount})`"
        >
          <p>Assign pending nurses:</p>
          <RouterLink class="link-warning" :to="{ name: 'nurse-assignment' }">
            Go to Assignments
          </RouterLink>
        </DashboardCardContent>

        <!--Users inactivos-->
        <DashboardCardContent
          v-if="inactiveUsersCount > 0"
          type="info"
          :title="`Inactive Users (${inactiveUsersCount})`"
        >
          <p>Review inactive accounts:</p>
          <RouterLink class="link-warning" :to="{ name: 'admin-users' }">
            Manage Users
          </RouterLink>
        </DashboardCardContent>

        <!--Departaments inactives-->
        <DashboardCardContent
          v-if="inactiveDepartmentsCount > 0"
          type="info"
          :title="`Inactive Departments (${inactiveDepartmentsCount})`"
        >
          <p>Review inactive departments:</p>
          <RouterLink class="link-info" :to="{ name: 'admin-departments' }">
            Manage Departments
          </RouterLink>
        </DashboardCardContent>

        <!--Average Nurse/Dept-->
        <DashboardCardContent
          v-if="totalDepartments && averageNursesPerDepartment < 2"
          type="tip"
          title="Low Nurse Density"
        >
          <p>
            Average nurses/dept: {{ averageNursesPerDepartment.toFixed(1) }}
          </p>
        </DashboardCardContent>

        <!--Todo asignado-->
        <DashboardCardContent
          v-if="hasAnyAssignments"
          type="success"
          title="All in Order"
        >
          <p>All departments have assignments.</p>
        </DashboardCardContent>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue';
import { useAdminStore } from '../../stores/admin.store';
import { UserRole } from '../../types/enums/user-role.enum';
import DashboardCard from '../../components/ui/DashboardCard.vue';
import DashboardCardContent from '../../components/ui/DashboardCardContent.vue';

const adminStore = useAdminStore();
const users = computed(() => adminStore.users);
const departments = computed(() => adminStore.departments);
const supervisorAssignments = computed(() => adminStore.supervisorAssignments);
const nurseAssignments = computed(() => adminStore.nurseAssignments);

const departmentsWithoutSupervisorCount = computed(() => {
  const all = new Set(departments.value.content.map((d) => d.id));
  const assigned = new Set(
    supervisorAssignments.value.content.map((s) => s.department.id),
  );
  return [...all].filter((id) => !assigned.has(id)).length;
});

const departmentsWithoutNursesCount = computed(() => {
  const all = new Set(departments.value.content.map((d) => d.id));
  const assigned = new Set(
    nurseAssignments.value.content.map((n) => n.department.id),
  );
  return [...all].filter((id) => !assigned.has(id)).length;
});

const inactiveUsersCount = computed(
  () => users.value.content.filter((u) => !u.isActive).length,
);

const inactiveDepartmentsCount = computed(
  () => departments.value.content.filter((d) => !d.isActive).length,
);

const totalNurses = computed(
  () => users.value.content.filter((u) => u.role === UserRole.NURSE).length,
);

const totalDepartments = computed(() => departments.value.totalElements);

const averageNursesPerDepartment = computed(() =>
  totalDepartments.value ? totalNurses.value / totalDepartments.value : 0,
);

const hasAnyAssignments = computed(
  () =>
    departmentsWithoutSupervisorCount.value === 0 &&
    departmentsWithoutNursesCount.value === 0,
);

onMounted(() => {
  Promise.all([
    adminStore.getAllUsers(),
    adminStore.getAllDepartments(),
    adminStore.getAllNurseAssignments(),
    adminStore.getAllSupervisorAssignments(),
  ]);
});
</script>

<style lang="scss" scoped>
@use './AdminDashboard.scss';
</style>
