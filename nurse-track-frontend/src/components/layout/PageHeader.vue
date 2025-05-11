<template>
  <div class="page-header px-4 sm:px-6 lg:px-8 mb-6">
    <!-- Breadcrumbs -->
    <nav
      v-if="breadcrumbs && breadcrumbs.length > 0"
      class="flex mb-4"
      aria-label="Breadcrumb"
    >
      <ol class="flex items-center space-x-2">
        <li v-for="(crumb, idx) in breadcrumbs" :key="crumb.name">
          <div class="flex items-center">
            <svg
              v-if="idx > 0"
              class="flex-shrink-0 h-5 w-5 text-gray-400"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              aria-hidden="true"
            >
              <path
                fill-rule="evenodd"
                d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                clip-rule="evenodd"
              />
            </svg>
            <span
              v-if="crumb.isCurrent"
              class="ml-2 text-sm font-medium text-gray-500"
              aria-current="page"
            >
              {{ crumb.name }}
            </span>
            <router-link
              v-else
              :to="crumb.href"
              class="ml-2 text-sm font-medium text-primary-600 hover:text-primary-700"
            >
              {{ crumb.name }}
            </router-link>
          </div>
        </li>
      </ol>
    </nav>

    <!-- Title and Actions -->
    <div class="md:flex md:items-center md:justify-between">
      <div class="flex-1 min-w-0">
        <h1
          class="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate"
        >
          {{ title }}
        </h1>
        <p v-if="subtitle" class="mt-1 text-sm text-gray-500">{{ subtitle }}</p>
      </div>

      <div
        v-if="actions"
        class="mt-4 flex-shrink-0 flex md:mt-0 md:ml-4 space-x-3"
      >
        <slot name="actions" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';

interface Breadcrumb {
  name: string;
  href: string;
  isCurrent?: boolean;
}

defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    required: false,
  },
  breadcrumbs: {
    type: Array as PropType<Breadcrumb[]>,
    required: false,
  },
  actions: {
    type: Boolean,
    required: false,
    default: false,
  },
});
</script>

<style lang="scss" scoped>
.page-header {
  .breadcrumb {
    svg {
      height: 1.25rem;
      width: 1.25rem;
      color: #9ca3af;
    }

    a {
      color: #3b82f6;
      &:hover {
        color: #2563eb;
      }
    }

    span {
      color: #6b7280;
    }
  }

  h1 {
    color: #1f2937;
  }

  p {
    color: #6b7280;
  }

  .actions {
    display: flex;
    gap: 0.75rem;
  }
}
</style>
