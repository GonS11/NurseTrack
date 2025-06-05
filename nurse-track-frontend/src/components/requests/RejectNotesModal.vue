<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-content">
      <h2>Reject Request</h2>
      <p>Please provide notes for rejecting this request:</p>
      <textarea v-model="internalNotes" rows="5"></textarea>
      <div class="modal-actions">
        <button @click="emit('cancelled')" class="btn-outline">Cancel</button>
        <button @click="emit('confirmed', internalNotes)" class="btn-danger">
          Reject
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, ref, watch } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  notes: {
    type: String,
    default: '',
  },
});

const emit = defineEmits<{
  (e: 'update:show', value: boolean): void;
  (e: 'update:notes', value: string): void;
  (e: 'confirmed', notes: string): void;
  (e: 'cancelled'): void;
}>();

const internalNotes = ref(props.notes);

watch(
  () => props.notes,
  (newNotes) => {
    internalNotes.value = newNotes;
  },
);

watch(internalNotes, (newVal) => {
  emit('update:notes', newVal);
});

watch(
  () => props.show,
  (newVal) => {
    if (!newVal) {
      internalNotes.value = '';
    }
  },
);
</script>

<style scoped lang="scss">
@use 'RejectNotesModal.scss';
</style>
