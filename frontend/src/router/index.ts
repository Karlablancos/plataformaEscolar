import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'

import SeleccionarEstablecimientoView from '../views/establecimiento/SeleccionarEstablecimientoView.vue'
import EstablecimientoView from '../views/establecimiento/EstablecimientoView.vue'

import AlumnosListView from '../views/alumnos/AlumnosListView.vue'
import AlumnoCreateView from '../views/alumnos/AlumnoCreateView.vue'
import AlumnoDetailView from '../views/alumnos/AlumnoDetailView.vue'

import CursosView from '../views/cursos/CursosView.vue'
import CursoDetalleView from '../views/cursos/CursoDetalleView.vue'

import ProfesoresView from '../views/profesores/ProfesoresView.vue'
import ProfesorFormView from '../views/profesores/ProfesorFormView.vue'
import ProfesorDetailView from '../views/profesores/ProfesorDetailView.vue'

import AsignaturasView from '../views/asignaturas/AsignaturasView.vue'

const routes = [
  {
    path: '/',
    name: 'seleccionar-establecimiento',
    component: SeleccionarEstablecimientoView,
    meta: {
      publicOnly: true,
    },
  },

  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresEstablecimiento: true,
      publicOnly: true,
    },
  },

  {
    path: '/admin',
    component: () => import('@/components/AppLayout.vue'),
    meta: {
      requiresAuth: true,
      roles: ['ADMIN', 'UTP', 'DIRECTOR'],
    },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard',
      },
      {
        path: 'dashboard',
        name: 'dashboard',
        component: DashboardView,
      },
      {
        path: 'establecimiento',
        name: 'establecimiento',
        component: EstablecimientoView,
      },
      {
        path: 'usuarios',
        name: 'usuarios',
        component: () => import('@/views/usuarios/UsuariosView.vue'),
      },
      {
        path: 'alumnos',
        name: 'alumnos',
        component: AlumnosListView,
      },
      {
        path: 'alumnos/nuevo',
        name: 'alumnos-nuevo',
        component: AlumnoCreateView,
      },
      {
        path: 'alumnos/:id',
        name: 'alumnos-detalle',
        component: AlumnoDetailView,
      },
      {
        path: 'alumnos/:id/editar',
        name: 'alumnos-editar',
        component: AlumnoDetailView,
      },
      {
        path: 'cursos',
        name: 'cursos',
        component: CursosView,
      },
      {
        path: 'cursos/:id',
        name: 'curso-detalle',
        component: CursoDetalleView,
      },
      {
        path: 'profesores',
        name: 'profesores',
        component: ProfesoresView,
      },
      {
        path: 'profesores/nuevo',
        name: 'profesores-nuevo',
        component: ProfesorFormView,
      },
      {
        path: 'profesores/:id',
        name: 'profesores-detalle',
        component: ProfesorDetailView,
      },
      {
        path: 'profesores/:id/editar',
        name: 'profesores-editar',
        component: ProfesorFormView,
      },
      {
        path: 'asignaturas',
        name: 'asignaturas',
        component: AsignaturasView,
      },
    ],
  },

  {
    path: '/sostenedor/dashboard',
    name: 'sostenedor-dashboard',
    component: () => import('@/views/sostenedor/SostenedorDashboardView.vue'),
    meta: {
      requiresAuth: true,
      roles: ['SOSTENEDOR'],
    },
  },

  {
    path: '/profesor',
    component: () => import('@/components/AppLayout.vue'),
    meta: {
      requiresAuth: true,
      roles: ['PROFESOR'],
    },
    children: [
      {
        path: '',
        redirect: '/profesor/dashboard',
      },
      {
        path: 'dashboard',
        name: 'profesor-dashboard',
        component: DashboardView,
      },
      {
        path: 'cursos',
        name: 'profesor-cursos',
        component: CursosView,
      },
      {
        path: 'cursos/:id',
        name: 'profesor-curso-detalle',
        component: CursoDetalleView,
      },
    ],
  },

  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  const establecimiento = JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')

  if (to.meta.requiresEstablecimiento && !establecimiento) {
    return '/'
  }

  if (to.meta.requiresAuth && !token) {
    return establecimiento ? '/login' : '/'
  }

  if (to.meta.requiresAuth && token && !user) {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    return establecimiento ? '/login' : '/'
  }

  if (to.meta.publicOnly && token && user) {
    if (user.rol === 'PROFESOR') return '/profesor/dashboard'
    if (user.rol === 'SOSTENEDOR') return '/sostenedor/dashboard'
    return '/admin/dashboard'
  }

  const allowedRoles = to.meta.roles as unknown as string[] | undefined

  if (allowedRoles && user && !allowedRoles.includes(user.rol)) {
    if (user.rol === 'PROFESOR') return '/profesor/dashboard'
    if (user.rol === 'SOSTENEDOR') return '/sostenedor/dashboard'
    return '/admin/dashboard'
  }
})

export default router
