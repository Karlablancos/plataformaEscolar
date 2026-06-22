<template>
  <div class="container-fluid py-4">
    <div class="d-flex justify-content-between page-header align-items-center mb-4">
      <div>
        <h1 class="page-title mb-1">
          <i class="bi bi-journal-bookmark me-2"></i>
          Asignaturas
        </h1>
        <p class="text-muted mb-0">Catálogo institucional de asignaturas del establecimiento.</p>
      </div>

      <button class="btn btn-success btn-rounded" @click="abrirModalCrear">
        <i class="bi bi-plus-circle me-2"></i>
        Nueva asignatura
      </button>
    </div>

    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <div class="card border-0 shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-md-8">
            <label class="form-label">Buscar asignatura</label>
            <input
              v-model="busqueda"
              type="text"
              class="form-control"
              placeholder="Buscar por nombre o código"
            />
          </div>

          <div class="col-md-4">
            <label class="form-label">Tipo de calificación</label>
            <select v-model="filtroTipoCalificacion" class="form-select">
              <option value="">Todos</option>
              <option
                v-for="tipo in asignaturasStore.tiposCalificacionActivos"
                :key="tipo.id_tipo_calificacion"
                :value="tipo.id_tipo_calificacion"
              >
                {{ tipo.nombre }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div v-if="asignaturasStore.cargando" class="text-center py-5">
          <span class="spinner-border text-primary" role="status"></span>
          <p class="text-muted mt-2 mb-0">Cargando asignaturas...</p>
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>Código</th>
                <th>Asignatura</th>
                <th>Tipo calificación</th>
                <th>Escala</th>
                <th>Promedia</th>
                <th>Estado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="asignatura in asignaturasFiltradas" :key="asignatura.id_asignatura">
                <td>
                  <span class="badge text-bg-light border">
                    {{ asignatura.codigo || 'Sin código' }}
                  </span>
                </td>

                <td class="fw-semibold">
                  {{ asignatura.nombre }}
                </td>

                <td>
                  {{ getTipoCalificacion(asignatura)?.nombre || 'No definido' }}
                </td>

                <td>
                  {{ getTipoCalificacion(asignatura)?.escala || '-' }}
                </td>

                <td>
                  <span
                    class="badge"
                    :class="
                      getTipoCalificacion(asignatura)?.entra_promedio
                        ? 'text-bg-success'
                        : 'text-bg-secondary'
                    "
                  >
                    {{ getTipoCalificacion(asignatura)?.entra_promedio ? 'Sí' : 'No' }}
                  </span>
                </td>

                <td>
                  <span
                    class="badge"
                    :class="
                      asignatura.estado === 'Activa' ? 'text-bg-success' : 'text-bg-secondary'
                    "
                  >
                    {{ asignatura.estado }}
                  </span>
                </td>

                <td class="text-end">
                  <button
                    class="btn btn-outline-primary btn-sm btn-rounded me-2"
                    @click="abrirModalEditar(asignatura)"
                  >
                    <i class="bi bi-pencil-square me-1"></i>
                    Editar
                  </button>

                  <button
                    class="btn btn-outline-danger btn-sm btn-rounded"
                    @click="eliminarAsignatura(asignatura)"
                  >
                    <i class="bi bi-trash me-1"></i>
                    Eliminar
                  </button>
                </td>
              </tr>

              <tr v-if="asignaturasFiltradas.length === 0">
                <td colspan="7" class="text-center text-muted py-4">
                  No se encontraron asignaturas.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

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
              {{ modoEdicion ? 'Editar asignatura' : 'Nueva asignatura' }}
            </h5>

            <button type="button" class="btn-close" @click="cerrarModal"></button>
          </div>

          <form @submit.prevent="guardarAsignatura">
            <div class="modal-body">
              <div class="alert alert-light border">
                <i class="bi bi-info-circle me-2"></i>
                Las horas semanales y el tipo de enseñanza se configuran luego en el curso, dentro
                de la relación curso-asignatura.
              </div>

              <div class="row g-3">
                <div class="col-md-8">
                  <label class="form-label">Nombre asignatura</label>
                  <input
                    v-model="form.nombre"
                    type="text"
                    class="form-control"
                    placeholder="Ej: Matemática"
                    required
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Código</label>
                  <input
                    v-model="form.codigo"
                    type="text"
                    class="form-control"
                    placeholder="Ej: MAT"
                    required
                  />
                </div>

                <div class="col-md-8">
                  <label class="form-label">Tipo de calificación</label>
                  <select v-model="form.id_tipo_calificacion" class="form-select" required>
                    <option value="">Seleccionar tipo</option>
                    <option
                      v-for="tipo in asignaturasStore.tiposCalificacionActivos"
                      :key="tipo.id_tipo_calificacion"
                      :value="tipo.id_tipo_calificacion"
                    >
                      {{ tipo.nombre }} · {{ tipo.escala }}
                    </option>
                  </select>
                </div>

                <div class="col-md-4">
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select">
                    <option value="Activa">Activa</option>
                    <option value="Inactiva">Inactiva</option>
                  </select>
                </div>
              </div>

              <div v-if="tipoCalificacionSeleccionado" class="card border mt-4">
                <div class="card-body">
                  <h6 class="fw-bold mb-3">Detalle tipo de calificación</h6>

                  <div class="row g-3">
                    <div class="col-md-4">
                      <small class="text-muted d-block">Escala</small>
                      <strong>{{ tipoCalificacionSeleccionado.escala }}</strong>
                    </div>

                    <div class="col-md-4">
                      <small class="text-muted d-block">Entra al promedio</small>
                      <strong>
                        {{ tipoCalificacionSeleccionado.entra_promedio ? 'Sí' : 'No' }}
                      </strong>
                    </div>

                    <div class="col-md-4">
                      <small class="text-muted d-block">Mínimo aprobación</small>
                      <strong>
                        {{ tipoCalificacionSeleccionado.minimo_aprobacion || 'No aplica' }}
                      </strong>
                    </div>
                  </div>
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
import { useAsignaturasStore } from '@/stores/asignaturasStore'

const asignaturasStore = useAsignaturasStore()

const busqueda = ref('')
const filtroTipoCalificacion = ref('')
const modalAbierto = ref(false)
const modoEdicion = ref(false)
const asignaturaEditandoId = ref(null)
const guardando = ref(false)
const mensajeError = ref('')

const form = reactive({
  nombre: '',
  codigo: '',
  id_tipo_calificacion: '',
  estado: 'Activa',
})

const normalizarTexto = (texto) =>
  String(texto || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')

const asignaturasFiltradas = computed(() => {
  const query = normalizarTexto(busqueda.value)

  return asignaturasStore.asignaturasFiltradasNormalizadas.filter((asignatura) => {
    const coincideBusqueda =
      normalizarTexto(asignatura.nombre).includes(query) ||
      normalizarTexto(asignatura.codigo).includes(query)

    const coincideTipo =
      !filtroTipoCalificacion.value ||
      Number(asignatura.id_tipo_calificacion) === Number(filtroTipoCalificacion.value)

    return coincideBusqueda && coincideTipo
  })
})

const tipoCalificacionSeleccionado = computed(() => {
  if (!form.id_tipo_calificacion) return null

  return asignaturasStore.getTipoCalificacionById(form.id_tipo_calificacion)
})

const getTipoCalificacion = (asignatura) => {
  return asignaturasStore.getTipoCalificacionById(asignatura.id_tipo_calificacion)
}

const resetForm = () => {
  form.nombre = ''
  form.codigo = ''
  form.id_tipo_calificacion = ''
  form.estado = 'Activa'
  asignaturaEditandoId.value = null
}

const abrirModalCrear = () => {
  resetForm()
  modoEdicion.value = false
  modalAbierto.value = true
}

const abrirModalEditar = (asignatura) => {
  form.nombre = asignatura.nombre || ''
  form.codigo = asignatura.codigo || ''
  form.id_tipo_calificacion = asignatura.id_tipo_calificacion || ''
  form.estado = asignatura.estado || 'Activa'

  asignaturaEditandoId.value = asignatura.id_asignatura
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

const guardarAsignatura = async () => {
  const data = {
    nombre: form.nombre.trim(),
    codigo: form.codigo.trim().toUpperCase(),
    id_tipo_calificacion: Number(form.id_tipo_calificacion),
    estado: form.estado,
  }

  guardando.value = true
  mensajeError.value = ''

  try {
    if (modoEdicion.value && asignaturaEditandoId.value) {
      await asignaturasStore.actualizarAsignatura(asignaturaEditandoId.value, data)
    } else {
      await asignaturasStore.agregarAsignatura(data)
    }

    cerrarModal()
  } catch (error) {
    mensajeError.value = extraerMensajeError(error)
  } finally {
    guardando.value = false
  }
}

const eliminarAsignatura = async (asignatura) => {
  const confirmacion = window.confirm(
    `¿Seguro que deseas eliminar la asignatura "${asignatura.nombre}"?`,
  )

  if (!confirmacion) return

  mensajeError.value = ''

  try {
    await asignaturasStore.eliminarAsignatura(asignatura.id_asignatura)
  } catch (error) {
    mensajeError.value = extraerMensajeError(error)
  }
}

onMounted(() => {
  asignaturasStore.cargarDatos().catch(() => {
    mensajeError.value = 'No se pudieron cargar las asignaturas desde el servidor.'
  })
})
</script>
