<template>
  <transition name="modal-fade">
    <div
      v-if="props.modelValue"
      class="confirm-modal-overlay"
      @click.self="onCancel"
    >
      <div class="confirm-modal-content">
        <p class="confirm-message">{{ props.message }}</p>
        <div class="confirm-actions">
          <button class="btn btn-danger" @click="onConfirm">Confirm</button>
          <button class="btn btn-outline" @click="onCancel">Cancel</button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

const props = defineProps<{
  modelValue: boolean;
  message: string;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'confirmed'): void;
  (e: 'cancelled'): void;
}>();

function onConfirm() {
  emit('confirmed');
  emit('update:modelValue', false);
}

function onCancel() {
  emit('cancelled');
  emit('update:modelValue', false);
}
</script>

<style lang="scss" scoped>
@use 'ConfirmModal.scss';
</style>
