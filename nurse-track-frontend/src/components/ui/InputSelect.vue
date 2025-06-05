<template>
  <div class="form-group">
    <label v-if="label" :for="id">{{ label }}</label>
    <div class="input-wrapper" :class="{ 'error-border': error }">
      <select
        :id="id"
        :value="modelValue"
        @change="handleChange"
        v-bind="$attrs"
      >
        <option :value="placeholderValue" disabled>
          {{ text }}
        </option>
        <option
          v-for="entity in entities"
          :key="entity[itemKey]"
          :value="entity[itemValue]"
        >
          {{ getOptionText(entity) }}
        </option>
      </select>
    </div>
    <span v-if="error" class="error-text">
      {{ error }}
    </span>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, type PropType } from 'vue';

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
  label: {
    type: String,
    default: '',
  },
  modelValue: {
    type: [String, Number, Boolean, null],
    default: null,
  },
  text: {
    type: String,
    default: 'Select an option',
  },
  entities: {
    type: Array as () => Record<string, any>[],
    required: true,
  },
  itemKey: {
    type: String,
    required: true,
    default: 'id',
  },
  itemValue: {
    type: String,
    required: true,
    default: 'id',
  },
  optionValue: {
    type: [String, Function] as PropType<string | ((entity: any) => string)>,
    required: true,
  },
  error: {
    type: [String, null],
    default: null,
  },
  placeholderValue: {
    type: [String, Number, null],
    default: null,
  },
});

const emit = defineEmits(['update:modelValue']);

const handleChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  emit('update:modelValue', target.value);
};

const getOptionText = (entity: any) => {
  if (typeof props.optionValue === 'function') {
    return props.optionValue(entity);
  }
  return entity[props.optionValue];
};
</script>

<style lang="scss" scoped>
@use 'InputSelect.scss';
</style>
