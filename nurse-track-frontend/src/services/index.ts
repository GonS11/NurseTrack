import { createPinia } from 'pinia';

// Import individual stores
import { useAuthStore } from '../services/auth/auth.store';
import { useAdminUserStore } from '../services/admin/adminUser.store';
import { useSupervisorStore } from '../services/supervisor/supervisor.store';
import { useNurseStore } from '../services/nurse/nurse.store';
import { useNotificationStore } from '../services/shared/notification.store';

// Create Pinia instance
const pinia = createPinia();

// Export Pinia instance
export default pinia;

// Export individual stores for direct usage
export { useAuthStore };
