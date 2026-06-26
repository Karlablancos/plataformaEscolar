<template>
  <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
    <div>
      <h1 class="fw-bold mb-1">Dashboard UTP</h1>
      <p class="text-muted mb-0">Resumen académico del año {{ anioActivo }}.</p>
    </div>

    <div class="d-flex align-items-center gap-2">
      <label class="form-label mb-0 fw-semibold">Año académico</label>
      <select v-model="anioActivo" class="form-select">
        <option :value="2025">2025</option>
        <option :value="2026">2026</option>
        <option :value="2027">2027</option>
      </select>
    </div>
  </div>

  <div class="row g-3 mb-4">
    <div class="col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <p class="text-muted mb-1">
            <i class="bi bi-people me-1"></i>
            Estudiantes
          </p>
          <h2 class="fw-bold mb-0">
            <span v-if="cargando" class="spinner-border spinner-border-sm text-secondary" />
            <span v-else>{{ totalAlumnos }}</span>
          </h2>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <p class="text-muted mb-1">
            <i class="bi bi-person-badge me-1"></i>
            Docentes
          </p>
          <h2 class="fw-bold mb-0">
            <span v-if="cargando" class="spinner-border spinner-border-sm text-secondary" />
            <span v-else>{{ totalProfesores }}</span>
          </h2>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <p class="text-muted mb-1">
            <i class="bi bi-easel me-1"></i>
            Cursos
          </p>
          <h2 class="fw-bold mb-0">
            <span v-if="cargando" class="spinner-border spinner-border-sm text-secondary" />
            <span v-else>{{ totalCursos }}</span>
          </h2>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <p class="text-muted mb-1">
            <i class="bi bi-journal-bookmark me-1"></i>
            Asignaturas
          </p>
          <h2 class="fw-bold mb-0">
            <span v-if="cargando" class="spinner-border spinner-border-sm text-secondary" />
            <span v-else>{{ totalAsignaturas }}</span>
          </h2>
        </div>
      </div>
    </div>
  </div>

  <div class="card shadow-sm border-0 mb-4">
    <div class="card-body">
      <h2 class="h5 fw-bold mb-3">Pendientes de configuración</h2>

      <div class="row g-3">
        <div class="col-md-4">
          <div
            class="alert mb-0"
            :class="cursosSinProfesorJefe.length ? 'alert-warning' : 'alert-success'"
          >
            <strong>{{ cursosSinProfesorJefe.length }}</strong>
            cursos sin profesor jefe
          </div>
        </div>

        <div class="col-md-4">
          <div
            class="alert mb-0"
            :class="cursosSinAsignaturas.length ? 'alert-warning' : 'alert-success'"
          >
            <strong>{{ cursosSinAsignaturas.length }}</strong>
            cursos sin asignaturas
          </div>
        </div>

        <div class="col-md-4">
          <div
            class="alert mb-0"
            :class="cursosSinAlumnos.length ? 'alert-warning' : 'alert-success'"
          >
            <strong>{{ cursosSinAlumnos.length }}</strong>
            cursos sin estudiantes
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="card shadow-sm border-0 mb-4">
    <div class="card-body">
      <h2 class="h5 fw-bold mb-3">Acciones rápidas</h2>

      <div class="d-flex flex-wrap gap-2">
        <RouterLink to="/admin/establecimiento" class="btn btn-outline-primary btn-rounded">
          <i class="bi bi-building me-1"></i>
          Establecimiento
        </RouterLink>

        <RouterLink to="/admin/alumnos" class="btn btn-outline-primary btn-rounded">
          <i class="bi bi-people me-1"></i>
          Gestionar estudiantes
        </RouterLink>

        <RouterLink to="/admin/profesores" class="btn btn-outline-primary btn-rounded">
          <i class="bi bi-person-badge me-1"></i>
          Gestionar docentes
        </RouterLink>

        <RouterLink to="/admin/asignaturas" class="btn btn-outline-primary btn-rounded">
          <i class="bi bi-journal-bookmark me-1"></i>
          Gestionar asignaturas
        </RouterLink>

        <RouterLink to="/admin/cursos" class="btn btn-primary btn-rounded">
          <i class="bi bi-easel me-1"></i>
          Configurar cursos
        </RouterLink>
      </div>
    </div>
  </div>

  <div class="card shadow-sm border-0">
    <div class="card-body">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-bold mb-0">Cursos del año {{ anioActivo }}</h2>
        <span class="badge text-bg-primary">{{ cursos.length }} cursos</span>
      </div>

      <div class="table-responsive">
        <table class="table table-hover align-middle">
          <thead class="table-light">
            <tr>
              <th>Curso</th>
              <th>Tipo enseñanza</th>
              <th>Modalidad</th>
              <th>Profesor jefe</th>
              <th>Estudiantes</th>
              <th>Asignaturas</th>
              <th class="text-end">Acción</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="curso in cursos" :key="curso.id_curso">
              <td class="fw-semibold">{{ curso.nombre }}</td>
              <td>{{ curso.tipo_ensenanza || 'No definido' }}</td>
              <td>{{ curso.modalidad || 'No definida' }}</td>
              <td>{{ getNombreProfesor(curso.profesorJefeId) }}</td>
              <td>{{ getTotalAlumnosCurso(curso) }}</td>
              <td>{{ getTotalAsignaturasCurso(curso) }}</td>
              <td class="text-end">
                <RouterLink
                  class="btn btn-sm btn-primary btn-rounded"
                  :to="`/admin/cursos/${curso.id_curso}`"
                >
                  Configurar
                </RouterLink>
              </td>
            </tr>

            <tr v-if="cursos.length === 0">
              <td colspan="7" class="text-center text-muted py-4">
                No hay cursos para el año {{ anioActivo }}.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import { useAuthStore } from '@/stores/authStore'
