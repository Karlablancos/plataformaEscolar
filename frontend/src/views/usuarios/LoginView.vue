<template>
  <main
    class="min-vh-100 d-flex align-items-center justify-content-center p-3"
    style="background: linear-gradient(135deg, #f0f4f8 0%, #e8edf5 100%)"
  >
    <section class="card border-0 shadow-lg rounded-4 overflow-hidden login-card">
      <div class="row g-0 h-100">

        <!-- Panel izquierdo — plataforma genérica -->
        <div class="col-lg-6 d-none d-lg-flex flex-column justify-content-between p-5 left-panel">
        <div class="d-flex align-items-center gap-3">
          <img src="/iconos-plataforma-escolar/plataforma-escolar.png" alt="Plataforma Escolar" style="width: 52px; height: 52px; object-fit: contain; flex-shrink: 0" />
          <h1 class="fw-bold mb-0 text-white" style="font-size: 1.90rem; line-height: 1.2">Libro de Clases Digital</h1>
        </div>

          <div>
            <div class="d-flex align-items-center justify-content-between mb-2">
              <h2 class="fw-bold mb-0 text-white">Gestión académica escolar</h2>
              <img src="/iconos-plataforma-escolar/virrete.png" alt="Birrete" style="width: 90px; height: 90px; object-fit: contain; flex-shrink: 0" />
            </div>
            <div class="mb-3 accent-bar"></div>
            <p class="mb-4 text-white opacity-75">
              Administra alumnos, cursos, docentes y asignaturas desde una sola plataforma.
            </p>

            <div class="d-flex gap-3">
              <div class="feature-chip">
                <div class="feature-chip-icon"><i class="bi bi-people-fill"></i></div>
                <div class="text-white fw-semibold small mt-2">Alumnos</div>
                <div class="text-white opacity-75 feature-desc">Gestión completa</div>
              </div>
              <div class="feature-chip">
                <div class="feature-chip-icon"><i class="bi bi-book-fill"></i></div>
                <div class="text-white fw-semibold small mt-2">Cursos</div>
                <div class="text-white opacity-75 feature-desc">Organización eficiente</div>
              </div>
              <div class="feature-chip">
                <div class="feature-chip-icon"><i class="bi bi-bar-chart-fill"></i></div>
                <div class="text-white fw-semibold small mt-2">Reportes</div>
                <div class="text-white opacity-75 feature-desc">Información en tiempo real</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Panel derecho — colores de la institución en el formulario -->
        <div class="col-12 col-lg-6 bg-white">
          <div class="card-body p-4 p-md-5">
            <div class="text-center mb-3">
              <img
                src="/logo-BOH.png"
                alt="Plataforma Escolar"
                style="width: 80px; height: 80px; object-fit: contain"
              />
            </div>

            <h1 class="h2 fw-bold mb-2">Bienvenido de nuevo 👋</h1>
            <p class="mb-1">Inicia sesión para continuar en</p>
            <h2 class="h5 fw-bold mb-4 text-uppercase">
              {{ establecimiento?.nombre || 'su establecimiento' }}
            </h2>

            <form @submit.prevent="login">
              <!-- Email con floating label -->
              <div class="input-group input-group-lg mb-3 floating-group">
                <span class="input-group-text bg-white border-end-0 icon-institucion">
                  <i class="bi bi-envelope"></i>
                </span>
                <div class="form-floating flex-grow-1">
                  <input
                    id="login-email"
                    v-model="email"
                    type="text"
                    class="form-control border-start-0"
                    placeholder="Correo institucional"
                  />
                  <label for="login-email">Correo institucional</label>
                </div>
              </div>

              <!-- Contraseña con floating label -->
              <div class="input-group input-group-lg mb-3 floating-group">
                <span class="input-group-text bg-white border-end-0 icon-institucion">
                  <i class="bi bi-lock"></i>
                </span>
                <div class="form-floating flex-grow-1">
                  <input
                    id="login-password"
                    v-model="password"
                    :type="showPassword ? 'text' : 'password'"
                    class="form-control border-start-0 border-end-0"
                    placeholder="Contraseña"
                  />
                  <label for="login-password">Contraseña</label>
                </div>
                <button
                  class="btn btn-outline-secondary border-start-0"
                  type="button"
                  @click="showPassword = !showPassword"
                >
                  <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                </button>
              </div>

              <div class="d-flex justify-content-between align-items-center mb-4">
                <div class="form-check">
                  <input id="remember" class="form-check-input check-institucion" type="checkbox" />
                  <label class="form-check-label small" for="remember">Recordarme</label>
                </div>
                <a href="#" class="small text-decoration-none fw-semibold link-institucion">
                  ¿Olvidaste tu contraseña?
                </a>
              </div>

              <div v-if="error" class="alert alert-danger py-2">
                {{ error }}
              </div>

              <button
                type="submit"
                class="btn btn-lg w-100 fw-bold text-uppercase text-white d-flex align-items-center justify-content-center gap-2 btn-login"
              >
                Iniciar sesión
                <i class="bi bi-arrow-right"></i>
              </button>

              <div class="d-flex align-items-center gap-2 my-3">
                <hr class="flex-grow-1 m-0" />
                <small class="text-muted">o</small>
                <hr class="flex-grow-1 m-0" />
              </div>

              <button
                type="button"
                class="btn btn-lg w-100 fw-semibold d-flex align-items-center justify-content-center gap-2 btn-cambiar"
                @click="cambiarEstablecimiento"
              >
                <i class="bi bi-building"></i>
                Cambiar establecimiento
              </button>
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
import { useAuthStore } from '@/stores/authStore'

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
  let destino = '/admin/dashboard-admin'
  if (user?.rol === 'DOCENTE') destino = '/profesor/dashboard-docente'
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

