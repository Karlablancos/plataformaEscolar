<template>
  <div class="container-fluid py-4">

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <div>
        <h1 class="h3 fw-bold mb-1">
          <i class="bi bi-table me-2" style="color:#1B4F9C"></i>
          Libro de Notas
        </h1>
        <p class="text-muted mb-0">Ingresa y consulta las notas por curso y asignatura.</p>
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
          <div class="col-12 col-md-4">
            <label class="form-label fw-semibold">Curso</label>
            <select v-model.number="idCurso" class="form-select" @change="onCursoChange">
              <option value="">Seleccionar curso</option>
              <option v-for="c in cursos" :key="c.id_curso" :value="c.id_curso">
                {{ c.nombre || `${c.numero}° ${c.letra}` }}
              </option>
            </select>
          </div>
          <div class="col-12 col-md-4">
            <label class="form-label fw-semibold">Asignatura</label>
            <select v-model.number="idAsignatura" class="form-select" @change="onAsignaturaChange">
              <option value="">Seleccionar asignatura</option>
              <option v-for="a in asignaturas" :key="a.id_asignatura" :value="a.id_asignatura">
                {{ a.nombre }}
              </option>
            </select>
          </div>
          <div class="col-12 col-md-4">
            <div class="d-flex gap-2">
              <span v-if="cargando" class="spinner-border spinner-border-sm text-primary align-self-center" />
              <span v-if="evaluacionesFiltradas.length" class="badge rounded-pill text-bg-primary align-self-center">
                {{ evaluacionesFiltradas.length }} evaluaciones
              </span>
              <span v-if="alumnos.length" class="badge rounded-pill text-bg-secondary align-self-center">
                {{ alumnos.length }} alumnos
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Grilla de notas -->
    <div class="card border-0 shadow-sm rounded-4">
      <div class="card-body">
        <div v-if="!idCurso || !idAsignatura" class="text-center text-muted py-5">
          <i class="bi bi-filter-circle fs-2 d-block mb-2"></i>
          Selecciona un curso y una asignatura para ver el libro de notas.
        </div>

        <div v-else-if="evaluacionesFiltradas.length === 0 && !cargando"
          class="text-center text-muted py-5">
          <i class="bi bi-clipboard-x fs-2 d-block mb-2"></i>
          No hay evaluaciones para este curso y asignatura.
          <div class="mt-2">
            <RouterLink to="/academico/evaluaciones" class="btn btn-sm btn-outline-primary rounded-pill">
              <i class="bi bi-plus me-1"></i>Crear evaluación
            </RouterLink>
          </div>
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th style="min-width:160px">Alumno</th>
                <th v-for="ev in evaluacionesFiltradas" :key="ev.id"
                  class="text-center" style="min-width:110px">
                  <div class="small fw-semibold">{{ ev.nombre }}</div>
                  <div class="text-muted" style="font-size:0.72rem">
                    {{ ev.tipo }} · {{ ev.ponderacion }}%
                  </div>
                </th>
                <th class="text-center" style="min-width:90px">
                  Promedio
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="alumno in alumnos" :key="alumno.id_alumno">
                <td>
                  <div class="fw-semibold">{{ alumno.nombreCompleto }}</div>
                  <div class="text-muted small">{{ alumno.rut }}</div>
                </td>
                <td v-for="ev in evaluacionesFiltradas" :key="ev.id" class="text-center p-1">
                  <div class="position-relative">
                    <input
                      type="number"
                      class="form-control form-control-sm text-center px-1"
                      :class="classCelda(alumno.id_alumno, ev.id)"
                      v-model.number="notasGrid[alumno.id_alumno][ev.id]"
                      min="1.0" max="7.0" step="0.1"
                      placeholder="—"
                      style="width:70px; margin:0 auto;"
                      @blur="guardarNota(alumno.id_alumno, ev.id)"
                    />
                    <span v-if="celdaGuardando(alumno.id_alumno, ev.id)"
                      class="position-absolute top-0 end-0"
                      style="font-size:0.6rem; color:#1B4F9C">
                      <i class="bi bi-arrow-repeat spin"></i>
                    </span>
                  </div>
                </td>
                <td class="text-center fw-bold">
                  <span :class="clasePromedio(promedios[alumno.id_alumno])">
                    {{ promedios[alumno.id_alumno] ?? '—' }}
                  </span>
                </td>
              </tr>
              <tr v-if="alumnos.length === 0">
                <td :colspan="evaluacionesFiltradas.length + 2"
                  class="text-center text-muted py-4">
                  No hay alumnos registrados para este establecimiento.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
