<script setup>
import { computed, onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import { useCursosStore } from '@/stores/cursosStore'

const route = useRoute()
const academic = useAcademicStore()
const cursosStore = useCursosStore()

onMounted(() => {
  cursosStore.cargarCursos().catch(() => {})
})

const tabActiva = ref('estudiantes')

const mostrarBuscadorEstudiante = ref(false)
const busquedaAlumno = ref('')

const profesorJefeId = ref('')
const asignaturaSeleccionadaId = ref('')
const docenteSeleccionadoId = ref('')

const cursoId = computed(() => Number(route.params.id))
const curso = computed(() => academic.getCursoById(cursoId.value))

const nombreCurso = computed(() => {
  if (!curso.value) return ''
  return academic.getCursoNombre(curso.value)
})

const nombreAlumno = (alumno) => {
  return (
    alumno.nombre ||
    `${alumno.nombres} ${alumno.apellidoPaterno} ${alumno.apellidoMaterno || ''}`
      .replace(/\s+/g, ' ')
      .trim()
  )
}

const normalizarTexto = (texto) =>
  String(texto || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[.\-\s]/g, '')

const alumnosDelCurso = computed(() => {
  if (!curso.value) return []

  return academic.alumnos.filter(
    (alumno) => alumno.cursoId === cursoId.value || (curso.value.alumnos || []).includes(alumno.id),
  )
})

const alumnosDisponibles = computed(() => {
  return academic.alumnosFiltrados.filter(
    (alumno) => !alumno.cursoId || alumno.cursoId !== cursoId.value,
  )
})

const alumnosDisponiblesFiltrados = computed(() => {
  const query = normalizarTexto(busquedaAlumno.value)

  if (query.length < 2) return []

  return alumnosDisponibles.value
    .filter((alumno) => {
      const nombre = normalizarTexto(nombreAlumno(alumno))
      const rut = normalizarTexto(alumno.rut)

      return nombre.includes(query) || rut.includes(query)
    })
    .slice(0, 8)
})

const profesorJefe = computed(() => {
  if (!curso.value?.profesorJefeId) return null
  return academic.getProfesorById(curso.value.profesorJefeId)
})

const asignaturasCurso = computed(() => {
  return academic.docenteAsignaturaCurso
    .filter((item) => item.cursoId === cursoId.value && item.anioAcademico === academic.anioActivo)
    .map((item) => {
      const asignatura = academic.getAsignaturaById(item.asignaturaId)
      const docente = item.docenteId ? academic.getProfesorById(item.docenteId) : null

      return {
        ...item,
        asignaturaNombre: asignatura?.nombre || 'Asignatura no encontrada',
        docenteNombre: docente
          ? `${docente.nombres} ${docente.apellido_paterno}`.trim()
          : 'Sin docente',
      }
    })
})

const abrirBuscadorEstudiante = () => {
  mostrarBuscadorEstudiante.value = !mostrarBuscadorEstudiante.value
  busquedaAlumno.value = ''
}

const agregarAlumnoDirecto = (alumnoId) => {
  academic.asignarAlumnoACurso(cursoId.value, alumnoId)
  busquedaAlumno.value = ''
  mostrarBuscadorEstudiante.value = false
}

const quitarAlumno = (alumnoId) => {
  academic.quitarAlumnoDeCurso(cursoId.value, alumnoId)
}

const guardarProfesorJefe = () => {
  academic.asignarProfesorJefe(cursoId.value, profesorJefeId.value || null)
}

const agregarAsignatura = () => {
  if (!asignaturaSeleccionadaId.value) return

  academic.agregarAsignaturaACurso(
    cursoId.value,
    asignaturaSeleccionadaId.value,
    docenteSeleccionadoId.value || null,
  )

  asignaturaSeleccionadaId.value = ''
  docenteSeleccionadoId.value = ''
}

const quitarAsignatura = (asignaturaId) => {
  academic.quitarAsignaturaDeCurso(cursoId.value, asignaturaId)
}
</script>

<template>
  <div v-if="curso">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h1 class="h3 mb-1">Configurar curso {{ nombreCurso }}</h1>
        <p class="text-muted mb-0">
          Gestión académica del curso para el año {{ academic.anioActivo }}.
        </p>
      </div>

      <RouterLink to="/admin/cursos" class="btn btn-outline-secondary btn-rounded">
        ← Volver
      </RouterLink>
    </div>

    <ul class="nav nav-tabs mb-4">
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tabActiva === 'estudiantes' }"
          @click="tabActiva = 'estudiantes'"
        >
          <i class="bi bi-people"></i>
          Estudiantes
        </button>
      </li>

      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tabActiva === 'profesor' }"
          @click="tabActiva = 'profesor'"
        >
          <i class="bi bi-person-workspace"></i>
          Profesor jefe
        </button>
      </li>

      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tabActiva === 'asignaturas' }"
          @click="tabActiva = 'asignaturas'"
        >
          <i class="bi bi-journal-bookmark"></i>
          Asignaturas del curso
        </button>
      </li>
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tabActiva === 'planificacion' }"
          @click="tabActiva = 'planificacion'"
        >
          <i class="bi bi-calendar-week me-2"></i>
          Planificación académica
        </button>
      </li>
    </ul>

    <!-- Estudiantes -->
    <div v-if="tabActiva === 'estudiantes'" class="card">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2 class="h5 mb-0">Estudiantes del curso</h2>

          <button
            class="btn btn-success btn-rounded"
            type="button"
            @click="abrirBuscadorEstudiante"
          >
            <i class="bi bi-plus-circle me-1"></i>
            Agregar estudiante
          </button>
        </div>

        <div v-if="mostrarBuscadorEstudiante" class="position-relative mb-4">
          <label class="form-label">Buscar estudiante</label>

          <input
            v-model="busquedaAlumno"
            type="text"
            class="form-control"
            placeholder="Buscar por nombre o RUT"
            autocomplete="off"
          />

          <div class="form-text">
            Escribe al menos 2 caracteres. Puedes buscar por nombre, apellido o RUT.
          </div>

          <div
            v-if="busquedaAlumno.length >= 2"
            class="list-group position-absolute w-100 shadow-sm mt-1"
            style="z-index: 1050; max-height: 280px; overflow-y: auto"
          >
            <button
              v-for="alumno in alumnosDisponiblesFiltrados"
              :key="alumno.id"
              type="button"
              class="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
              @click="agregarAlumnoDirecto(alumno.id)"
            >
              <div>
                <div class="fw-semibold">
                  {{ nombreAlumno(alumno) }}
                </div>

                <small class="text-muted">
                  RUT: {{ alumno.rut || 'No registrado' }}
                  <span v-if="alumno.cursoId">
                    · Curso actual:
                    {{ academic.getCursoNombre(academic.getCursoById(alumno.cursoId)) }}
                  </span>
                  <span v-else> · Sin curso </span>
                </small>
              </div>

              <span class="badge text-bg-success"> Agregar </span>
            </button>

            <div v-if="alumnosDisponiblesFiltrados.length === 0" class="list-group-item text-muted">
              No se encontraron estudiantes disponibles.
            </div>
          </div>
        </div>

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>Estudiante</th>
                <th>RUT</th>
                <th>Estado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="alumno in alumnosDelCurso" :key="alumno.id">
                <td>{{ nombreAlumno(alumno) }}</td>
                <td>{{ alumno.rut || 'No registrado' }}</td>
                <td>
                  <span class="badge text-bg-success">
                    {{ alumno.estado || 'Activo' }}
                  </span>
                </td>
                <td class="text-end">
                  <button
                    class="btn btn-outline-danger btn-sm btn-rounded"
                    @click="quitarAlumno(alumno.id)"
                  >
                    Quitar
                  </button>
                </td>
              </tr>

              <tr v-if="alumnosDelCurso.length === 0">
                <td colspan="4" class="text-center text-muted py-4">
                  Este curso aún no tiene estudiantes asignados.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Profesor jefe -->
    <div v-if="tabActiva === 'profesor'" class="card">
      <div class="card-body">
        <h2 class="h5 mb-3">Profesor jefe</h2>

        <div v-if="profesorJefe" class="alert alert-info">
          Profesor jefe actual:
          <strong>{{ profesorJefe.nombres }} {{ profesorJefe.apellido_paterno }}</strong>
        </div>

        <div class="row g-3 align-items-end">
          <div class="col-md-8">
            <label class="form-label">Seleccionar profesor jefe</label>
            <select v-model="profesorJefeId" class="form-select">
              <option value="">Sin profesor jefe</option>
              <option
                v-for="docente in academic.profesoresFiltrados"
                :key="docente.id_docente"
                :value="docente.id_docente"
              >
                {{ docente.nombres }} {{ docente.apellido_paterno }} {{ docente.apellido_materno }}
              </option>
            </select>
          </div>

          <div class="col-md-4">
            <button class="btn btn-success btn-rounded w-100" @click="guardarProfesorJefe">
              Guardar profesor jefe
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Asignaturas -->
    <div v-if="tabActiva === 'asignaturas'" class="card">
      <div class="card-body">
        <h2 class="h5 mb-3">Asignaturas del curso</h2>

        <div class="row g-3 align-items-end mb-4">
          <div class="col-md-5">
            <label class="form-label">Asignatura</label>
            <select v-model="asignaturaSeleccionadaId" class="form-select">
              <option value="">Seleccionar asignatura</option>
              <option
                v-for="asignatura in academic.asignaturasFiltradas"
                :key="asignatura.id"
                :value="asignatura.id"
              >
                {{ asignatura.nombre }}
              </option>
            </select>
          </div>

          <div class="col-md-5">
            <label class="form-label">Docente responsable</label>
            <select v-model="docenteSeleccionadoId" class="form-select">
              <option value="">Sin docente</option>
              <option
                v-for="docente in academic.profesoresFiltrados"
                :key="docente.id_docente"
                :value="docente.id_docente"
              >
                {{ docente.nombres }} {{ docente.apellido_paterno }}
              </option>
            </select>
          </div>

          <div class="col-md-2">
            <button class="btn btn-success btn-rounded w-100" @click="agregarAsignatura">
              Agregar
            </button>
          </div>
        </div>

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>Asignatura</th>
                <th>Docente responsable</th>
                <th>Estado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="item in asignaturasCurso" :key="item.id">
                <td>{{ item.asignaturaNombre }}</td>
                <td>{{ item.docenteNombre }}</td>
                <td>
                  <span class="badge text-bg-success">
                    {{ item.estado }}
                  </span>
                </td>
                <td class="text-end">
                  <button
                    class="btn btn-outline-danger btn-sm btn-rounded"
                    @click="quitarAsignatura(item.asignaturaId)"
                  >
                    Quitar
                  </button>
                </td>
              </tr>

              <tr v-if="asignaturasCurso.length === 0">
                <td colspan="4" class="text-center text-muted py-4">
                  Este curso aún no tiene asignaturas configuradas.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!--- Planificación académica -->

    <div v-if="tabActiva === 'planificacion'" class="card border-0 shadow-sm">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-start mb-4">
          <div>
            <h2 class="h5 mb-1">Planificación académica</h2>
            <p class="text-muted mb-0">
              Configuración de periodos y planificación horaria del curso.
            </p>
          </div>

          <span class="badge text-bg-light border"> Próximamente </span>
        </div>

        <div class="alert alert-light border">
          <i class="bi bi-info-circle me-2"></i>
          Este módulo dependerá del
          <strong>modo aula del establecimiento</strong>
          (semestral, trimestral o anual).
        </div>

        <div class="row g-3">
          <div class="col-md-4">
            <div class="card h-100 border">
              <div class="card-body">
                <div class="mb-3">
                  <i class="bi bi-calendar-event fs-4 text-primary"></i>
                </div>

                <h3 class="h6 fw-bold">Periodos académicos</h3>

                <p class="text-muted small mb-0">
                  Configuración de semestres, trimestres o periodos académicos según el
                  establecimiento.
                </p>
              </div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="card h-100 border">
              <div class="card-body">
                <div class="mb-3">
                  <i class="bi bi-clock fs-4 text-primary"></i>
                </div>

                <h3 class="h6 fw-bold">Bloques horarios</h3>

                <p class="text-muted small mb-0">
                  Definición de horarios de inicio, término, recreos y bloques académicos.
                </p>
              </div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="card h-100 border">
              <div class="card-body">
                <div class="mb-3">
                  <i class="bi bi-table fs-4 text-primary"></i>
                </div>

                <h3 class="h6 fw-bold">Horario semanal</h3>

                <p class="text-muted small mb-0">
                  Distribución de asignaturas y docentes dentro de la semana.
                </p>
              </div>
            </div>
          </div>
        </div>

        <div class="mt-4">
          <button class="btn btn-primary btn-rounded" disabled>
            <i class="bi bi-gear me-2"></i>
            Configurar planificación
          </button>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="alert alert-warning">Curso no encontrado.</div>
</template>
