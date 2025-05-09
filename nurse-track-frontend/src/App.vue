<script setup lang="ts">
import { RouterView } from 'vue-router'; // Para manejar las rutas
// Store de autenticación
import { onMounted } from 'vue';
import { useAuthStore } from './services';

// Cargar el usuario actual al montar la aplicación
const authStore = useAuthStore();
onMounted(async () => {
  if (authStore.token) {
    try {
      await authStore.fetchUser(); // Cargar datos del usuario si hay un token
    } catch (error) {
      console.error('Error fetching user:', error);
      authStore.logout(); // Si falla, cerrar sesión
    }
  }
});
</script>

<template>
  <div id="app">
    <header>
      <nav>
        <h1>NurseTrack</h1>
        <ul>
          <li><router-link to="/">Home</router-link></li>
          <li v-if="authStore.user">
            <router-link to="/dashboard">Dashboard</router-link>
          </li>
          <li v-if="authStore.user">
            <button @click="authStore.logout">Logout</button>
          </li>
          <li v-else>
            <router-link to="/login">Login</router-link>
          </li>
        </ul>
      </nav>
    </header>

    <main>
      <RouterView />
      <!-- Aquí se renderizan las rutas -->
    </main>

    <footer>
      <p>&copy; 2025 NurseTrack. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  background-color: #007bff;
  color: white;
  padding: 1rem;
}

header nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

header nav ul {
  list-style: none;
  display: flex;
  gap: 1rem;
  margin: 0;
  padding: 0;
}

header nav ul li {
  display: inline;
}

header nav ul li a {
  color: white;
  text-decoration: none;
}

header nav ul li a:hover {
  text-decoration: underline;
}

main {
  flex: 1;
  padding: 2rem;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 1rem;
  border-top: 1px solid #ddd;
}
</style>
