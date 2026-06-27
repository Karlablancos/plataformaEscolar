<template>
  <div class="container-fluid py-4">
    <div class="d-flex justify-content-between page-header align-items-center mb-4">
      <div>
        <h1 class="page-title mb-1">
          <i class="bi bi-door-open me-2"></i>
          Salas
        </h1>
        <p class="text-muted mb-0">Gestión de salas y espacios del establecimiento.</p>
      </div>

      <button class="btn btn-success btn-rounded" @click="abrirModalCrear">
        <i class="bi bi-plus-circle me-2"></i>
        Nueva sala
      </button>
    </div>

    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <!-- Filtros -->
    <div class="card border-0 shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-md-6">
            <label class="form-label">Buscar sala</label>
            <input
              v-model="busqueda"
              type="text"
              class="form-control"
              placeholder="Buscar por nombre o número"
            />
          </div>

          <div class="col-md-3">
            <label class="form-label">Tipo</label>
            <select v-model="filtroTipo" class="form-select">
              <option value="">Todos</option>
              <option v-for="tipo in tiposDisponibles" :key="tipo.valor" :value="tipo.valor">
                {{ tipo.etiqueta }}
              </option>
            </select>
          </div>

          <div class="col-md-3">
            <label class="form-label">Estado</label>
            <select v-model="filtroEstado" class="form-select">
              <option value="">Todos</option>
              <option value="ACTIVO">Activo</option>
              <option value="INACTIVO">Inactivo</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Tabla -->
    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div v-if="salasStore.cargando" class="text-center py-5">
          <span class="spinner-border text-primary" role="status"></span>
          <p class="text-muted mt-2 mb-0">Cargando salas...</p>
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>N°</th>
                <th>Nombre</th>
                <th>Tipo</th>
                <th>Piso</th>
                <th class="text-center">Capacidad</th>
                <th>Estado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="sala in salasFiltradas" :key="sala.id_sala">
                <td>
                  <span class="badge text-bg-light border">
                    {{ sala.numero ?? '—' }}
                  </span>
                </td>

                <td class="fw-semibold">{{ sala.nombre }}</td>

                <td>
                  <span class="badge rounded-pill" :class="badgeTipo(sala.tipo)">
                    {{ etiquetaTipo(sala.tipo) }}
                  </span>
                </td>

                <td>Piso {{ sala.piso }}</td>

                <td class="text-center">
                  <span class="fw-semibold" style="color:#1B4F9C">{{ sala.capacidad }}</span>
                  <small class="text-muted ms-1">alumnos</small>
                </td>

                <td>
                  <span
                    class="badge"
                    :class="sala.estado === 'ACTIVO' ? 'text-bg-success' : 'text-bg-secondary'"
                  >
                    {{ sala.estado === 'ACTIVO' ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>

                <td class="text-end">
                  <button
                    class="btn btn-outline-primary btn-sm btn-rounded me-2"
                    @click="abrirModalEditar(sala)"
                  >
                    <i class="bi bi-pencil-square me-1"></i>Editar
                  </button>

                  <button
                    class="btn btn-outline-danger btn-sm btn-rounded"
                    @click="eliminarSala(sala)"
                  >
                    <i class="bi bi-trash me-1"></i>Eliminar
                  </button>
                </td>
              </tr>

              <tr v-if="salasFiltradas.length === 0">
                <td colspan="7" class="text-center text-muted py-4">
                  No se encontraron salas.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal crear / editar -->
    <div
      v-if="modalAbierto"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.45)"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header">
            <h5 class="modal-title">
              {{ modoEdicion ? 'Editar sala' : 'Nueva sala' }}
            </h5>
            <button type="button" class="btn-close" @click="cerrarModal"></button>
          </div>

          <form @submit.prevent="guardarSala">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-8">
                  <label class="form-label">Nombre</label>
                  <input
                    v-model="form.nombre"
                    type="text"
                    class="form-control"
                    placeholder="Ej: Sala 1A"
                    required
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Número</label>
                  <input
                    v-model.number="form.numero"
                    type="number"
                    class="form-control"
                    placeholder="Opcional"
                    min="1"
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Tipo</label>
                  <select v-model="form.tipo" class="form-select" required>
                    <option value="">Seleccionar tipo</option>
                    <option v-for="tipo in tiposDisponibles" :key="tipo.valor" :value="tipo.valor">
                      {{ tipo.etiqueta }}
                    </option>
                  </select>
                </div>

                <div class="col-md-4">
                  <label class="form-label">Piso</label>
                  <input
                    v-model.number="form.piso"
                    type="number"
                    class="form-control"
                    placeholder="Ej: 1"
                    min="0"
                    required
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Capacidad (alumnos)</label>
                  <input
                    v-model.number="form.capacidad"
                    type="number"
                    class="form-control"
                    placeholder="Ej: 35"
                    min="1"
                    required
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select">
                    <option value="ACTIVO">Activo</option>
                    <option value="INACTIVO">Inactivo</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-danger btn-rounded" @click="cerrarModal">
                Cancelar
              </button>

              <button type="submit" class="btn btn-success btn-rounded" :disabled="guardando">
                <span v-if="guardando" class="spinner-border spinner-border-sm me-2"></span>
                Guardar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useSalasStore } from '@/stores/salasStore'

