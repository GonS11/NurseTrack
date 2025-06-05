<template>
  <div class="form-group">
    <label v-if="label" :for="id">{{ label }}</label>
    <div class="input-wrapper" :class="{ 'error-border': error }">
      <i v-if="icon" class="material-icons icon">{{ icon }}</i>
      <input
        :id="id"
        :type="type"
        :value="modelValue"
        @input="
          $emit('update:modelValue', ($event.target as HTMLInputElement).value)
        "
        :placeholder="placeholder"
        :required="required"
        v-bind="$attrs"
      />
    </div>
    <span v-if="error" class="error-text">
      {{ error }}
    </span>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
  label: {
    type: String,
    default: '',
  },
  type: {
    type: String,
    default: 'text',
  },
  modelValue: {
    type: [String, Number],
    default: '',
  },
  placeholder: {
    type: String,
    default: '',
  },
  required: {
    type: Boolean,
    default: false,
  },
  icon: {
    type: String,
    default: '',
  },
  error: {
    type: [String, null],
    default: null,
  },
});

const emit = defineEmits(['update:modelValue']);

//const attrs = useAttrs();
</script>

<style lang="scss" scoped>
@use 'Input.scss';
</style>
