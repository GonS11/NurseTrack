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
} from '../../../types/schemas/shifts.schema';
import { ShiftStatus } from '../../../types/enums/shift-status.enum';
import InputSelect from '../InputSelect.vue';
import type { ShiftFormModalProps } from '../../../types/common'; // Asegúrate de que esta importación sea correcta

const props = defineProps<ShiftFormModalProps>();

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
  (e: 'shift-cancel-requested', shiftId: number): void;
}>();

const form = ref<Partial<CreateShiftRequest> & Partial<UpdateShiftRequest>>({
  nurseId: undefined,
  departmentId: undefined,
  shiftTemplateId: undefined,
  shiftDate: '',
  notes: '',
  status: ShiftStatus.SCHEDULED,
});

const currentShiftId = ref<number | null>(null);

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      resetForm();
      if (props.isEditMode && props.shiftToEdit) {
        currentShiftId.value = props.shiftToEdit.id;
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
      }
    }
  },
);

const resetForm = () => {
  form.value = {
    nurseId: undefined,
    departmentId: undefined,
    shiftTemplateId: undefined,
    shiftDate: '',
    notes: '',
    status: ShiftStatus.SCHEDULED,
  };
  currentShiftId.value = null;
};

const closeModal = () => {
  emit('update:isOpen', false);
  resetForm();
};

const saveShift = () => {
  if (
    form.value.nurseId === undefined ||
    form.value.shiftTemplateId === undefined ||
    !form.value.shiftDate
  ) {
    console.error('Missing required fields for shift operation.');
    return;
  }

  if (props.isEditMode && currentShiftId.value) {
    const updateRequest: UpdateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: form.value.departmentId!,
      shiftTemplateId: form.value.shiftTemplateId!,
      shiftDate: form.value.shiftDate,
      notes: form.value.notes,
      status: form.value.status!,
    };

    emit('shift-saved', {
      type: 'update',
      data: updateRequest,
      shiftId: currentShiftId.value,
    });
  } else {
    const createRequest: CreateShiftRequest = {
      nurseId: form.value.nurseId!,
      departmentId: undefined!,
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
  if (currentShiftId.value) {
    emit('shift-cancel-requested', currentShiftId.value);
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
