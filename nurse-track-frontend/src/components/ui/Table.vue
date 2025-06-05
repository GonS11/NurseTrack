<template>
  <div class="data-table">
    <div class="table-header">
      <h2 class="title">{{ title }}</h2>
      <div class="header-actions">
        <slot name="header-actions"></slot>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th v-for="(header, i) in headers" :key="i">
              {{ header.label }}
            </th>
            <th v-if="actions.length > 0">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in data" :key="item.id">
            <td
              v-for="(header, j) in headers"
              :key="j"
              :class="{ 'text-center': header.alignCenter }"
            >
              <template v-if="header.format">
                <span v-html="header.format(item)"></span>
              </template>
              <template v-else-if="$slots[`cell-${header.key}`]">
                <slot :name="`cell-${header.key}`" :item="item"></slot>
              </template>
              <template v-else>
                {{ item[header.key] }}
              </template>
            </td>
            <td v-if="actions.length > 0" class="actions-cell">
              <div class="actions-wrapper">
                <template v-for="(action, k) in actions" :key="k">
                  <button
                    v-if="showAction(action, item)"
                    class="action-button"
                    :class="action.class"
                    @click="handleAction(action, item)"
                    :title="action.label"
                    :disabled="action.disabled?.(item)"
                  >
                    <span class="material-icons">
                      {{ resolveIcon(action.icon, item) }}
                    </span>
                  </button>
                </template>
              </div>
            </td>
          </tr>
          <tr v-if="data.length === 0">
            <td :colspan="headers.length + (actions.length > 0 ? 1 : 0)">
              <div class="empty-state">
                <span class="material-icons">info</span>
                No data available
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showPagination" class="pagination-controls">
      <div class="pagination-info">
        {{ rangeStart }} - {{ rangeEnd }} of {{ totalItems }}
      </div>
      <div class="pagination-buttons">
        <button
          class="pagination-button"
          :disabled="currentPage === 0"
          @click="changePage(-1)"
        >
          <span class="material-icons">chevron_left</span>
        </button>
        <div class="page-indicator">{{ currentPage + 1 }}</div>
        <button
          class="pagination-button"
          :disabled="currentPage >= totalPages - 1"
          @click="changePage(1)"
        >
          <span class="material-icons">chevron_right</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, type PropType } from 'vue';

type Header = {
  key: string;
  label: string;
  format?: (item: any) => string;
  alignCenter?: boolean; // Nueva prop para centrar el texto de la celda
};

export interface TableAction<Item = any> {
  label: string;
  icon: string | ((item: Item) => string);
  handler: (item: Item) => void;
  class?: string;
  condition?: (item: Item) => boolean;
  disabled?: (item: Item) => boolean;
}

const props = defineProps({
  title: { type: String, required: true },
  headers: {
    type: Array as PropType<Header[]>,
    required: true,
  },
  data: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  actions: {
    type: Array as PropType<TableAction<any>[]>,
    default: () => [],
  },
  showPagination: { type: Boolean, default: false },
  currentPage: { type: Number, default: 0 },
  totalPages: { type: Number, default: 1 },
  totalItems: { type: Number, default: 0 },
  pageSize: { type: Number, default: 10 },
});

const emit = defineEmits<{
  (e: 'page-change', page: number): void;
  (e: 'action', payload: { action: string; item: any }): void;
}>();

const rangeStart = computed(() => props.currentPage * props.pageSize + 1);
const rangeEnd = computed(() =>
  Math.min((props.currentPage + 1) * props.pageSize, props.totalItems),
);

const resolveIcon = (icon: TableAction['icon'], item: any): string => {
  return typeof icon === 'function' ? icon(item) : icon;
};

const showAction = (action: TableAction, item: any) => {
  return !action.condition || action.condition(item);
};

const handleAction = (action: TableAction, item: any) => {
  emit('action', { action: action.label, item });
  action.handler(item); // The actual action handler
};

const changePage = (delta: number) => {
  const nextPage = props.currentPage + delta;
  if (nextPage >= 0 && nextPage < props.totalPages) {
    emit('page-change', nextPage);
  }
};
</script>

<style scoped lang="scss">
@use 'Table.scss';
</style>
