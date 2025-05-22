import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from 'vue-router';
import AppShell from '../components/layout/AppShell.vue';
import { useAuthStore } from '../services';
import { UserRole } from '../types/enums/user-role.enum';
import { isUserRole } from '../utils/isUserRole';

// Lazy-loaded views
const LoginPage = () => import('../views/auth/LoginPage.vue');
const UsersManagement = () => import('../views/admin/UsersManagement.vue');
const DepartmentsManagement = () =>
  import('../views/admin/DepartmentsManagement.vue');
const ShiftTemplates = () => import('../views/admin/ShiftTemplates.vue');
const DepartmentManagement = () =>
  import('../views/supervisor/DepartmentManagement.vue');
const StaffManagement = () => import('../views/supervisor/StaffManagement.vue');
const ShiftSchedule = () => import('../views/supervisor/ShiftSchedule.vue');
const RequestsManagement = () =>
  import('../views/supervisor/RequestsManagement.vue');
const MyDepartments = () => import('../views/nurse/MyDepartments.vue');
const MySchedule = () => import('../views/nurse/MySchedule.vue');
const RequestShiftSwap = () => import('../views/nurse/RequestShiftSwap.vue');
const RequestVacation = () => import('../views/nurse/RequestVacation.vue');
const UserProfile = () => import('../views/common/UserProfile.vue');
const Notifications = () => import('../views/common/Notifications.vue');
const Settings = () => import('../views/common/Settings.vue');
const NotFound = () => import('../views/common/NotFound.vue');

// Route definitions
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { public: true },
  },
  {
    path: '/',
    component: AppShell,
    children: [
      {
        path: '',
        name: 'dashboard',
        meta: { requiresAuth: true },
        redirect: () => {
          const { user } = useAuthStore();

          // Añadir validación adicional
          if (!user || !user.role) return { name: 'login' };

          switch (user.role) {
            case UserRole.ADMIN:
              return { name: 'admin-users' };
            case UserRole.SUPERVISOR:
              return { name: 'supervisor-department' };
            case UserRole.NURSE:
              return { name: 'nurse-departments' };
            default:
              return { name: 'login' };
          }
        },
      },
      {
        path: 'admin',
        meta: { requiresAuth: true, allowedRoles: [UserRole.ADMIN] },
        children: [
          { path: 'users', name: 'admin-users', component: UsersManagement },
          {
            path: 'departments',
            name: 'admin-departments',
            component: DepartmentsManagement,
          },
          {
            path: 'shift-templates',
            name: 'admin-shift-templates',
            component: ShiftTemplates,
          },
        ],
      },
      // Supervisor
      {
        path: 'supervisor',
        meta: { requiresAuth: true, allowedRoles: [UserRole.SUPERVISOR] },
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
      // Nurse
      {
        path: 'nurse',
        meta: { requiresAuth: true, allowedRoles: [UserRole.NURSE] },
        children: [
          {
            path: 'departments',
            name: 'nurse-departments',
            component: MyDepartments,
          },
          {
            path: 'schedule',
            name: 'nurse-schedule',
            component: MySchedule,
          },
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
      // Common
      {
        path: 'profile',
        name: 'profile',
        component: UserProfile,
        meta: { requiresAuth: true },
      },
      {
        path: 'notifications',
        name: 'notifications',
        component: Notifications,
        meta: { requiresAuth: true },
      },
      {
        path: 'settings',
        name: 'settings',
        component: Settings,
        meta: { requiresAuth: true },
      },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'not-found', component: NotFound },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  const { isAuthenticated, user } = useAuthStore();

  if (to.meta.requiresAuth && !isAuthenticated) {
    return next({ name: 'login', query: { redirect: to.fullPath } });
  }

  const allowed = to.meta.allowedRoles as UserRole[] | undefined;
  if (allowed && user) {
    // Validamos que user.role es un UserRole
    if (!isUserRole(user.role) || !allowed.includes(user.role)) {
      return next({ name: 'dashboard' });
    }
  }

  next();
});

export default router;
