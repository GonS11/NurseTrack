<template>
  <Table
    :title="title"
    :headers="headers"
    :data="data.content"
    :actions="actions"
    :show-pagination="true"
    :current-page="data.number"
    :total-pages="data.totalPages"
    :total-items="data.totalElements"
    :page-size="data.size"
    @page-change="handlePageChange"
  >
    <template #header-actions>
      <button class="btn-primary" @click="openCreateModal">
        <span class="material-icons">add</span>
        <span class="text">{{ createButtonModal }}</span>
      </button>
    </template>
  </Table>

  <component
    :is="modalComponent"
    :isOpen="showModal"
    :item="selectedEntity"
    @close="closeModal"
    @submit="handleModalSubmitInternal"
  />
</template>

<script setup lang="ts">
import { ref, type PropType } from 'vue';
import Table, { type TableAction } from '../ui/Table.vue';
import type { Page } from '../../types/common';

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  headers: {
    type: Array as PropType<
      { key: string; label: string; format?: (item: any) => string }[]
    >,
    required: true,
  },
  data: {
    type: Object as PropType<Page<any>>,
    required: true,
  },
  actions: {
    type: Array as PropType<TableAction<any>[]>,
    required: true,
  },
  modalComponent: {
    type: Object as PropType<any>,
    required: true,
  },
  createButtonModal: {
    type: String,
    default: 'New Item',
  },
  fetchData: {
    type: Function as PropType<(page: number) => Promise<void>>,
    required: true,
  },
  createItem: {
    type: Function as PropType<(formData: any) => Promise<void>>,
    required: true,
  },
  updateItem: {
    type: Function as PropType<(id: number, formData: any) => Promise<void>>,
    required: false,
  },
  itemIdKey: {
    type: String,
    default: 'id',
  },
});

const showModal = ref(false);
const selectedEntity = ref<any | null>(null);

const openCreateModal = () => {
  selectedEntity.value = null;
  showModal.value = true;
};

const openUpdateModal = (entity: any) => {
  selectedEntity.value = entity;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedEntity.value = null;
};

const handleModalSubmitInternal = async (formData: any) => {
  try {
    if (!selectedEntity.value) {
      await props.createItem(formData);
    } else {
      if (props.updateItem) {
        const id = selectedEntity.value[props.itemIdKey];
        await props.updateItem(id, formData);
      } else {
        console.warn('Update operation not supported for this component type.');
      }
    }

    closeModal();
  } catch (error: any) {
    console.error('Error saving item in ManagementComponent: ', error);
    throw error;
  }
};

const handlePageChange = async (newPage: number) => {
  await props.fetchData(newPage);
};

defineExpose({
  openUpdateModal,
  closeCreateModal: closeModal,
  closeUpdateModal: closeModal,
});
</script>

<style lang="scss" scoped>
@use 'ManagamentComponent.scss';
</style>