import api from '@/api/axios'

const academic = useAcademicStore()
const authStore = useAuthStore()

// Estado de carga de la API
const cargando = ref(false)
const apiTotales = ref({ alumnos: null, docentes: null, cursos: null, asignaturas: null })

const anioActivo = computed({
  get: () => academic.anioActivo,
  set: (value) => academic.cambiarAnioActivo(value),
})

// Datos del localStorage (fallback)
const alumnos = computed(() => academic.alumnosFiltradosNormalizados)
const profesores = computed(() => academic.docentesFiltradosNormalizados)
const cursos = computed(() => academic.cursosFiltradosNormalizados)
const asignaturas = computed(() => academic.asignaturasFiltradasNormalizadas)

const relacionesAsignaturaCurso = computed(() => {
  return academic.docenteAsignaturaCurso.filter((item) => {
    const anioRelacion = item.anioAcademico ?? item.anio_academico
    return Number(anioRelacion) === Number(anioActivo.value)
  })
})

// Totales: usa API si cargó, sino localStorage como fallback
const totalAlumnos = computed(() => apiTotales.value.alumnos ?? alumnos.value.length)
const totalProfesores = computed(() => apiTotales.value.docentes ?? profesores.value.length)
const totalCursos = computed(() => apiTotales.value.cursos ?? cursos.value.length)
const totalAsignaturas = computed(() => apiTotales.value.asignaturas ?? asignaturas.value.length)

onMounted(async () => {
  const idEstablecimiento = authStore.establecimientoId
    ?? JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento
  if (!idEstablecimiento) return

  cargando.value = true

  const [estudiantesRes, docentesRes, cursosRes, asignaturasRes] = await Promise.allSettled([
    api.get(`/establecimiento/${idEstablecimiento}/estudiantes`),
    api.get(`/usuarios?idEstablecimiento=${idEstablecimiento}`),
    api.get(`/establecimiento/${idEstablecimiento}/cursos`),
    api.get(`/establecimiento/${idEstablecimiento}/asignaturas`),
  ])

  if (estudiantesRes.status === 'fulfilled') {
    apiTotales.value.alumnos = estudiantesRes.value.data.length
  }

  if (docentesRes.status === 'fulfilled') {
    apiTotales.value.docentes = docentesRes.value.data.filter((u) => u.idRol === 2).length
  }

  if (cursosRes.status === 'fulfilled') {
    apiTotales.value.cursos = cursosRes.value.data.length
  }

  if (asignaturasRes.status === 'fulfilled') {
    apiTotales.value.asignaturas = asignaturasRes.value.data.length
  }

  cargando.value = false
})

const getCursoId = (curso) => curso.id_curso ?? curso.id

const getDocenteId = (docente) => docente.id_docente ?? docente.id

const getNombreDocente = (docente) => {
  if (!docente) return 'Sin asignar'

  if (docente.nombreCompleto) return docente.nombreCompleto
  if (docente.nombre) return docente.nombre

  return [docente.nombres, docente.apellido_paterno, docente.apellido_materno]
    .filter(Boolean)
    .join(' ')
}

const getNombreProfesor = (profesorId) => {
  if (!profesorId) return 'Sin asignar'

  const profesor = profesores.value.find((item) => getDocenteId(item) === Number(profesorId))

  return getNombreDocente(profesor)
}

const getTotalAlumnosCurso = (curso) => {
  const cursoId = getCursoId(curso)

  const idsDesdeCurso = curso.alumnos?.length || 0

  const alumnosPorCursoId = alumnos.value.filter((alumno) => {
    return alumno.cursoId === cursoId || alumno.id_curso === cursoId
  }).length

  return Math.max(idsDesdeCurso, alumnosPorCursoId)
}

const getTotalAsignaturasCurso = (curso) => {
  const cursoId = getCursoId(curso)

  return relacionesAsignaturaCurso.value.filter((item) => {
    return item.cursoId === cursoId || item.id_curso === cursoId
  }).length
}

const cursosSinProfesorJefe = computed(() => {
  return cursos.value.filter((curso) => !curso.profesorJefeId)
})

const cursosSinAsignaturas = computed(() => {
  return cursos.value.filter((curso) => getTotalAsignaturasCurso(curso) === 0)
})

const cursosSinAlumnos = computed(() => {
  return cursos.value.filter((curso) => getTotalAlumnosCurso(curso) === 0)
})
</script>
