<template>
  <div class="login-page">
    <div class="container">
      <div class="header">
        <h1>NurseTrack</h1>
        <h2>Healthcare Staff Scheduling</h2>
        <p>Sign in to access your account</p>
      </div>

      <div class="form-container">
        <div v-if="formGeneralError" class="error-message">
          <i class="material-icons icon">error</i>
          <span>{{ formGeneralError }}</span>
        </div>

        <form @submit.prevent="handleSubmit" class="login-form">
          <Input
            id="username"
            label="Username"
            icon="person"
            type="text"
            v-model="username"
            placeholder="Enter your username"
            :error="errors.username"
            required
          />

          <Input
            id="password"
            label="Password"
            icon="lock"
            type="password"
            v-model="password"
            placeholder="Enter your password"
            :error="errors.password"
            required
          />

          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading">Signing in...</span>
            <span v-else>Sign in</span>
          </button>
        </form>

        <div class="demo-accounts">
          <p>Demo accounts</p>
          <div class="buttons">
            <button @click="setDemoAccount('admin1', 'admin11234')">
              Admin
            </button>
            <button @click="setDemoAccount('super1', 'super11234')">
              Supervisor
            </button>
            <button @click="setDemoAccount('nurse1', 'nurse11234')">
              Nurse
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../../services';
import { AuthSchemas } from '../../types/schemas/auth.schema';
import { ZodError } from 'zod';
import Input from '../../components/ui/Input.vue';
import { useFormErrors } from '../../utils/formValidation';
import { useNotifications } from '../../composables/useNotifications';

const auth = useAuthStore();
const router = useRouter();
const notifications = useNotifications();

const username = ref('');
const password = ref('');
const isLoading = ref(false);

const formGeneralError = ref<string | null>(null);

const { errors, mapZodErrors, clearErrors } = useFormErrors({
  username: null,
  password: null,
});

const validateForm = (): boolean => {
  clearErrors();
  formGeneralError.value = null;

  try {
    AuthSchemas.authenticationRequest.parse({
      username: username.value,
      password: password.value,
    });

    return true;
  } catch (err: any) {
    if (err instanceof ZodError) {
      mapZodErrors(err);
    } else {
      formGeneralError.value =
        err.message || 'An unexpected error occurred during validation.';
    }

    return false;
  }
};

const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }

  isLoading.value = true;
  formGeneralError.value = null;

  try {
    await auth.login(username.value, password.value);
    await router.push({ name: 'dashboard' });
    notifications.showSuccess('Login successful! Welcome.');
  } catch (err: any) {
    notifications.showError(
      err.message || 'Login failed. Please check your credentials.',
    );
  } finally {
    isLoading.value = false;
  }
};

const setDemoAccount = (name: string, passw: string) => {
  username.value = name;
  password.value = passw;
  clearErrors();
  formGeneralError.value = null;
};
</script>

<style lang="scss" scoped>
@use './LoginPage.scss';
</style>
