<template>
  <div class="container-fluid py-4">

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <div>
        <h1 class="h3 fw-bold mb-1">
          <i class="bi bi-journal-check me-2" style="color:#1B4F9C"></i>
          Evaluaciones
        </h1>
        <p class="text-muted mb-0">Crea y administra las evaluaciones por curso.</p>
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

    <!-- Formulario -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
      <div class="card-body">
        <h2 class="h5 fw-bold mb-3">
          <i class="bi bi-plus-circle me-2" style="color:#1B4F9C"></i>
          Nueva evaluación
        </h2>
        <form @submit.prevent="crearEvaluacion">
          <div class="row g-3">
            <div class="col-12 col-md-4">
              <label class="form-label fw-semibold">Nombre</label>
              <input v-model.trim="form.nombre" type="text" class="form-control"
                placeholder="Ej: Prueba Unidad 1" required />
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">Tipo</label>
              <select v-model="form.tipo" class="form-select" required>
                <option value="PRUEBA">Prueba</option>
                <option value="CONTROL">Control</option>
                <option value="EXAMEN">Examen</option>
                <option value="TRABAJO">Trabajo</option>
              </select>
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">Ponderación (%)</label>
              <input v-model.number="form.ponderacion" type="number" class="form-control"
                placeholder="60" min="1" max="100" step="1" required />
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">Fecha</label>
              <input v-model="form.fecha" type="date" class="form-control" required />
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">Curso</label>
              <select v-model.number="form.idCurso" class="form-select" required
                @change="sincronizarCurso">
                <option value="">Seleccionar</option>
                <option v-for="c in cursos" :key="c.id_curso" :value="c.id_curso">
                  {{ c.nombre || `${c.numero}° ${c.letra}` }}
                </option>
              </select>
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">Asignatura</label>
              <select v-model.number="form.idAsignatura" class="form-select" required>
                <option value="">Seleccionar</option>
                <option v-for="a in asignaturas" :key="a.id_asignatura" :value="a.id_asignatura">
                  {{ a.nombre }}
                </option>
              </select>
            </div>
            <div class="col-6 col-md-2">
              <label class="form-label fw-semibold">ID Docente</label>
              <input v-model.number="form.idDocente" type="number" class="form-control"
                placeholder="ID del docente" required />
            </div>
            <div class="col-12 d-flex justify-content-end">
              <button type="submit" class="btn btn-primary rounded-pill px-4"
                :disabled="guardando">
                <span v-if="guardando" class="spinner-border spinner-border-sm me-2"></span>
                <i v-else class="bi bi-check-lg me-2"></i>
                Crear evaluación
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- Tabla de evaluaciones -->
    <div class="card border-0 shadow-sm rounded-4">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
          <h2 class="h5 fw-bold mb-0">
            <i class="bi bi-list-check me-2" style="color:#1B4F9C"></i>
            Evaluaciones del curso
          </h2>
          <div class="d-flex align-items-center gap-2">
            <select v-model.number="idCursoFiltro" class="form-select form-select-sm"
              style="width:180px" @change="cargarEvaluaciones">
              <option value="">Seleccionar curso</option>
              <option v-for="c in cursos" :key="c.id_curso" :value="c.id_curso">
                {{ c.nombre || `${c.numero}° ${c.letra}` }}
              </option>
            </select>
            <span v-if="cargando" class="spinner-border spinner-border-sm text-primary" />
            <span class="badge rounded-pill text-bg-primary">
              {{ evaluaciones.length }} registros
            </span>
          </div>
        </div>

        <div v-if="!idCursoFiltro" class="text-center text-muted py-5">
          <i class="bi bi-search fs-2 d-block mb-2"></i>
          Selecciona un curso para ver sus evaluaciones.
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>#</th>
                <th>Nombre</th>
                <th>Tipo</th>
                <th>Ponderación</th>
                <th>Fecha</th>
                <th>Asignatura ID</th>
                <th>Docente ID</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(ev, i) in evaluaciones" :key="ev.id">
                <td>{{ i + 1 }}</td>
                <td><strong>{{ ev.nombre }}</strong></td>
                <td>
                  <span class="badge rounded-pill"
                    :class="badgeTipo(ev.tipo)">
                    {{ ev.tipo }}
                  </span>
                </td>
                <td>{{ ev.ponderacion }}%</td>
                <td>{{ ev.fecha }}</td>
                <td>{{ ev.idAsignatura }}</td>
                <td>{{ ev.idDocente }}</td>
              </tr>
              <tr v-if="evaluaciones.length === 0 && !cargando">
                <td colspan="7" class="text-center text-muted py-4">
                  No hay evaluaciones para este curso.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { useCursosStore } from '@/stores/cursosStore'
import { useAsignaturasStore } from '@/stores/asignaturasStore'
import api from '@/api/axios'

const authStore = useAuthStore()
const cursosStore = useCursosStore()
const asignaturasStore = useAsignaturasStore()

const evaluaciones = ref([])
const cargando = ref(false)
const guardando = ref(false)
const mensajeExito = ref('')
const mensajeError = ref('')
const idCursoFiltro = ref('')

const form = reactive({
  nombre: '',
  tipo: 'PRUEBA',
  ponderacion: null,
  fecha: '',
  idCurso: null,
  idAsignatura: null,
  idDocente: null,
  idEstablecimiento: authStore.establecimientoId,
})

const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)
const asignaturas = computed(() => asignaturasStore.asignaturasFiltradasNormalizadas)

const cargarEvaluaciones = async () => {
  if (!idCursoFiltro.value) { evaluaciones.value = []; return }
  cargando.value = true
  try {
    const { data } = await api.get(`/evaluacion/curso/${idCursoFiltro.value}`)
    evaluaciones.value = data
  } catch {
    mensajeError.value = 'No se pudieron cargar las evaluaciones.'
  } finally {
    cargando.value = false
  }
}

const sincronizarCurso = () => {
  idCursoFiltro.value = form.idCurso
  cargarEvaluaciones()
}

const crearEvaluacion = async () => {
  guardando.value = true
  mensajeExito.value = ''
  mensajeError.value = ''
  try {
    form.idEstablecimiento = authStore.establecimientoId
    await api.post('/evaluacion', { ...form })
    mensajeExito.value = 'Evaluación creada correctamente.'
    Object.assign(form, {
      nombre: '', tipo: 'PRUEBA', ponderacion: null,
      fecha: '', idAsignatura: null, idDocente: null,
    })
    await cargarEvaluaciones()
  } catch (e) {
    mensajeError.value = e?.response?.data?.error || 'Error al crear la evaluación.'
  } finally {
    guardando.value = false
  }
}

const badgeTipo = (tipo) => ({
  PRUEBA: 'text-bg-primary',
  CONTROL: 'text-bg-info',
  EXAMEN: 'text-bg-danger',
  TRABAJO: 'text-bg-warning',
}[tipo] || 'text-bg-secondary')

onMounted(() => {
  asignaturasStore.cargarDatos().catch(() => {})
})
</script>
