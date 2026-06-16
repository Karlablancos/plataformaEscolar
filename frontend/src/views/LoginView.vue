<template>
  <main class="min-vh-100 bg-light d-flex align-items-center justify-content-center p-3">
    <section class="card border-0 shadow-lg rounded-4 overflow-hidden login-card">
      <div class="row g-0 h-100">
        <div
          class="col-lg-6 d-none d-lg-flex bg-primary text-white p-5 flex-column justify-content-between"
        >
          <div class="fw-bold text-uppercase small letter-spacing">
            <i class="bi bi-cloud-fill me-2"></i>
            Libro Digital
          </div>

          <div>
            <div class="display-1 mb-4">
              <i class="bi bi-mortarboard-fill"></i>
            </div>

            <h2 class="fw-bold mb-3">Gestión académica escolar</h2>
            <p class="mb-0 opacity-75">
              Administra alumnos, cursos, docentes y asignaturas desde una sola plataforma.
            </p>
          </div>
        </div>

        <div class="col-12 col-lg-6">
          <div class="card-body p-4 p-md-5">
            <h1 class="h2 fw-bold text-primary text-uppercase mb-2">Login</h1>

            <p class="text-muted mb-1">Bienvenido al inicio de sesión de</p>
            <h2 class="h5 fw-bold mb-4">
              {{ establecimiento?.nombre || 'su establecimiento' }}
            </h2>

            <form @submit.prevent="login">
              <div class="input-group input-group-lg mb-3">
                <span class="input-group-text bg-white">
                  <i class="bi bi-envelope"></i>
                </span>

                <input
                  v-model="email"
                  type="text"
                  class="form-control"
                  placeholder="Usuario"
                />
              </div>

              <div class="input-group input-group-lg mb-3">
                <span class="input-group-text bg-white">
                  <i class="bi bi-lock"></i>
                </span>

                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  class="form-control"
                  placeholder="Contraseña"
                />

                <button
                  class="btn btn-outline-secondary"
                  type="button"
                  @click="showPassword = !showPassword"
                >
                  <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                </button>
              </div>

              <div class="d-flex justify-content-between align-items-center mb-4">
                <div class="form-check">
                  <input id="remember" class="form-check-input" type="checkbox" />
                  <label class="form-check-label small" for="remember"> Recordarme </label>
                </div>

                <a href="#" class="small text-decoration-none fw-semibold">
                  ¿Olvidaste tu contraseña?
                </a>
              </div>

              <div v-if="error" class="alert alert-danger py-2">
                {{ error }}
              </div>

              <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold text-uppercase">
                Iniciar sesión
              </button>

              <div class="text-center mt-4">
                <button
                  type="button"
                  class="btn btn-link btn-sm text-decoration-none"
                  @click="cambiarEstablecimiento"
                >
                  Cambiar establecimiento
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const error = ref('')
const showPassword = ref(false)

const establecimiento = JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')

const login = async () => {
  error.value = ''

  if (!establecimiento) {
    error.value = 'Debe seleccionar un establecimiento antes de iniciar sesión.'
    router.push('/')
    return
  }

  if (!email.value || !password.value) {
    error.value = 'Ingresa usuario y contraseña.'
    return
  }

  const rbd = establecimiento.rbd || establecimiento.id_establecimiento?.toString()
  const exito = await authStore.login(rbd, email.value, password.value)

  if (!exito) {
    error.value = 'Correo o contraseña incorrectos para este establecimiento.'
    return
  }

  const user = authStore.user
  let destino = '/admin/dashboard'
  if (user?.rol === 'PROFESOR') destino = '/profesor/dashboard'
  else if (user?.rol === 'SOSTENEDOR') destino = '/sostenedor/dashboard'
  router.replace(destino)
}

const cambiarEstablecimiento = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  localStorage.removeItem('establecimientoActivo')
  router.push('/')
}
</script>

<style scoped>
.login-card {
  max-width: 980px;
  width: 100%;
}

.letter-spacing {
  letter-spacing: 0.12em;
}
</style>
