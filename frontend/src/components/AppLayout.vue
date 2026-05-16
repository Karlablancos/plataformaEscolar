<template>
  <div>
    <nav class="navbar navbar-expand-lg bg-dark navbar-dark navbar-colegio">
      <div class="container">
        <div class="d-flex align-items-center gap-3">
          <RouterLink class="navbar-brand fw-bold mb-0" :to="homePath">
            {{ nombreColegio }}
          </RouterLink>

          <small class="d-none d-lg-inline">
            <span class="text-secondary">Hola! </span>
            <span class="primary-color-light">
              {{ auth.user?.nombre }} ({{ auth.user?.rol }})
            </span>
          </small>
        </div>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNavbar"
          aria-controls="mainNavbar"
          aria-expanded="false"
          aria-label="Abrir navegación"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div id="mainNavbar" class="collapse navbar-collapse justify-content-end">
          <ul class="navbar-nav align-items-lg-center gap-lg-2 mb-2 mb-lg-0">
            <li class="nav-item">
              <RouterLink class="nav-link" :to="homePath">Inicio</RouterLink>
            </li>

            <li class="nav-item" v-if="!auth.isProfesor">
              <RouterLink class="nav-link" to="/admin/establecimiento">
                Establecimiento
              </RouterLink>
            </li>

            <li class="nav-item" v-if="auth.isAdmin">
              <RouterLink class="nav-link" to="/admin/profesores"> Profesores </RouterLink>
            </li>

            <li class="nav-item" v-if="auth.isAdmin">
              <RouterLink class="nav-link" to="/admin/alumnos"> Alumnos </RouterLink>
            </li>

            <li class="nav-item">
              <RouterLink class="nav-link" :to="cursosPath"> Cursos </RouterLink>
            </li>

            <li class="nav-item" v-if="auth.isAdmin">
              <RouterLink class="nav-link" to="/admin/asignaturas"> Asignaturas </RouterLink>
            </li>

            <li class="nav-item ms-lg-2">
              <button class="btn btn-outline-light px-3 btn-sm" @click="logout">
                <i class="bi bi-box-arrow-right me-1"></i>
                Cerrar sesión
              </button>
            </li>
          </ul>

          <small class="d-lg-none text-white mt-3 d-block">
            <span class="text-secondary">Hola! </span>
            <span class="primary-color-light">
              {{ auth.user?.nombre }} ({{ auth.user?.rol }})
            </span>
          </small>
        </div>
      </div>
    </nav>

    <main class="container mt-5 py-4">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { useAcademicStore } from '../stores/academicStore'

const router = useRouter()
const auth = useAuthStore()
const academicStore = useAcademicStore()

const nombreColegio = computed(() => {
  return (
    academicStore.establecimientoActivo?.nombre ||
    academicStore.establecimiento?.nombre ||
    auth.user?.establecimientoNombre ||
    'Libro de Clases'
  )
})

const homePath = computed(() => (auth.isProfesor ? '/profesor/dashboard' : '/admin/dashboard'))

const cursosPath = computed(() => (auth.isProfesor ? '/profesor/cursos' : '/admin/cursos'))

const logout = () => {
  auth.logout()
  router.push('/')
}
</script>
