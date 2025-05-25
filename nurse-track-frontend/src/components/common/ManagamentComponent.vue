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
        {{ createButtonModal }}
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

//Props del componente generico
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
    type: Object as PropType<any>, //Compnent del modal
    required: true,
  },
  createButtonModal: {
    type: String,
    dedault: 'New Item',
  },
  //Para CRUD + paginacion
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
    required: true,
  },
  //Pa PK
  itemIdKey: {
    type: String,
    default: 'id',
  },
});

//Estado local del modal
const showModal = ref(false);
const selectedEntity = ref<any | null>(null);

//Fx para controlar modal
const openCreateModal = () => {
  selectedEntity.value = null; //En crear no se selecciona
  showModal.value = true;
};

// Necesitamos una forma para que la vista padre le diga a EntityManagement
// que se va a editar un item. Esto lo haremos a través de las acciones de la tabla.
// La acción "Edit" en UsersManagement.vue (o DepartmentsManagement.vue)
// llamará a una función que a su vez llama a este openModalForEdit.
const openUpdateModal = (entity: any) => {
  selectedEntity.value = entity;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedEntity.value = null; //Limpiar item
};

//Envio del modal
const handleModalSubmitInternal = async (formData: any) => {
  try {
    if (!selectedEntity.value) {
      //Si no entity selecionada es creacion
      await props.createItem(formData);
    } else {
      // Update
      const id = selectedEntity.value[props.itemIdKey];
      await props.updateItem(id, formData);
    }

    closeModal();
    await props.fetchData(props.data.number); //Refresca datos de la tabla
  } catch (error) {
    console.error('Error saving item: ', error);
    alert('Error saving item. Try agin later');
  }
};

//Cambio de pagina
const handlePageChange = async (newPage: number) => {
  await props.fetchData(newPage);
};

//
defineExpose({ openUpdateModal });
</script>

<style lang="scss" scoped>
@use 'ManagamentComponent.scss';
</style>
