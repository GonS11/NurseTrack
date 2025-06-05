<template>
  <div class="monthly-shift-calendar">
    <div class="calendar-header">
      <button @click="previousMonth" class="nav-button">&lt;</button>
      <h2>{{ currentMonthName }} {{ currentYear }}</h2>
      <button @click="nextMonth" class="nav-button">&gt;</button>
    </div>

    <div v-if="isLoading" class="loading-overlay">
      <p>Loading shifts...</p>
    </div>

    <div v-else class="calendar-grid">
      <div class="week-days">
        <div v-for="day in weekDays" :key="day" class="day-name">
          {{ day }}
        </div>
      </div>

      <div class="days-of-month">
        <div
          v-for="(day, index) in calendarDays"
          :key="index"
          :class="[
            'day-cell',
            {
              'is-current-month': day.isCurrentMonth,
              'has-shifts': day.shifts.length > 0,
            },
          ]"
          @click="openShiftModal(day.date)"
        >
          <template v-if="day.isCurrentMonth">
            <span class="day-number">{{ day.dayNumber }}</span>
            <div v-if="day.shifts.length > 0" class="shift-list">
              <div
                v-for="shift in day.shifts"
                :key="shift.id"
                class="shift-item"
                :class="getShiftTypeClass(shift.shiftTemplate.type)"
                @click.stop="editShift(shift)"
              >
                <span class="shift-time">
                  {{ formatShiftTime(shift.shiftStart, shift.shiftEnd) }}
                </span>
                <span class="shift-nurse">
                  {{ shift.nurse.firstname }} {{ shift.nurse.lastname }}
                </span>
              </div>
            </div>
            <p v-else class="no-shifts-text">No shifts</p>
          </template>
          <template v-else>
            <span class="day-number">&nbsp;</span>
          </template>
        </div>
      </div>
    </div>

    <div
      v-if="isShiftModalOpen"
      class="shift-modal-overlay"
      @click.self="closeShiftModal"
    >
      <div class="shift-modal-content">
        <h3>
          {{ isEditMode ? 'Edit Shift' : 'Create New Shift' }} on
          {{ formatDate(selectedDate) }}
        </h3>
        <form @submit.prevent="saveShift">
          <div class="form-group">
            <label for="nurse">Nurse:</label>
            <select id="nurse" v-model="form.nurseId" required>
              <option value="" disabled>Select a nurse</option>
              <option
                v-for="assignment in availableNurses"
                :key="assignment.id"
                :value="assignment.nurse.id"
              >
                {{ assignment.nurse.firstname }} {{ assignment.nurse.lastname }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="shiftTemplate">Shift Type:</label>
            <select id="shiftTemplate" v-model="form.shiftTemplateId" required>
              <option value="" disabled>Select a shift type</option>
              <option
                v-for="template in shiftTemplatesStore.shiftTemplates"
                :key="template.id"
                :value="template.id"
              >
                {{ template.name }} ({{ formatTime(template.shiftStartTime) }} -
                {{ formatTime(template.shiftEndTime) }})
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="notes">Notes:</label>
            <textarea id="notes" v-model="form.notes"></textarea>
          </div>

          <div class="modal-actions">
            <button type="submit" class="button button-primary">
              {{ isEditMode ? 'Update Shift' : 'Create Shift' }}
            </button>
            <button
              v-if="isEditMode"
              @click="handleCancelShiftConfirmation"
              type="button"
              class="button button-danger"
            >
              Cancel Shift
            </button>
            <button
              type="button"
              @click="closeShiftModal"
              class="button button-secondary"
            >
              Close
            </button>
          </div>
        </form>
      </div>
    </div>

    <ConfirmModal
      v-model="showConfirmModal"
      :message="confirmMessage"
      @confirmed="confirmCancelShift"
      @cancelled="cancelShiftOperation"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useSupervisorStore } from '../../stores/supervisor.store';
import { useShiftTemplatesStore } from '../../stores/shiftTemplate.store';
import type {
  ShiftResponse,
  CreateShiftRequest,
  UpdateShiftRequest,
} from '../../types/schemas/shifts.schema';
import type { NurseDepartmentResponse } from '../../types/schemas/assignments.schema';
import { ShiftStatus } from '../../types/enums/shift-status.enum';
import type { CalendarDay } from '../../types/common';
import { useAuthStore } from '../../services';
import type { ShiftType } from '../../types/enums/shift-types.enum';
import ConfirmModal from '../../components/common/ConfirmModal.vue';
import { useNotifications } from '../../composables/useNotifications';

interface Props {
  departmentId: number;
  shifts: ShiftResponse[];
  isLoading: boolean;
}

const props = defineProps<Props>();

