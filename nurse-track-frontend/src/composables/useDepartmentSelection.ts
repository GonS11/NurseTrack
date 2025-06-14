import { ref, computed, onMounted, watch, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import { useNurseStore } from '../stores/nurse.store';
import { useSupervisorStore } from '../stores/supervisor.store';
import { useNotifications } from './useNotifications';
import type { DepartmentResponse } from '../types/schemas/department.schema';

type FetchDepartmentsFunction = (userId: number) => Promise<void>;

interface UseDepartmentSelectionOptions {
  store:
    | ReturnType<typeof useNurseStore>
    | ReturnType<typeof useSupervisorStore>;
  fetchDepartmentsAction: FetchDepartmentsFunction;
  onDepartmentSelected: (departmentId: number) => Promise<void>;
  userId: Ref<number | null>;
}

export function useDepartmentSelection(options: UseDepartmentSelectionOptions) {
  const route = useRoute();
  const { showError } = useNotifications();

  const isLoadingDepartments = ref(false);
  const errorDepartments = ref<string | null>(null);
  const selectedDepartmentId: Ref<number | null> = ref(null);

  const departments = computed<DepartmentResponse[]>(
    () => options.store.departments || [],
  );

  const currentDepartment = computed<DepartmentResponse | null>(() => {
    if (selectedDepartmentId.value === null) return null;

    return (
      departments.value.find(
        (dept) => dept.id === selectedDepartmentId.value,
      ) || null
    );
  });

  onMounted(async () => {
    isLoadingDepartments.value = true;
    errorDepartments.value = null;

    try {
      if (options.userId.value) {
        await options.fetchDepartmentsAction(options.userId.value);
      }

      const routeDepartmentId = route.query.departmentId;
      const availableDepartments = departments.value;

      if (routeDepartmentId) {
        const idFromRoute = Number(routeDepartmentId);

        if (
          !isNaN(idFromRoute) &&
          availableDepartments.some((d) => d.id === idFromRoute)
        ) {
          selectedDepartmentId.value = idFromRoute;
        } else if (availableDepartments.length > 0) {
          selectedDepartmentId.value = availableDepartments[0].id;
        }
      } else if (availableDepartments.length > 0) {
        selectedDepartmentId.value = availableDepartments[0].id;
      }
    } catch (error: any) {
      const backendMsg = error?.response?.data?.message;

      showError(
        backendMsg || error.message || 'Error loading departments.',
        false,
      );

      errorDepartments.value =
        backendMsg || error.message || 'Error loading departments.';
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
    departments,
  };
}
