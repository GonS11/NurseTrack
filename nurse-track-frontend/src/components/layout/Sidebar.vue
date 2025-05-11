<template>
  <!-- Mobile Sidebar -->
  <transition name="fade">
    <div
      v-if="isMobile && isOpen"
      class="fixed inset-0 flex z-40 md:hidden"
      @click.self="onClose"
    >
      <div class="relative flex-1 flex flex-col max-w-xs w-full bg-white">
        <button
          type="button"
          class="absolute top-0 right-0 -mr-12 pt-2"
          @click="onClose"
        >
          <span class="sr-only">Close sidebar</span>
          <X class="h-6 w-6 text-gray-600" aria-hidden="true" />
        </button>
        <SidebarContent :navigation="navigation" />
      </div>
      <div class="flex-shrink-0 w-14" aria-hidden="true"></div>
    </div>
  </transition>

  <!-- Desktop Sidebar -->
  <div v-if="!isMobile" class="hidden md:flex md:flex-shrink-0">
    <div class="flex flex-col w-64">
      <SidebarContent :navigation="navigation" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { PropType } from 'vue';
import { useAuthStore } from '../../stores/auth.store';

import {
  HomeIcon,
  UserCircleIcon,
  BellIcon,
  BuildingOfficeIcon,
  ClockIcon,
  UserGroupIcon,
  UsersIcon,
  DocumentTextIcon,
  CalendarIcon,
  ClipboardIcon,
} from '@heroicons/vue/24/outline';

defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
  onClose: {
    type: Function as PropType<(event: MouseEvent) => void>,
    default: () => {},
  },
  isMobile: {
    type: Boolean,
    required: true,
  },
});

const authStore = useAuthStore();
const user = computed(() => authStore.user);

const navigation = computed(() => {
  const commonNavItems = [
    { name: 'Dashboard', href: '/', icon: HomeIcon },
    { name: 'Profile', href: '/profile', icon: UserCircleIcon },
    { name: 'Notifications', href: '/notifications', icon: BellIcon },
  ];

  const adminNavItems = [
    { name: 'Users', href: '/admin/users', icon: UserGroupIcon },
    {
      name: 'Departments',
      href: '/admin/departments',
      icon: BuildingOfficeIcon,
    },
    {
      name: 'Shift Templates',
      href: '/admin/shift-templates',
      icon: ClockIcon,
    },
  ];

  const supervisorNavItems = [
    {
      name: 'My Department',
      href: '/supervisor/department',
      BuildingOfficeIcon,
    },
    { name: 'Staff', href: '/supervisor/staff', UsersIcon },
    { name: 'Shifts', href: '/supervisor/shifts', CalendarIcon },
    { name: 'Requests', href: '/supervisor/requests', DocumentTextIcon },
  ];

  const nurseNavItems = [
    { name: 'My Departments', href: '/nurse/departments', BuildingOfficeIcon },
    { name: 'My Schedule', href: '/nurse/schedule', CalendarIcon },
    { name: 'Request Shift Swap', href: '/nurse/shift-swap', ClockIcon },
    { name: 'Request Vacation', href: '/nurse/vacation', ClipboardIcon },
  ];

  if (user.value?.role === 'ADMIN') {
    return [...commonNavItems, ...adminNavItems];
  } else if (user.value?.role === 'SUPERVISOR') {
    return [...commonNavItems, ...supervisorNavItems];
  } else if (user.value?.role === 'NURSE') {
    return [...commonNavItems, ...nurseNavItems];
  }

  return commonNavItems;
});
</script>

<style lang="scss" scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
