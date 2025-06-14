<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <h2>
        {{ isEditMode ? 'Edit Shift' : 'Create New Shift' }} on
        {{ formatDate(initialShiftDate) }}
      </h2>

      <form @submit.prevent="saveShift">
        <div class="form-fields">
          <InputSelect
            id="nurse"
            label="Nurse"
            :model-value="form.nurseId"
            @update:model-value="form.nurseId = $event"
            text="Select a nurse"
            :entities="availableNurses"
            item-key="id"
            item-value="nurse.id"
            :option-value="(assignment: any) => `${assignment.nurse.firstname} ${assignment.nurse.lastname}`"
            placeholder-value=""
            required
          />

          <InputSelect
            id="shiftTemplate"
            label="Shift Type"
            :model-value="form.shiftTemplateId"
            @update:model-value="form.shiftTemplateId = $event"
            text="Select a shift type"
            :entities="shiftTemplates"
            item-key="id"
            item-value="id"
            :option-value="(template: any) => `${template.name} (${formatTime(template.shiftStartTime)} - ${formatTime(template.shiftEndTime)})`"
            placeholder-value=""
            required
          />

          <div class="form-group">
            <label for="notes">Notes:</label>
            <textarea id="notes" v-model="form.notes"></textarea>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">
            {{ isEditMode ? 'Update' : 'Create' }}
          </button>
          <button type="button" @click="closeModal" class="btn btn-secondary">
            Close
          </button>
          <button
            v-if="isEditMode"
            type="button"
            @click="requestShiftCancellation"
            class="btn btn-danger"
          >
            Cancel Shift
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { formatDate } from '../../../utils/helpers';
import type {
  CreateShiftRequest,
  UpdateShiftRequest,
  ShiftResponse,
} from '../../../types/schemas/shifts.schema';
import { ShiftStatus } from '../../../types/enums/shift-status.enum';
import InputSelect from '../InputSelect.vue';
import type { ShiftFormModalProps } from '../../../types/common';
import { useNotifications } from '../../../composables/useNotifications';

const { showError, showWarning } = useNotifications();

const props = defineProps<ShiftFormModalProps & { departmentId?: number }>();

const emit = defineEmits<{
  (e: 'update:isOpen', value: boolean): void;
  (
    e: 'shift-saved',
    payload: {
      type: 'create' | 'update';
      data: CreateShiftRequest | UpdateShiftRequest;
      shiftId?: number;
    },
  ): void;
  (e: 'shift-cancel-requested', shift: ShiftResponse): void;
}>();

const form = ref<Partial<CreateShiftRequest> & Partial<UpdateShiftRequest>>({
  nurseId: undefined,
  departmentId: undefined,
  shiftTemplateId: undefined,
  shiftDate: '',
  notes: '',
  status: ShiftStatus.SCHEDULED,
});

const currentShift = ref<ShiftResponse | null>(null);

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      resetForm();
      if (props.isEditMode && props.shiftToEdit) {
        currentShift.value = props.shiftToEdit;
        form.value = {
          nurseId: props.shiftToEdit.nurse.id,
          departmentId: props.shiftToEdit.department.id,
          shiftTemplateId: props.shiftToEdit.shiftTemplate.id,
          shiftDate: props.shiftToEdit.shiftDate.split('T')[0],
          notes: props.shiftToEdit.notes,
          status: props.shiftToEdit.status,
        };
      } else {
        form.value.shiftDate = props.initialShiftDate;
        form.value.status = ShiftStatus.SCHEDULED;
        form.value.departmentId = props.departmentId;
      }
    }
  },
  { immediate: true },
);

const resetForm = () => {
  form.value = {
    nurseId: undefined,
    departmentId: props.departmentId,
    shiftTemplateId: undefined,
    shiftDate: '',
    notes: '',
    status: ShiftStatus.SCHEDULED,
  };
  currentShift.value = null;
};

const closeModal = () => {
  emit('update:isOpen', false);
};

const saveShift = () => {
  if (
    form.value.nurseId === undefined ||
    form.value.shiftTemplateId === undefined ||
    !form.value.shiftDate
  ) {
    showError('Missing required fields for shift operation.');
    return;
  }

  if (props.isEditMode) {
    if (!currentShift.value) {
      showError('Error: No shift to edit.');
      return;
    }
    const updateRequest: UpdateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: currentShift.value.department.id,
      shiftTemplateId: form.value.shiftTemplateId!,
      shiftDate: form.value.shiftDate,
      notes: form.value.notes,
      status: form.value.status!,
    };
    emit('shift-saved', {
      type: 'update',
      data: updateRequest,
      shiftId: currentShift.value.id,
    });
  } else {
    if (!form.value.departmentId) {
      showError('No department ID provided to create the shift.');
      return;
    }
    const createRequest: CreateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: form.value.departmentId!,
      shiftTemplateId: form.value.shiftTemplateId!,
      shiftDate: form.value.shiftDate,
      notes: form.value.notes,
      status: form.value.status!,
      createdById: undefined!,
    };
    emit('shift-saved', { type: 'create', data: createRequest });
  }
  closeModal();
};

const requestShiftCancellation = () => {
  if (currentShift.value) {
    emit('shift-cancel-requested', currentShift.value);
  } else {
    showWarning('No shift selected for cancellation.');
  }
};

const formatTime = (timeString: string) => {
  if (!timeString) return '';
  const date = new Date(`2000-01-01T${timeString}`);
  return date.toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  });
};
</script>

<style lang="scss" scoped>
@use 'Modal.scss';
</style>
