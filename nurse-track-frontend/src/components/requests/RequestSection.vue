<template>
  <section class="request-section">
    <h2>{{ title }}</h2>

    <div class="actions">
      <button
        @click="emit('fetch-requests', selectedDepartmentId, false)"
        :disabled="isLoading || selectedDepartmentId === null"
        class="btn-outline"
      >
        {{ isLoading ? 'Loading...' : 'Show Pending' }}
      </button>
      <button
        @click="emit('fetch-requests', selectedDepartmentId, true)"
        :disabled="isLoading || selectedDepartmentId === null"
        class="btn-outline"
      >
        {{ isLoading ? 'Loading...' : 'Show All' }}
      </button>
    </div>

    <div v-if="isLoading" class="loading-spinner">
      Loading {{ title.toLowerCase() }}...
    </div>

    <div v-if="!isLoading">
      <div
        v-if="selectedDepartmentId === null || requests.length === 0"
        class="no-requests"
      >
        {{
          selectedDepartmentId === null
            ? 'Select a department to view requests.'
            : `No ${title
                .toLowerCase()
                .replace(' requests', '')} found for this department.`
        }}
      </div>
      <div v-else class="requests-grid">
        <div
          v-for="request in requests"
          :key="request.id"
          class="request-card"
          :class="`status-${getStatusConfig(request.status).badgeStyle}`"
        >
          <div class="request-header">
            <h3>
              {{ title.replace(' Requests', ' Request') }} #{{ request.id }}
            </h3>
            <span
              :class="[
                'badge',
                `badge-${getStatusConfig(request.status).badgeStyle}`,
              ]"
            >
              {{ getStatusConfig(request.status).displayName }}
            </span>
          </div>
          <div class="request-details">
            <slot name="request-card-content" :request="request"></slot>
          </div>
          <div
            class="request-actions"
            v-if="request.status === RequestStatus.PENDING"
          >
            <button
              @click="emit('approve-request', request)"
              class="btn-primary"
            >
              Approve
            </button>
            <button
              @click="emit('reject-request', request.id)"
              class="btn-secondary"
            >
              Reject
            </button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, type PropType } from 'vue';

import {
  RequestStatus,
  RequestStatusData,
} from '../../types/enums/status.enum';

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  requests: {
    type: Array as () => any[],
    required: true,
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
  selectedDepartmentId: {
    type: Number as PropType<number | null>,
    default: null,
  },
  requestType: {
    type: String,
    required: true,
  },
});

const emit = defineEmits<{
  (e: 'fetch-requests', departmentId: number | null, all: boolean): void;
  (e: 'approve-request', request: any): void;
  (e: 'reject-request', requestId: number): void;
}>();

function getStatusConfig(status: RequestStatus) {
  return (
    RequestStatusData[status] || {
      displayName: status,
      badgeStyle: 'secondary',
    }
  );
}
</script>

<style scoped lang="scss">
@use 'RequestSection.scss';
</style>