// Eventos que vamos a emitir
const emit = defineEmits<{
  (
    e: 'shift-updated',
    payload: { shiftId: number; request: UpdateShiftRequest },
  ): void;
  (e: 'shift-cancelled', shiftId: number): void;
  (e: 'shift-created', request: CreateShiftRequest): void;
}>();

// Stores
const supervisorStore = useSupervisorStore();
const shiftTemplatesStore = useShiftTemplatesStore();
const authStore = useAuthStore();
const notifications = useNotifications();

// Estado interno del calendario
const currentMonth = ref(new Date().getMonth());
const currentYear = ref(new Date().getFullYear());

const isShiftModalOpen = ref(false);
const isEditMode = ref(false);
const selectedDate = ref<string>('');
const currentShiftId = ref<number | null>(null);

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const shiftIdToCancel = ref<number | null>(null);

const todayString = new Date().toISOString().split('T')[0];

const form = ref<Partial<CreateShiftRequest> & Partial<UpdateShiftRequest>>({
  nurseId: undefined,
  departmentId: props.departmentId,
  shiftTemplateId: undefined,
  shiftDate: '',
  notes: '',
  status: ShiftStatus.SCHEDULED,
});

const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

const currentMonthName = computed(() => {
  return new Date(currentYear.value, currentMonth.value).toLocaleString(
    'en-US',
    { month: 'long' },
  );
});

const availableNurses = computed<NurseDepartmentResponse[]>(() => {
  return supervisorStore.nurseAssignments;
});

const calendarDays = computed<CalendarDay[]>(() => {
  const year = currentYear.value;
  const month = currentMonth.value;
  const firstOfMonth = new Date(year, month, 1);
  const lastOfMonth = new Date(year, month + 1, 0);
  const daysInMonth = lastOfMonth.getDate();
  const startDayOfWeek = firstOfMonth.getDay(); // 0 = Sunday, etc.

  const daysArray: CalendarDay[] = [];

  // 1) Rellenar huecos VACÍOS ANTES del día 1 del mes
  for (let i = 0; i < startDayOfWeek; i++) {
    daysArray.push({
      date: '',
      dayNumber: 0,
      isCurrentMonth: false,
      shifts: [],
    });
  }

  // 2) Añadir los días reales del mes
  for (let dayNum = 1; dayNum <= daysInMonth; dayNum++) {
    const dateObj = new Date(year, month, dayNum);
    const dateString = dateObj.toISOString().split('T')[0];
    const shiftsForDay = props.shifts.filter(
      (s) => s.shiftDate.split('T')[0] === dateString,
    );
    daysArray.push({
      date: dateString,
      dayNumber: dayNum,
      isCurrentMonth: true,
      shifts: shiftsForDay,
    });
  }

  // 3) Rellenar huecos VACÍOS DESPUÉS para completar la última fila de la cuadrícula
  const totalCells = daysArray.length;
  const remainder = totalCells % 7;
  if (remainder !== 0) {
    const emptyToAdd = 7 - remainder;
    for (let i = 0; i < emptyToAdd; i++) {
      daysArray.push({
        date: '',
        dayNumber: 0,
        isCurrentMonth: false,
        shifts: [],
      });
    }
  }

  return daysArray;
});

// ---------------------- WATCHERS ----------------------
// 1) Siempre que cambie el departmentId del padre, lo reflejamos en el form
watch(
  () => props.departmentId,
  (newDeptId) => {
    form.value.departmentId = newDeptId;
  },
  { immediate: true },
);

// 2) Cuando cambia departmentId, recargamos las plantillas de turno
watch(
  () => props.departmentId,
  async (newDeptId) => {
    if (newDeptId) {
      try {
        await shiftTemplatesStore.getShiftTemplates();
      } catch (error: any) {
        // CAMBIO: Manejar error con notificaciones
        notifications.showError(
          `Error fetching shift templates: ${error.message || 'Unknown error'}`,
        );
        console.error('Error fetching shift templates:', error);
      }
    }
  },
  { immediate: true },
);

// ---------------------- NAVEGACIÓN DE MESES ----------------------
const previousMonth = () => {
  if (currentMonth.value === 0) {
    currentMonth.value = 11;
    currentYear.value -= 1;
  } else {
    currentMonth.value -= 1;
  }
};

const nextMonth = () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value += 1;
  } else {
    currentMonth.value += 1;
  }
};

// ---------------------- MODAL: CREAR / EDITAR TURNO ----------------------
const openShiftModal = (date: string) => {
  // 1) Si date es '' (celda vacía), no abrimos modal
  if (!date) return;

  // 2) Si la fecha es anterior a hoy, bloqueamos la creación/edición
  if (date < todayString) {
    notifications.showWarning('Cannot create or edit a shift in the past.'); // CAMBIO: Usar notificación
    return;
  }

  // 3) Abrimos en modo "create"
  selectedDate.value = date;
  isEditMode.value = false;
  currentShiftId.value = null;
  resetForm();
  form.value.shiftDate = date;
  isShiftModalOpen.value = true;
};

