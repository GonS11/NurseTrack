<template>
  <div class="flex flex-col h-0 flex-1 bg-white">
    <div class="flex items-center flex-shrink-0 px-4">
      <h1 class="text-2xl font-bold text-primary-600">MediStaff</h1>
    </div>
    <nav class="mt-5 flex-1 px-2 space-y-1">
      <router-link
        v-for="item in navigation"
        :key="item.name"
        :to="item.href"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md"
        :class="{
          'bg-primary-50 text-primary-600': $route.path === item.href,
          'text-gray-600 hover:bg-gray-50 hover:text-gray-900':
            $route.path !== item.href,
        }"
      >
        <component
          :is="item.icon"
          class="mr-3 flex-shrink-0 h-5 w-5"
          :class="{
            'text-primary-500': $route.path === item.href,
            'text-gray-400 group-hover:text-gray-500':
              $route.path !== item.href,
          }"
        />
        {{ item.name }}
      </router-link>
    </nav>
    <div class="flex-shrink-0 flex p-4 border-t border-gray-200">
      <div class="flex-shrink-0 w-full group block">
        <div class="flex items-center">
          <img
            class="inline-block h-9 w-9 rounded-full"
            :src="userAvatar"
            alt="User Avatar"
          />
          <div class="ml-3">
            <p
              class="text-sm font-medium text-gray-700 group-hover:text-gray-900"
            >
              {{ user?.fullName || `${user?.firstName} ${user?.lastName}` }}
            </p>
            <p
              class="text-xs font-medium text-gray-500 group-hover:text-gray-700"
            >
              {{
                (user?.role ?? '').charAt(0).toUpperCase() +
                (user?.role ?? '').slice(1)
              }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useAuthStore } from '../../stores/auth.store';

interface NavigationItem {
  name: string;
  href: string;
  icon: string;
}

defineProps<{
  navigation: NavigationItem[];
}>();

const authStore = useAuthStore();
const user = computed(() => authStore.user);

const userAvatar = computed(
  () =>
    `https://ui-avatars.com/api/?name=${encodeURIComponent(
      user.value?.fullName || 'User',
    )}&background=random`,
);
</script>
