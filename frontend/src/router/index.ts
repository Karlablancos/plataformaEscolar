import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '../views/usuarios/LoginView.vue'
import DashboardAdminView from '../views/usuarios/DashboardAdminView.vue'

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

import EvaluacionesView from '../views/academico/EvaluacionesView.vue'
import LibroNotasView from '../views/academico/LibroNotasView.vue'
import PromocionView from '../views/academico/PromocionView.vue'

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
      roles: ['ADMINISTRADOR'],
    },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard-admin',
      },
      {
        path: 'dashboard-admin',
        name: 'dashboard-admin',
        component: DashboardAdminView,
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
      {
        path: 'academico/evaluaciones',
        name: 'evaluaciones',
        component: EvaluacionesView,
      },
      {
        path: 'academico/libro-notas',
        name: 'libro-notas',
        component: LibroNotasView,
      },
      {
        path: 'academico/promocion',
        name: 'promocion',
        component: PromocionView,
      },
    ],
  },

  {
    path: '/profesor',
    component: () => import('@/components/AppLayout.vue'),
    meta: {
      requiresAuth: true,
      roles: ['DOCENTE'],
    },
    children: [
      {
        path: '',
        redirect: '/profesor/dashboard-docente',
      },
      {
        path: 'dashboard-docente',
        name: 'profesor-dashboard',
        component: DashboardAdminView,
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
      {
        path: 'academico/evaluaciones',
        name: 'profesor-evaluaciones',
        component: EvaluacionesView,
      },
      {
        path: 'academico/libro-notas',
        name: 'profesor-libro-notas',
        component: LibroNotasView,
      },
      {
        path: 'academico/promocion',
        name: 'profesor-promocion',
        component: PromocionView,
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
  console.log('GUARD:', to.path, 'meta:', JSON.stringify(to.meta))
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  console.log('USER ROL:', user?.rol)
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
    if (to.path.startsWith('/admin') || to.path.startsWith('/profesor')) return
    if (user.rol === 'DOCENTE') return '/profesor/dashboard-docente'
    if (user.rol === 'ADMINISTRADOR') return '/admin/dashboard-admin'
    return '/admin/dashboard-admin'
  }

  if (to.meta.requiresAuth) {
    const allowedRoles = to.matched
      .map((r) => r.meta.roles)
      .find((roles) => roles) as string[] | undefined

    if (allowedRoles && user && !allowedRoles.includes(user.rol)) {
      if (user.rol === 'DOCENTE') return '/profesor/dashboard-docente'
      return '/admin/dashboard-admin'
    }
  }
})

export default router