const editShift = (shift: ShiftResponse) => {
  const shiftDateOnly = shift.shiftDate.split('T')[0]; // "YYYY-MM-DD"

  // Si el turno ya está en el pasado, no permitimos editarlo
  if (shiftDateOnly < todayString) {
    notifications.showWarning('Cannot edit a shift that is in the past.'); // CAMBIO: Usar notificación
    return;
  }

  // Entramos en modo edición
  isEditMode.value = true;
  selectedDate.value = shiftDateOnly;
  currentShiftId.value = shift.id;
  form.value = {
    nurseId: shift.nurse.id,
    departmentId: shift.department.id,
    shiftTemplateId: shift.shiftTemplate.id,
    shiftDate: shiftDateOnly,
    notes: shift.notes,
  };
  isShiftModalOpen.value = true;
};

const closeShiftModal = () => {
  isShiftModalOpen.value = false;
  resetForm();
};

const resetForm = () => {
  form.value = {
    nurseId: undefined,
    departmentId: props.departmentId,
    shiftTemplateId: undefined,
    shiftDate: '',
    notes: '',
    status: ShiftStatus.SCHEDULED,
  };
  currentShiftId.value = null;
};

const saveShift = () => {
  // **Modo Edición**
  if (isEditMode.value && currentShiftId.value) {
    if (!form.value.shiftDate) {
      notifications.showError('Shift date is required for updating a shift.'); // CAMBIO: Usar notificación
      return;
    }
    const updateRequest: UpdateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: form.value.departmentId!,
      shiftTemplateId: form.value.shiftTemplateId!,
      shiftDate: form.value.shiftDate,
      notes: form.value.notes,
    };
    emit('shift-updated', {
      shiftId: currentShiftId.value,
      request: updateRequest,
    });
    notifications.showSuccess('Shift updated successfully!'); // CAMBIO: Notificación de éxito
  } else {
    // **Modo Creación**
    if (
      form.value.nurseId === undefined ||
      form.value.shiftTemplateId === undefined ||
      !form.value.shiftDate ||
      form.value.status === undefined
    ) {
      notifications.showWarning(
        // CAMBIO: Usar notificación
        'Missing required fields. Please select Nurse, Shift Type, Date, and ensure Status is set.',
      );
      return;
    }

    if (!authStore.user || !authStore.user.id) {
      notifications.showError(
        'You must be logged in as a supervisor to create a shift.',
      ); // CAMBIO: Usar notificación
      return;
    }

    const supervisorId = authStore.user?.id;

    const createRequest: CreateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: form.value.departmentId!,
      shiftTemplateId: form.value.shiftTemplateId!,
      shiftDate: form.value.shiftDate,
      notes: form.value.notes,
      createdById: supervisorId,
      status: form.value.status,
    };
    emit('shift-created', createRequest);
    notifications.showSuccess('Shift created successfully!'); // CAMBIO: Notificación de éxito
  }
  closeShiftModal();
};

// New function to handle confirmation logic before cancelling the shift
const handleCancelShiftConfirmation = () => {
  if (currentShiftId.value) {
    shiftIdToCancel.value = currentShiftId.value;
    confirmMessage.value = 'Are you sure you want to cancel this shift?';
    showConfirmModal.value = true;
  }
};

// Function called when the ConfirmModal emits 'confirmed'
const confirmCancelShift = () => {
  if (!shiftIdToCancel.value) return; // Should not happen if logic is correct
  emit('shift-cancelled', shiftIdToCancel.value);
  notifications.showSuccess('Shift cancelled successfully.'); // CAMBIO: Notificación de éxito
  closeShiftModal(); // Close the shift creation/edit modal
  shiftIdToCancel.value = null; // Clear the stored ID
};

// Function called when the ConfirmModal emits 'cancelled'
const cancelShiftOperation = () => {
  shiftIdToCancel.value = null; // Clear the stored ID
  notifications.showInfo('Shift cancellation was cancelled.'); // CAMBIO: Notificación de información
};

// ---------------------- UTILITIES ----------------------
const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

const formatTime = (timeString: string) => {
  const date = new Date(timeString);
  return date.toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  });
};

const formatShiftTime = (start: string, end: string) => {
  return `${formatTime(start)} - ${formatTime(end)}`;
};

function getShiftTypeClass(shiftType: ShiftType): string {
  let cls = shiftType.toLowerCase();
  cls = cls.replace(/_/g, '-');
  return cls;
}
</script>

<style lang="scss" scoped>
@use 'MonthlyCalendar.scss';
</style>
