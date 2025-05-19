// main.ts
import { createApp } from 'vue';
import { createPinia } from 'pinia'; // Importa createPinia
import App from './App.vue';
import router from './router';

const app = createApp(App);

// Crea la instancia de Pinia
const pinia = createPinia();

app.use(pinia);
app.use(router);

app.mount('#app');
