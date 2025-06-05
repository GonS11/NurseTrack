<template>
  <transition name="alert-fade">
    <div v-if="modelValue" :class="['alert-message', `alert-${type}`]">
      <p>{{ message }}</p>
      <button v-if="dismissible" @click="dismiss" class="alert-close-btn">
        &times;
      </button>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, type PropType } from 'vue';
import type { NotificationType } from '../../composables/useNotifications';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  message: {
    type: String,
    required: true,
  },
  type: {
    type: String as PropType<NotificationType>,
    default: 'info',
    validator: (value: string) =>
      ['info', 'success', 'warning', 'error'].includes(value),
  },
  dismissible: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'dismiss'): void;
}>();

function dismiss() {
  emit('dismiss');
  emit('update:modelValue', false);
}
</script>

<style lang="scss" scoped>
@use 'AlertMessage.scss';
</style>
