import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from 'vue-router';
import AppShell from '../components/layout/AppShell.vue';
import { useAuthStore } from '../services';
import { UserRole } from '../types/enums/user-role.enum';

// Lazy-loaded views
const LoginPage = () => import('../views/auth/LoginPage.vue');

// Admin
const AdminDashboard = () => import('../views/dashboard/AdminDashboard.vue');
const UsersManagement = () => import('../views/admin/UsersManagement.vue');
const DepartmentsManagement = () =>
  import('../views/admin/DepartmentsManagement.vue');
const ShiftTemplates = () => import('../views/admin/ShiftTemplates.vue');

// Supervisor
const SupervisorDashboard = () =>
  import('../views/dashboard/SupervisorDashboard.vue');
const DepartmentManagement = () =>
  import('../views/supervisor/DepartmentManagement.vue');
const StaffManagement = () => import('../views/supervisor/StaffManagement.vue');
const ShiftSchedule = () => import('../views/supervisor/ShiftSchedule.vue');
const RequestsManagement = () =>
  import('../views/supervisor/RequestsManagement.vue');

// Nurse
const NurseDashboard = () => import('../views/dashboard/NurseDashboard.vue');
const MyDepartments = () => import('../views/nurse/MyDepartments.vue');
const MySchedule = () => import('../views/nurse/MySchedule.vue');
const RequestShiftSwap = () => import('../views/nurse/RequestShiftSwap.vue');
const RequestVacation = () => import('../views/nurse/RequestVacation.vue');

// Common
const UserProfile = () => import('../views/common/UserProfile.vue');
const Notifications = () => import('../views/common/Notifications.vue');
const Settings = () => import('../views/common/Settings.vue');
const NotFound = () => import('../views/common/NotFound.vue');

const routes: RouteRecordRaw[] = [
  // Public
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { public: true },
  },

  // Protected
  {
    path: '/',
    component: AppShell,
    meta: { requiresAuth: true },
    children: [
      // Redirect root to role-based dashboard
      {
        path: '',
        name: 'dashboard',
        redirect: () => {
          const { user } = useAuthStore();

          if (user?.roles === null) return { name: 'login' };

          const mainRole = user?.roles[0].authority;

          switch (mainRole) {
            case UserRole.ADMIN:
              return { name: 'admin-dashboard' };
            case UserRole.ADMIN:
              return { name: 'supervisor-dashboard' };
            case UserRole.ADMIN:
              return { name: 'nurse-dashboard' };
            default:
              return { name: 'login' };
          }
        },
      },

      // Dashboards
      {
        path: 'dashboard/admin',
        name: 'admin-dashboard',
        component: AdminDashboard,
        meta: { allowedRoles: [UserRole.ADMIN] },
      },
      {
        path: 'dashboard/supervisor',
        name: 'supervisor-dashboard',
        component: SupervisorDashboard,
        meta: { allowedRoles: [UserRole.SUPERVISOR] },
      },
      {
        path: 'dashboard/nurse',
        name: 'nurse-dashboard',
        component: NurseDashboard,
        meta: { allowedRoles: [UserRole.NURSE] },
      },

      // Admin section
      {
        path: 'admin',
        meta: { allowedRoles: [UserRole.ADMIN] },
        children: [
          { path: 'users', name: 'admin-users', component: UsersManagement },
          {
            path: 'departments',
            name: 'admin-departments',
            component: DepartmentsManagement,
          },
          { path: 'shifts', name: 'admin-shifts', component: ShiftTemplates },
        ],
      },

      // Supervisor section
      {
        path: 'supervisor',
        meta: { allowedRoles: [UserRole.SUPERVISOR] },
        children: [
          {
            path: 'department',
            name: 'supervisor-department',
            component: DepartmentManagement,
          },
          {
            path: 'staff',
            name: 'supervisor-staff',
            component: StaffManagement,
          },
          {
            path: 'shifts',
            name: 'supervisor-shifts',
            component: ShiftSchedule,
          },
          {
            path: 'requests',
            name: 'supervisor-requests',
            component: RequestsManagement,
          },
        ],
      },

      // Nurse section
      {
        path: 'nurse',
        meta: { allowedRoles: [UserRole.NURSE] },
        children: [
          {
            path: 'departments',
            name: 'nurse-departments',
            component: MyDepartments,
          },
          { path: 'schedule', name: 'nurse-schedule', component: MySchedule },
          {
            path: 'shift-swap',
            name: 'nurse-shift-swap',
            component: RequestShiftSwap,
          },
          {
            path: 'vacation',
            name: 'nurse-vacation',
            component: RequestVacation,
          },
        ],
      },

      // Common routes
      { path: 'profile', name: 'profile', component: UserProfile },
      {
        path: 'notifications',
        name: 'notifications',
        component: Notifications,
      },
      { path: 'settings', name: 'settings', component: Settings },

      // 404 inside shell
      { path: ':pathMatch(.*)*', name: 'not-found', component: NotFound },
    ],
  },

  // Global 404
  { path: '/:pathMatch(.*)*', name: 'global-not-found', component: NotFound },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  const { isAuthenticated, user } = useAuthStore();

  // Redirect to login if not authenticated
  if (to.meta.requiresAuth && !isAuthenticated) {
    return next({ name: 'login', query: { redirect: to.fullPath } });
  }

  // Role-based access control
  const allowedRoles = to.meta.allowedRoles as UserRole[] | undefined;
  if (allowedRoles?.length && user?.roles) {
    const userRoles = user.roles.map((r) => r.authority);
    if (!userRoles.some((role) => allowedRoles.includes(role))) {
      // Insufficient permission: send to dashboard redirect
      return next({ name: 'dashboard' });
    }
  }

  next();
});

export default router;