/* ── Panel izquierdo ── */
.left-panel {
  background: linear-gradient(135deg, #6f42c1 0%, #4b0082 100%);
  position: relative;
  overflow: hidden;
}

.left-panel::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle, rgba(255, 255, 255, 0.12) 1px, transparent 1px);
  background-size: 22px 22px;
  pointer-events: none;
}

.left-panel > * {
  position: relative;
}

.accent-bar {
  width: 40px;
  height: 3px;
  background: #6f42c1;
  border-radius: 2px;
}

/* Feature chips horizontales */
.feature-chip {
  flex: 1;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 12px 10px;
  text-align: center;
}

.feature-chip-icon {
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1rem;
}

.feature-desc {
  font-size: 0.7rem;
}

/* ── Colores institucionales (panel derecho) ── */

/* ── Colores institucionales — Colegio Bernardo O'Higgins Coquimbo ──
   Rojo: #c8102e  |  Azul: #003087  |  Dorado: #f0c030
── */

.icon-institucion {
  color: #212529;
}

/* Inputs: borde de foco en rojo institucional */
.floating-group .form-floating > label {
  padding-left: 0.5rem;
}

.floating-group .form-floating > .form-control {
  height: calc(3.5rem + 2px);
}

.floating-group .form-floating > .form-control:focus {
  box-shadow: none;
}

.floating-group:focus-within .input-group-text,
.floating-group:focus-within .form-control,
.floating-group:focus-within .btn-outline-secondary {
  border-color: #c8102e;
}

/* Checkbox institucional */
.check-institucion:checked {
  background-color: #c8102e;
  border-color: #c8102e;
}

/* Link institucional */
.link-institucion {
  color: #212529;
}

/* Botón iniciar sesión — rojo institucional */
.btn-login {
  background-color: #c8102e;
  border: none;
  border-radius: 0.5rem;
  padding: 14px;
  transition: background-color 0.2s ease;
}

.btn-login:hover {
  background-color: #9e0b24;
  color: white;
}

/* Botón cambiar establecimiento */
.btn-cambiar {
  border: 2px solid #dee2e6;
  color: #6f42c1;
  border-radius: 0.5rem;
  background: white;
  transition: border-color 0.2s ease, background-color 0.2s ease;
}

.btn-cambiar:hover {
  border-color: #6f42c1;
  background-color: #f8f9fa;
  color: #4b0082;
}
</style>
