<template>
  <header
    class="header sticky top-0 z-10 flex-shrink-0 flex h-16 bg-white shadow"
  >
    <!-- Mobile menu button -->
    <button
      type="button"
      class="px-4 border-r border-gray-200 text-gray-500 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500 md:hidden"
    >
      <span class="sr-only">Open sidebar</span>
      <Bars3Icon class="h-6 w-6" aria-hidden="true" />
    </button>

    <div class="flex-1 px-4 flex justify-between">
      <!-- App title for mobile -->
      <div class="flex-1 flex">
        <h1
          class="md:hidden flex items-center text-xl font-bold text-primary-600"
        >
          NurseTrack
        </h1>
      </div>

      <div class="ml-4 flex items-center md:ml-6 space-x-4">
        <!-- Notification button -->
        <router-link
          to="/notifications"
          class="relative p-1 rounded-full text-gray-400 hover:text-gray-500"
        >
          <span class="sr-only">View notifications</span>
          <BellIcon class="h-6 w-6" aria-hidden="true" />
          <span
            class="absolute top-0 right-0 block h-2 w-2 rounded-full bg-error-500 ring-2 ring-white"
          ></span>
        </router-link>

        <!-- Profile dropdown -->
        <div class="relative">
          <button
            class="max-w-xs bg-white rounded-full flex items-center text-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            @click="toggleDropdown"
          >
            <span class="sr-only">Open user menu</span>
            <img
              class="h-8 w-8 rounded-full"
              :src="userAvatar"
              alt="User Avatar"
            />
          </button>

          <transition
            name="dropdown"
            enter-active-class="transition ease-out duration-100"
            leave-active-class="transition ease-in duration-75"
          >
            <div
              v-if="dropdownOpen"
              class="origin-top-right absolute right-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 divide-y divide-gray-100 focus:outline-none"
            >
              <div class="px-4 py-3">
                <p class="text-sm">Signed in as</p>
                <p class="text-sm font-medium text-gray-900 truncate">
                  {{ user?.email }}
                </p>
              </div>
              <div class="py-1">
                <router-link
                  to="/profile"
                  class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                >
                  <UserCircleIcon
                    class="mr-3 h-5 w-5 text-gray-400"
                    aria-hidden="true"
                  />
                  Your Profile
                </router-link>
                <router-link
                  to="/settings"
                  class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                >
                  <Cog6ToothIcon
                    class="mr-3 h-5 w-5 text-gray-400"
                    aria-hidden="true"
                  />
                  Settings
                </router-link>
              </div>
              <div class="py-1">
                <button
                  @click="logout"
                  class="flex items-center w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                >
                  <ArrowLeftEndOnRectangleIcon
                    class="mr-3 h-5 w-5 text-gray-400"
                    aria-hidden="true"
                  />
                  Sign out
                </button>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAuthStore } from '../../stores/auth.store';
import {
  BellIcon,
  Bars3Icon,
  ArrowLeftEndOnRectangleIcon,
  UserCircleIcon,
  Cog6ToothIcon,
} from '@heroicons/vue/24/outline';

const authStore = useAuthStore();
const dropdownOpen = ref(false);

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
};

const logout = () => {
  authStore.logout();
  window.location.href = '/login';
};

const user = computed(() => authStore.user);
const userAvatar = computed(
  () =>
    `https://ui-avatars.com/api/?name=${encodeURIComponent(
      user.value?.fullName || 'User',
    )}&background=random`,
);
</script>

<style lang="scss" scoped>
.header {
  .dropdown {
    &-enter-active {
      transition: transform 0.1s ease-out, opacity 0.1s ease-out;
    }
    &-leave-active {
      transition: transform 0.1s ease-in, opacity 0.1s ease-in;
    }
    &-enter-from,
    &-leave-to {
      transform: scale(0.95);
      opacity: 0;
    }
    &-enter-to,
    &-leave-from {
      transform: scale(1);
      opacity: 1;
    }
  }
}
</style>