.spin {
  animation: spin 0.8s linear infinite;
  display: inline-block;
}
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useCursosStore } from '@/stores/cursosStore'
import { useAsignaturasStore } from '@/stores/asignaturasStore'
import { useAlumnosStore } from '@/stores/alumnosStore'
import api from '@/api/axios'

const authStore = useAuthStore()
const cursosStore = useCursosStore()
const asignaturasStore = useAsignaturasStore()
const alumnosStore = useAlumnosStore()

const idCurso = ref('')
const idAsignatura = ref('')
const cargando = ref(false)
const mensajeExito = ref('')
const mensajeError = ref('')

const evaluaciones = ref([])
const notasGrid = reactive({})
const promedios = reactive({})
const celdaEnGuardado = reactive({})

const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)
const asignaturas = computed(() => asignaturasStore.asignaturasFiltradasNormalizadas)
const alumnos = computed(() => alumnosStore.alumnosFiltradosNormalizados)

const evaluacionesFiltradas = computed(() =>
  evaluaciones.value.filter((ev) => ev.idAsignatura === idAsignatura.value)
)

const claveGuardado = (idAlumno, idEv) => `${idAlumno}-${idEv}`

const celdaGuardando = (idAlumno, idEv) => !!celdaEnGuardado[claveGuardado(idAlumno, idEv)]

const classCelda = (idAlumno, idEv) => {
  const v = notasGrid[idAlumno]?.[idEv]
  if (v == null || v === '') return ''
  if (v < 1 || v > 7) return 'is-invalid'
  if (v < 4) return 'border-danger text-danger'
  return 'border-success text-success'
}

const clasePromedio = (prom) => {
  if (prom == null) return 'text-muted'
  return parseFloat(prom) >= 4 ? 'text-success' : 'text-danger'
}

const inicializarGrid = () => {
  for (const alumno of alumnos.value) {
    const id = alumno.id_alumno
    if (!notasGrid[id]) notasGrid[id] = {}
    promedios[id] = null
  }
}

const calcularPromedioLocal = (idAlumno) => {
  let sumaPonderada = 0
  let sumaPonderaciones = 0
  for (const ev of evaluacionesFiltradas.value) {
    const val = notasGrid[idAlumno]?.[ev.id]
    if (val != null && val !== '' && !isNaN(val)) {
      const pond = parseFloat(ev.ponderacion)
      sumaPonderada += parseFloat(val) * pond
      sumaPonderaciones += pond
    }
  }
  if (sumaPonderaciones === 0) return null
  return (sumaPonderada / sumaPonderaciones).toFixed(1)
}

const guardarNota = async (idAlumno, idEvaluacion) => {
  const val = notasGrid[idAlumno]?.[idEvaluacion]
  if (val == null || val === '') return
  const calificacion = parseFloat(val)
  if (isNaN(calificacion) || calificacion < 1 || calificacion > 7) return

  const clave = claveGuardado(idAlumno, idEvaluacion)
  celdaEnGuardado[clave] = true
  try {
    await api.post('/notas', {
      calificacion,
      idEvaluacion,
      idEstudiante: idAlumno,
      idEstablecimiento: authStore.establecimientoId,
    })
    promedios[idAlumno] = calcularPromedioLocal(idAlumno)
    mensajeExito.value = 'Nota guardada.'
    setTimeout(() => { mensajeExito.value = '' }, 2000)
  } catch (e) {
    mensajeError.value = e?.response?.data?.error || 'Error al guardar la nota.'
  } finally {
    celdaEnGuardado[clave] = false
  }
}

const onCursoChange = async () => {
  idAsignatura.value = ''
  evaluaciones.value = []
  if (!idCurso.value) return
  cargando.value = true
  try {
    const { data } = await api.get(`/evaluacion/curso/${idCurso.value}`)
    evaluaciones.value = data
  } catch {
    mensajeError.value = 'No se pudieron cargar las evaluaciones.'
  } finally {
    cargando.value = false
  }
}

const onAsignaturaChange = () => {
  inicializarGrid()
}

onMounted(async () => {
  await alumnosStore.cargarAlumnos()
  inicializarGrid()
})
</script>
