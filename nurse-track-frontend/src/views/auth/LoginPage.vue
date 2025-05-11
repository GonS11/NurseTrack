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
            <button @click="setDemoAccount('admin', 'password')">Admin</button>
            <button @click="setDemoAccount('supervisor', 'password')">
              Supervisor
            </button>
            <button @click="setDemoAccount('nurse', 'password')">Nurse</button>
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

const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const isLoading = ref(false);
const error = ref<string | null>(null);

const handleSubmit = async () => {
  isLoading.value = true;
  error.value = null;

  try {
    await authStore.login(username.value, password.value);
    window.location.href = '/dashboard'; // Redirect after successful login
  } catch (err: any) {
    error.value =
      err.response?.data?.message || 'Login failed. Please try again.';
  } finally {
    isLoading.value = false;
  }
};

const setDemoAccount = (demoUsername: string, demoPassword: string) => {
  username.value = demoUsername;
  password.value = demoPassword;
};
</script>

<style lang="scss" scoped>
/* Styles for the login page */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f9f9f9;

  .container {
    max-width: 400px;
    width: 100%;
    padding: 2rem;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

    .header {
      text-align: center;
      margin-bottom: 1.5rem;

      h1 {
        font-size: 2rem;
        margin-bottom: 0.5rem;
      }

      h2 {
        font-size: 1.2rem;
        color: #666;
        margin-bottom: 1rem;
      }
    }

    .form-container {
      .error-message {
        display: flex;
        align-items: center;
        background: #ffe5e5;
        color: #d9534f;
        padding: 0.5rem;
        border-radius: 4px;
        margin-bottom: 1rem;

        .icon {
          margin-right: 0.5rem;
        }
      }

      .login-form {
        .form-group {
          margin-bottom: 1rem;

          label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
          }

          .input-wrapper {
            display: flex;
            align-items: center;
            border: 1px solid #ccc;
            border-radius: 4px;
            padding: 0.5rem;

            .icon {
              margin-right: 0.5rem;
              color: #666;
            }

            input {
              border: none;
              outline: none;
              flex: 1;
            }
          }
        }

        .btn-primary {
          width: 100%;
          padding: 0.75rem;
          background: #007bff;
          color: #fff;
          border: none;
          border-radius: 4px;
          font-size: 1rem;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #0056b3;
          }

          &:disabled {
            background: #ccc;
            cursor: not-allowed;
          }
        }
      }

      .demo-accounts {
        margin-top: 1.5rem;
        text-align: center;

        .buttons {
          display: flex;
          justify-content: space-around;

          button {
            padding: 0.5rem 1rem;
            background: #f8f9fa;
            border: 1px solid #ccc;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s;

            &:hover {
              background: #e2e6ea;
            }
          }
        }
      }
    }
  }
}
</style>