const salasStore = useSalasStore()

const busqueda = ref('')
const filtroTipo = ref('')
const filtroEstado = ref('')
const modalAbierto = ref(false)
const modoEdicion = ref(false)
const salaEditandoId = ref(null)
const guardando = ref(false)
const mensajeError = ref('')

const tiposDisponibles = [
  { valor: 'AULA', etiqueta: 'Aula' },
  { valor: 'LABORATORIO', etiqueta: 'Laboratorio' },
  { valor: 'SALA_ACTOS', etiqueta: 'Sala de actos' },
  { valor: 'BIBLIOTECA', etiqueta: 'Biblioteca' },
  { valor: 'GIMNASIO', etiqueta: 'Gimnasio' },
  { valor: 'SALA_REUNIONES', etiqueta: 'Sala de reuniones' },
  { valor: 'OTRO', etiqueta: 'Otro' },
]

const form = reactive({
  nombre: '',
  numero: null,
  tipo: '',
  piso: null,
  capacidad: null,
  estado: 'ACTIVO',
})

const normalizarTexto = (texto) =>
  String(texto || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '')

const salasFiltradas = computed(() => {
  const query = normalizarTexto(busqueda.value)

  return salasStore.salasFiltradasNormalizadas.filter((sala) => {
    const coincideBusqueda =
      !query ||
      normalizarTexto(sala.nombre).includes(query) ||
      String(sala.numero ?? '').includes(query)

    const coincideTipo = !filtroTipo.value || sala.tipo === filtroTipo.value

    const coincideEstado = !filtroEstado.value || sala.estado === filtroEstado.value

    return coincideBusqueda && coincideTipo && coincideEstado
  })
})

const etiquetaTipo = (tipo) => {
  const found = tiposDisponibles.find((t) => t.valor === tipo)
  return found ? found.etiqueta : tipo
}

const badgeTipo = (tipo) => {
  const mapa = {
    AULA: 'text-bg-primary',
    LABORATORIO: 'text-bg-info',
    SALA_ACTOS: 'text-bg-warning',
    BIBLIOTECA: 'text-bg-success',
    GIMNASIO: 'text-bg-danger',
    SALA_REUNIONES: 'text-bg-secondary',
    OTRO: 'text-bg-light border',
  }
  return mapa[tipo] || 'text-bg-light border'
}

const resetForm = () => {
  form.nombre = ''
  form.numero = null
  form.tipo = ''
  form.piso = null
  form.capacidad = null
  form.estado = 'ACTIVO'
  salaEditandoId.value = null
}

const abrirModalCrear = () => {
  resetForm()
  modoEdicion.value = false
  modalAbierto.value = true
}

const abrirModalEditar = (sala) => {
  form.nombre = sala.nombre || ''
  form.numero = sala.numero ?? null
  form.tipo = sala.tipo || ''
  form.piso = sala.piso ?? null
  form.capacidad = sala.capacidad ?? null
  form.estado = sala.estado || 'ACTIVO'

  salaEditandoId.value = sala.id_sala
  modoEdicion.value = true
  modalAbierto.value = true
}

const cerrarModal = () => {
  modalAbierto.value = false
  resetForm()
}

const extraerMensajeError = (error) =>
  error?.response?.data?.mensaje ||
  error?.response?.data?.message ||
  error?.message ||
  'Ocurrió un error al procesar la solicitud.'

const guardarSala = async () => {
  guardando.value = true
  mensajeError.value = ''

  try {
    if (modoEdicion.value && salaEditandoId.value) {
      await salasStore.actualizarSala(salaEditandoId.value, { ...form })
    } else {
      await salasStore.agregarSala({ ...form })
    }
    cerrarModal()
  } catch (error) {
    mensajeError.value = extraerMensajeError(error)
  } finally {
    guardando.value = false
  }
}

const eliminarSala = async (sala) => {
  const confirmacion = window.confirm(`¿Seguro que deseas eliminar la sala "${sala.nombre}"?`)
  if (!confirmacion) return

  mensajeError.value = ''

  try {
    await salasStore.eliminarSala(sala.id_sala)
  } catch (error) {
    mensajeError.value = extraerMensajeError(error)
  }
}

onMounted(() => {
  salasStore.cargarSalas().catch(() => {
    mensajeError.value = 'No se pudieron cargar las salas desde el servidor.'
  })
})
</script>
