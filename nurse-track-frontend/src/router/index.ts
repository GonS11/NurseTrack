import { createRouter, createWebHistory } from 'vue-router';
import AppShell from '../components/layout/AppShell.vue';
import { useAuthService } from '../services/shared/auth.service';

// Auth pages
const LoginPage = () => import('../views/auth/LoginPage.vue');

// Dashboard pages
const AdminDashboard = () => import('../views/dashboard/AdminDashboard.vue');
const SupervisorDashboard = () =>
  import('../views/dashboard/SupervisorDashboard.vue');
const NurseDashboard = () => import('../views/dashboard/NurseDashboard.vue');

// Protected route logic
const requireAuth = (allowedRoles?: string[]) => {
  return (to: any, from: any, next: any) => {
    const authStore = useAuthService;

    if (!authStore.isAuthenticated) {
      return next('/login');
    }

    if (
      allowedRoles &&
      authStore.user &&
      !allowedRoles.includes(authStore.user.role)
    ) {
      return next('/');
    }

    next();
  };
};

// Dashboard route logic based on user role
const DashboardRoute = () => {
  const authStore = useAuthService();

  if (!authStore.user) {
    return { path: '/login' };
  }

  switch (authStore.user.role) {
    case 'ADMIN':
      return { component: AdminDashboard };
    case 'SUPERVISOR':
      return { component: SupervisorDashboard };
    case 'NURSE':
      return { component: NurseDashboard };
    default:
      return { path: '/login' };
  }
};

const routes = [
  // Auth routes
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { public: true },
  },

  // App routes with AppShell layout
  {
    path: '/',
    component: AppShell,
    children: [
      // Dashboard - redirects based on user role
      {
        path: '',
        name: 'dashboard',
        beforeEnter: requireAuth(),
        component: DashboardRoute,
      },

      // Admin Routes
      {
        path: 'admin',
        children: [
          {
            path: 'users',
            name: 'admin-users',
            beforeEnter: requireAuth(['admin']),
            component: () => import('../views/admin/UsersManagement.vue'),
          },
          {
            path: 'departments',
            name: 'admin-departments',
            beforeEnter: requireAuth(['admin']),
            component: () => import('../views/admin/DepartmentsManagement.vue'),
          },
          {
            path: 'shift-templates',
            name: 'admin-shift-templates',
            beforeEnter: requireAuth(['admin']),
            component: () => import('../views/admin/ShiftTemplates.vue'),
          },
        ],
      },

      // Supervisor Routes
      {
        path: 'supervisor',
        children: [
          {
            path: 'department',
            name: 'supervisor-department',
            beforeEnter: requireAuth(['supervisor']),
            component: () =>
              import('../views/supervisor/DepartmentManagement.vue'),
          },
          {
            path: 'staff',
            name: 'supervisor-staff',
            beforeEnter: requireAuth(['supervisor']),
            component: () => import('../views/supervisor/StaffManagement.vue'),
          },
          {
            path: 'shifts',
            name: 'supervisor-shifts',
            beforeEnter: requireAuth(['supervisor']),
            component: () => import('../views/supervisor/ShiftSchedule.vue'),
          },
          {
            path: 'requests',
            name: 'supervisor-requests',
            beforeEnter: requireAuth(['supervisor']),
            component: () =>
              import('../views/supervisor/RequestsManagement.vue'),
          },
        ],
      },

      // Nurse Routes
      {
        path: 'nurse',
        children: [
          {
            path: 'departments',
            name: 'nurse-departments',
            beforeEnter: requireAuth(['nurse']),
            component: () => import('../views/nurse/MyDepartments.vue'),
          },
          {
            path: 'schedule',
            name: 'nurse-schedule',
            beforeEnter: requireAuth(['nurse']),
            component: () => import('../views/nurse/MySchedule.vue'),
          },
          {
            path: 'shift-swap',
            name: 'nurse-shift-swap',
            beforeEnter: requireAuth(['nurse']),
            component: () => import('../views/nurse/RequestShiftSwap.vue'),
          },
          {
            path: 'vacation',
            name: 'nurse-vacation',
            beforeEnter: requireAuth(['nurse']),
            component: () => import('../views/nurse/RequestVacation.vue'),
          },
        ],
      },

      // Common Routes
      {
        path: 'profile',
        name: 'profile',
        beforeEnter: requireAuth(),
        component: () => import('../views/common/UserProfile.vue'),
      },
      {
        path: 'notifications',
        name: 'notifications',
        beforeEnter: requireAuth(),
        component: () => import('../views/common/Notifications.vue'),
      },
      {
        path: 'settings',
        name: 'settings',
        beforeEnter: requireAuth(),
        component: () => import('../views/common/Settings.vue'),
      },
    ],
  },

  // Catch all route
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
