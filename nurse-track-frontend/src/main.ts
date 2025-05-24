import { createApp } from 'vue';
import { createPinia } from 'pinia';
import api from './api/axios';
import App from './App.vue';
import router from './router';

const app = createApp(App);
const pinia = createPinia();

// Si ya hay token guardado al recargar, lo inyectamos
const existingToken = localStorage.getItem('authToken');
if (existingToken) {
  api.defaults.headers.common.Authorization = `Bearer ${existingToken}`;
}

app.use(pinia);
app.use(router);
app.mount('#app');
