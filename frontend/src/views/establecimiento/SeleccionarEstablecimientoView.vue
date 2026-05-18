<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center bg-light px-3">
    <div class="card shadow-sm border-0 rounded-4" style="max-width: 480px; width: 100%">
      <div class="card-body p-5">
        <div class="text-center mb-4">
          <div
            class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
            style="width: 72px; height: 72px"
          >
            <i class="bi bi-building fs-1 text-primary"></i>
          </div>

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
              class="form-control form-control-lg"
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

          <button type="submit" class="btn btn-primary btn-lg w-100" :disabled="loading">
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
import axios from 'axios'

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
    const response = await axios.get(`http://localhost:8080/auth/validar-rbd/${rbdLimpio}`)
    
    establecimientoEncontrado.value = {
      nombre: response.data,
      rbd: rbdLimpio
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
