import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useAuthStore } from './stores/auth.store';
import './styles/main.scss';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);

// Initialize auth store AFTER pinia is installed, but BEFORE mounting the app
// This ensures the store is ready and auth state is rehydrated before any
// components or routes try to access it.
const authStore = useAuthStore();
// Using a promise here if initializeAuth was async, but it can be sync if just decoding.
// If it has async calls (e.g. to refresh token), you might await it here,
// though generally you wouldn't block app mount for initial token check.
// For now, we'll let it run in the background.
authStore.initializeAuth();

app.mount('#app');
