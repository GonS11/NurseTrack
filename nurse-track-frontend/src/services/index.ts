import { createPinia } from 'pinia';

// Import individual stores
import { useAuthStore } from '../stores/auth.store'; // Adjust the path as necessary

// Create Pinia instance
const pinia = createPinia();

// Export Pinia instance
export default pinia;

// Export individual stores for direct usage
export { useAuthStore };
