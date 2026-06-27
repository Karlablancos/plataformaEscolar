<template>
  <div class="container-fluid py-4">

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <div>
        <h1 class="h3 fw-bold mb-1">
          <i class="bi bi-calendar-check me-2" style="color:#1B4F9C"></i>
          Asistencia Diaria
        </h1>
        <p class="text-muted mb-0">Registro de asistencia por curso y fecha.</p>
      </div>
    </div>

    <div v-if="mensajeExito" class="alert alert-success alert-dismissible fade show">
      {{ mensajeExito }}
      <button type="button" class="btn-close" @click="mensajeExito = ''"></button>
    </div>
    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <!-- Filtros -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-12 col-md-5">
            <label class="form-label fw-semibold">Curso</label>
            <select v-model.number="idCurso" class="form-select" @change="onCursoChange">
              <option value="">Seleccionar curso</option>
              <option v-for="c in cursos" :key="c.id_curso" :value="c.id_curso">
                {{ c.nombre || `${c.numero}° ${c.letra}` }}
              </option>
            </select>
          </div>
          <div class="col-12 col-md-4">
            <label class="form-label fw-semibold">Fecha</label>
            <input v-model="fecha" type="date" class="form-control" @change="onFechaChange" />
          </div>
          <div class="col-12 col-md-3 d-flex gap-2">
            <button
              class="btn btn-outline-success btn-sm rounded-pill flex-grow-1"
              :disabled="!registros.length"
              @click="marcarTodos('PRESENTE')"
            >
              <i class="bi bi-check-all me-1"></i>Todos presentes
            </button>
            <button
              class="btn btn-outline-danger btn-sm rounded-pill flex-grow-1"
              :disabled="!registros.length"
              @click="marcarTodos('AUSENTE')"
            >
              <i class="bi bi-x-lg me-1"></i>Todos ausentes
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Estado vacío -->
    <div v-if="!idCurso || !fecha" class="text-center text-muted py-5">
      <i class="bi bi-person-check fs-1 d-block mb-3" style="color:#1B4F9C"></i>
      Selecciona un curso y una fecha para registrar asistencia.
    </div>

    <template v-else>
      <div v-if="cargandoEstudiantes" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="text-muted mt-3 mb-0">Cargando estudiantes...</p>
      </div>

      <div v-else-if="registros.length === 0" class="text-center text-muted py-5">
        <i class="bi bi-people fs-1 d-block mb-3"></i>
        Este curso no tiene estudiantes matriculados.
      </div>

      <template v-else>
        <!-- Resumen -->
        <div class="row g-3 mb-4">
          <div class="col-6 col-md-3">
            <div class="card border-0 shadow-sm rounded-4 text-center py-3 px-2">
              <div class="fs-2 fw-bold" style="color:#1B4F9C">{{ registros.length }}</div>
              <div class="text-muted small">Total</div>
            </div>
          </div>
          <div class="col-6 col-md-3">
            <div class="card border-0 shadow-sm rounded-4 text-center py-3 px-2">
              <div class="fs-2 fw-bold text-success">{{ countPresentes }}</div>
              <div class="text-muted small">Presentes</div>
            </div>
          </div>
          <div class="col-6 col-md-3">
            <div class="card border-0 shadow-sm rounded-4 text-center py-3 px-2">
              <div class="fs-2 fw-bold" style="color:#CC1F2D">{{ countAusentes }}</div>
              <div class="text-muted small">Ausentes</div>
            </div>
          </div>
          <div class="col-6 col-md-3">
            <div class="card border-0 shadow-sm rounded-4 text-center py-3 px-2">
              <div class="fs-2 fw-bold" style="color:#F5C518">{{ countSinMarcar }}</div>
              <div class="text-muted small">Sin marcar</div>
            </div>
          </div>
        </div>

        <!-- Tabla -->
        <div class="card border-0 shadow-sm rounded-4">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
              <h2 class="h5 fw-bold mb-0">
                <i class="bi bi-people me-2" style="color:#1B4F9C"></i>
                Estudiantes
              </h2>
              <button
                class="btn btn-primary rounded-pill px-4"
                :disabled="guardando || countSinMarcar > 0"
                @click="guardarAsistencia"
              >
                <span v-if="guardando" class="spinner-border spinner-border-sm me-2"></span>
                <i v-else class="bi bi-floppy me-2"></i>
                Guardar asistencia
              </button>
            </div>

            <div v-if="countSinMarcar > 0" class="alert alert-warning py-2 mb-3">
              <i class="bi bi-exclamation-triangle me-2"></i>
              {{ countSinMarcar }} estudiante(s) sin marcar. Marca a todos antes de guardar.
            </div>

            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th style="width:40px">#</th>
                    <th>Nombre</th>
                    <th class="text-center" style="width:240px">Estado</th>
                    <th>Observación</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(reg, i) in registros" :key="reg.idEstudiante">
                    <td class="text-muted">{{ i + 1 }}</td>
                    <td>
                      <strong>{{ reg.nombre }}</strong>
                    </td>
                    <td class="text-center">
                      <div class="btn-group btn-group-sm" role="group">
                        <button
                          type="button"
                          class="btn"
                          :class="reg.estado === 'PRESENTE' ? 'btn-success' : 'btn-outline-success'"
                          @click="reg.estado = 'PRESENTE'"
                        >
                          <i class="bi bi-check-lg me-1"></i>Presente
                        </button>
                        <button
                          type="button"
                          class="btn"
                          :class="reg.estado === 'AUSENTE' ? 'btn-danger' : 'btn-outline-danger'"
                          @click="reg.estado = 'AUSENTE'"
                        >
                          <i class="bi bi-x-lg me-1"></i>Ausente
                        </button>
                      </div>
                    </td>
                    <td>
                      <input
                        v-model="reg.observacion"
                        type="text"
                        class="form-control form-control-sm"
                        placeholder="Opcional"
                        style="max-width:200px"
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </template>
    </template>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useCursosStore } from '@/stores/cursosStore'
