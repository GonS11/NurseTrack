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
const SupervisorAssignment = () =>
  import('../views/admin/SupervisorAssignment.vue');
const NurseAssignment = () => import('../views/admin/NurseAssignment.vue');

// Supervisor
const SupervisorDashboard = () =>
  import('../views/dashboard/SupervisorDashboard.vue');
const SupervisorDepartmentAndStaff = () =>
  import('../views/supervisor/SupervisorManagement.vue');
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
const Notifications = () => import('../views/common/Notifications.vue');
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
            case UserRole.SUPERVISOR:
              return { name: 'supervisor-dashboard' };
            case UserRole.NURSE:
              return { name: 'nurse-dashboard' };
            default:
              return { name: 'login' };
          }
        },
      },

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

      {
        path: 'admin',
        meta: { allowedRoles: [UserRole.ADMIN] },
        children: [
          {
            path: 'admin-users',
            name: 'admin-users',
            component: UsersManagement,
          },
          {
            path: 'departments',
            name: 'admin-departments',
            component: DepartmentsManagement,
          },
          {
            path: 'supervisor-assignment',
            name: 'supervisor-assignment',
            component: SupervisorAssignment,
          },
          {
            path: 'nurse-assignment',
            name: 'nurse-assignment',
            component: NurseAssignment,
          },
        ],
      },

      {
        path: 'supervisor',
        meta: { allowedRoles: [UserRole.SUPERVISOR] },
        children: [
          {
            path: 'department-staff-management',
            name: 'supervisor-department-staff',
            component: SupervisorDepartmentAndStaff,
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

      {
        path: 'notifications',
        name: 'notifications',
        component: Notifications,
      },

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
