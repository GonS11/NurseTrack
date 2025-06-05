import { ref, computed, onMounted, watch, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import { useSupervisorStore } from '../stores/supervisor.store';
import type { DepartmentResponse } from '../types/schemas/department.schema';

interface UseDepartmentSelectionOptions {
  onDepartmentSelected: (departmentId: number) => Promise<void>;
  showAlertMessage: (
    message: string,
    type: 'info' | 'success' | 'warning' | 'error',
    autoClose?: boolean | number,
  ) => void;
}

export function useDepartmentSelection(options: UseDepartmentSelectionOptions) {
  const supervisorStore = useSupervisorStore();
  const route = useRoute();

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
        selectedDepartmentId.value = Number(routeDepartmentId);
      } else if (supervisorStore.departments.length > 0) {
        selectedDepartmentId.value = supervisorStore.departments[0].id;
      }
    } catch (error: any) {
      options.showAlertMessage(
        error.message || 'Error loading departments.',
        'error',
        false,
      );
      errorDepartments.value = error.message || 'Error loading departments.';
      console.error('Error in useDepartmentSelection (onMounted):', error);
    } finally {
      isLoadingDepartments.value = false;
    }
  });

  watch(
    selectedDepartmentId,
    async (newDepartmentId) => {
      if (newDepartmentId !== null) {
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