import { useAsistenciaStore } from '@/stores/asistenciaStore'
import { getNombreCompleto } from '@/stores/shared/helpers'

const cursosStore = useCursosStore()
const asistenciaStore = useAsistenciaStore()

const idCurso = ref('')
const fecha = ref(new Date().toISOString().slice(0, 10))
const registros = ref([])
const cargandoEstudiantes = ref(false)
const guardando = ref(false)
const mensajeExito = ref('')
const mensajeError = ref('')

const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)

const countPresentes = computed(() => registros.value.filter((r) => r.estado === 'PRESENTE').length)
const countAusentes = computed(() => registros.value.filter((r) => r.estado === 'AUSENTE').length)
const countSinMarcar = computed(() => registros.value.filter((r) => !r.estado).length)

const cargarEstudiantes = async () => {
  if (!idCurso.value || !fecha.value) return

  cargandoEstudiantes.value = true
  mensajeExito.value = ''
  mensajeError.value = ''
  registros.value = []

  try {
    const estudiantes = await cursosStore.sincronizarAlumnosCurso(idCurso.value)
    registros.value = estudiantes.map((est) => ({
      idEstudiante: est.id_alumno ?? est.id,
      nombre: getNombreCompleto(est),
      estado: null,
      observacion: '',
    }))
  } catch {
    mensajeError.value = 'No se pudieron cargar los estudiantes del curso.'
  } finally {
    cargandoEstudiantes.value = false
  }
}

const onCursoChange = () => cargarEstudiantes()
const onFechaChange = () => cargarEstudiantes()

const marcarTodos = (estado) => {
  registros.value.forEach((r) => (r.estado = estado))
}

const guardarAsistencia = async () => {
  if (countSinMarcar.value > 0) return

  guardando.value = true
  mensajeExito.value = ''
  mensajeError.value = ''

  try {
    await Promise.all(
      registros.value.map((reg) =>
        asistenciaStore.registrarAsistencia({
          idEstudiante: reg.idEstudiante,
          fecha: fecha.value,
          estadoAsistencia: reg.estado,
          observacion: reg.observacion || null,
          justificada: false,
        }),
      ),
    )
    mensajeExito.value = `Asistencia guardada correctamente para ${registros.value.length} estudiante(s).`
  } catch {
    mensajeError.value = 'Ocurrió un error al guardar la asistencia. Intenta nuevamente.'
  } finally {
    guardando.value = false
  }
}

onMounted(() => {
  cursosStore.cargarCursos().catch(() => {})
})
</script>
