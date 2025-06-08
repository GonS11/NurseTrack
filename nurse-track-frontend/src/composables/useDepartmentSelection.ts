import { ref, computed, onMounted, watch, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import { useSupervisorStore } from '../stores/supervisor.store';
import { useNotifications } from './useNotifications';
import type { DepartmentResponse } from '../types/schemas/department.schema';

interface UseDepartmentSelectionOptions {
  onDepartmentSelected: (departmentId: number) => Promise<void>;
}

export function useDepartmentSelection(options: UseDepartmentSelectionOptions) {
  const supervisorStore = useSupervisorStore();
  const route = useRoute();
  const { showError } = useNotifications();

  const isLoadingDepartments = ref(false);
  const errorDepartments = ref<string | null>(null);
  const selectedDepartmentId: Ref<number | null> = ref(null);

  const currentDepartment = computed<DepartmentResponse | null>(() => {
    if (selectedDepartmentId.value === null) return null;
    return (
      supervisorStore.departments.find(
        (dept) => dept.id === selectedDepartmentId.value,
      ) || null
    );
  });

  onMounted(async () => {
    isLoadingDepartments.value = true;
    errorDepartments.value = null;
    try {
      await supervisorStore.getAllMyDepartments();

      const routeDepartmentId = route.query.departmentId;

      if (routeDepartmentId) {
        const idFromRoute = Number(routeDepartmentId);
        if (
          !isNaN(idFromRoute) &&
          supervisorStore.departments.some((d) => d.id === idFromRoute)
        ) {
          selectedDepartmentId.value = idFromRoute;
        } else if (supervisorStore.departments.length > 0) {
          selectedDepartmentId.value = supervisorStore.departments[0].id;
        }
      } else if (supervisorStore.departments.length > 0) {
        selectedDepartmentId.value = supervisorStore.departments[0].id;
      }
    } catch (error: any) {
      showError(error.message || 'Error loading departments.', false);
      errorDepartments.value = error.message || 'Error loading departments.';
    } finally {
      isLoadingDepartments.value = false;
    }
  });

  watch(
    selectedDepartmentId,
    async (newDepartmentId) => {
      if (newDepartmentId !== null) {
        // Asegúrate de que selectedDepartmentId.value siempre sea un número antes de pasarlo.
        await options.onDepartmentSelected(newDepartmentId);
      }
    },
    { immediate: true },
  );

  return {
    selectedDepartmentId,
    isLoadingDepartments,
    errorDepartments,
    currentDepartment,
  };
}
