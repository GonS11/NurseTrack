import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../services';
import { UserRole } from '../types/enums/user-role.enum';
import AppShell from '../components/layout/AppShell.vue';

// Import the LoginPage component
const LoginPage = () => import('../views/auth/LoginPage.vue');

//Dashboards
const AdminDashboard = () => import('../views/dashboard/AdminDashboard.vue');
const SupervisorDashboard = () =>
  import('../views/dashboard/SupervisorDashboard.vue');
const NurseDashboard = () => import('../views/dashboard/NurseDashboard.vue');

//Protected route logic
const requiresAuth = (allowedRoles?: string[]) => {
  return (_to: any, _from: any, next: any) => {
    const authStore = useAuthStore();

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

//Route logic on user role
const DashboardRoute = () => {
  const authStore = useAuthStore();

  if (!authStore.user) {
    return { path: '/login' };
  }

  switch (authStore.user.role) {
    case UserRole.ADMIN:
      return { component: AdminDashboard };
    case UserRole.SUPERVISOR:
      return { component: SupervisorDashboard };
    case UserRole.NURSE:
      return { component: NurseDashboard };
    default:
      return { path: '/login' };
  }
};

const routes = [
  //Auth routes
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
      // Dashboard - Redirect based on user role
      {
        path: '',
        name: 'dashboard',
        beforeEnter: requiresAuth(),
        component: DashboardRoute,
      },

      // Admin Routes
      {
        path: 'admin',
        children: [
          {
            path: 'users',
            name: 'admin-users',
            beforeEnter: requiresAuth([UserRole.ADMIN]),
            component: () => import('../views/admin/UsersManagement.vue'),
          },
          {
            path: 'departments',
            name: 'admin-departments',
            beforeEnter: requiresAuth([UserRole.ADMIN]),
            component: () => import('../views/admin/DepartmentsManagement.vue'),
          },
          {
            path: 'shift-templates',
            name: 'admin-shift-templates',
            beforeEnter: requiresAuth([UserRole.ADMIN]),
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
            beforeEnter: requiresAuth([UserRole.SUPERVISOR]),
            component: () =>
              import('../views/supervisor/DepartmentManagement.vue'),
          },
          {
            path: 'staff',
            name: 'supervisor-staff',
            beforeEnter: requiresAuth([UserRole.SUPERVISOR]),
            component: () => import('../views/supervisor/StaffManagement.vue'),
          },
          {
            path: 'shifts',
            name: 'supervisor-shifts',
            beforeEnter: requiresAuth([UserRole.SUPERVISOR]),
            component: () => import('../views/supervisor/ShiftSchedule.vue'),
          },
          {
            path: 'requests',
            name: 'supervisor-requests',
            beforeEnter: requiresAuth([UserRole.SUPERVISOR]),
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
            beforeEnter: requiresAuth([UserRole.NURSE]),
            component: () => import('../views/nurse/MyDepartments.vue'),
          },
          {
            path: 'schedule',
            name: 'nurse-schedule',
            beforeEnter: requiresAuth([UserRole.NURSE]),
            component: () => import('../views/nurse/MySchedule.vue'),
          },
          {
            path: 'shift-swap',
            name: 'nurse-shift-swap',
            beforeEnter: requiresAuth([UserRole.NURSE]),
            component: () => import('../views/nurse/RequestShiftSwap.vue'),
          },
          {
            path: 'vacation',
            name: 'nurse-vacation',
            beforeEnter: requiresAuth([UserRole.NURSE]),
            component: () => import('../views/nurse/RequestVacation.vue'),
          },
        ],
      },

      // Common Routes
      {
        path: 'profile',
        name: 'profile',
        beforeEnter: requiresAuth(),
        component: () => import('../views/common/UserProfile.vue'),
      },
      {
        path: 'notifications',
        name: 'notifications',
        beforeEnter: requiresAuth(),
        component: () => import('../views/common/Notifications.vue'),
      },
      {
        path: 'settings',
        name: 'settings',
        beforeEnter: requiresAuth(),
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
