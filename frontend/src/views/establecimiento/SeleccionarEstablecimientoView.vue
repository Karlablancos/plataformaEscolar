<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center px-3" style="background: #f0f2f5;">
  <div class="card border-0 rounded-4" style="max-width: 480px; width: 100%; box-shadow: 0 25px 70px rgba(0,0,0,0.5);">
      <div class="card-body p-5">

        <div class="text-center mb-4">
        <img src="/iconos-plataforma-escolar/plataforma-escolar.png" alt="Plataforma Escolar" style="width: 80px; height: 80px; object-fit: contain;" class="mb-3" />
        <h1 class="h3 fw-bold mb-2">Libro de Clases Digital</h1>
        <p class="text-muted mb-0">Ingrese el RBD de su establecimiento para continuar</p>
        </div>

        <form @submit.prevent="buscarEstablecimiento">
          <div class="mb-3">
            <label for="rbd" class="form-label fw-semibold"> RBD del establecimiento </label>

            <input
              id="rbd"
              v-model="rbd"
              type="text"
              class="form-control form-control-lg rounded-pill px-4"
              placeholder="Ej: 25874-3"
              maxlength="10"
              autocomplete="off"
            />
          </div>

          <div
            v-if="establecimientoEncontrado"
            class="alert alert-success d-flex align-items-start gap-3 rounded-3"
          >
            <i class="bi bi-check-circle-fill fs-4"></i>

            <div>
              <div class="fw-semibold">
                {{ establecimientoEncontrado.nombre }}
              </div>
              <small class="text-muted"> RBD: {{ establecimientoEncontrado.rbd }} </small>
            </div>
          </div>

          <div v-if="error" class="alert alert-danger rounded-3">
            {{ error }}
          </div>

          <button type="submit" class="btn btn-lg w-100 rounded-pill fw-semibold" style="background: linear-gradient(135deg, #6f42c1, #4b0082); color: white; border: none; padding: 14px;" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>

            {{
              establecimientoEncontrado ? 'Continuar al inicio de sesión' : 'Buscar establecimiento'
            }}
          </button>
        </form>

        <div class="text-center mt-4">
          <small class="text-muted"> Plataforma de gestión académica escolar </small>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'

const router = useRouter()

const rbd = ref('')
const loading = ref(false)
const error = ref('')
const establecimientoEncontrado = ref(null)

const buscarEstablecimiento = async () => {
  error.value = ''
  loading.value = true
  establecimientoEncontrado.value = null

  try {
    const rbdLimpio = rbd.value.trim().split('-')[0]
    if (!rbdLimpio) {
      error.value = 'Ingresa el RBD del establecimiento.'
      return
    }
    const response = await api.get(`/auth/validar-rbd/${rbdLimpio}`)

    establecimientoEncontrado.value = {
      idEstablecimiento: response.data.idEstablecimiento,
      nombre: response.data.nombre,
      rbd: rbdLimpio,
    }
    localStorage.setItem('rbd', rbdLimpio)
    localStorage.setItem('establecimientoActivo', JSON.stringify(establecimientoEncontrado.value))

    setTimeout(() => {
      router.push('/login')
    }, 700)
  } catch {
    error.value = 'No se encontró un establecimiento con ese RBD.'
  } finally {
    loading.value = false
  }
}
</script>
