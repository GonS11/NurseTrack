<template>
  <div class="login-page">
    <div class="container">
      <div class="header">
        <h1>NurseTrack</h1>
        <h2>Healthcare Staff Scheduling</h2>
        <p>Sign in to access your account</p>
      </div>

      <div class="form-container">
        <div v-if="error" class="error-message">
          <ExclamationCircleIcon class="icon" />
          <span>{{ error }}</span>
        </div>

        <form @submit.prevent="handleSubmit" class="login-form">
          <div class="form-group">
            <label for="username">Username</label>
            <div class="input-wrapper">
              <EnvelopeIcon class="icon" />
              <input
                id="username"
                type="text"
                v-model="username"
                placeholder="Enter your username"
                required
              />
            </div>
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="input-wrapper">
              <LockClosedIcon class="icon" />
              <input
                id="password"
                type="password"
                v-model="password"
                placeholder="Enter your password"
                required
              />
            </div>
          </div>

          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading">Signing in...</span>
            <span v-else>Sign in</span>
          </button>
        </form>

        <div class="demo-accounts">
          <p>Demo accounts</p>
          <div class="buttons">
            <button @click="setDemoAccount('admin1', 'admin1')">Admin</button>
            <button @click="setDemoAccount('super1', 'super1')">
              Supervisor
            </button>
            <button @click="setDemoAccount('nurse1', 'nurse1')">Nurse</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

import {
  ExclamationCircleIcon,
  EnvelopeIcon,
  LockClosedIcon,
} from '@heroicons/vue/24/outline';
import { useAuthStore } from '../../stores/auth.store';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const isLoading = ref(false);
const error = ref<string | null>(null);
const router = useRouter();

const handleSubmit = async () => {
  try {
    await authStore.login(username.value, password.value);

    // Redirección segura con catch
    await router.push({ name: 'dashboard' });
  } catch (err: any) {
    // Mejorar manejo de errores
    if (err instanceof Error) {
      error.value = err.message;
    }
    console.error('Login error:', err);
  }
};

const setDemoAccount = (demoUsername: string, demoPassword: string) => {
  username.value = demoUsername;
  password.value = demoPassword;
};
</script>

<style lang="scss" scoped>
@use './LoginPage.scss';
</style>
