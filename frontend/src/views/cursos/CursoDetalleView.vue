<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { RouterLink, useRoute } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import { useCursosStore } from '@/stores/cursosStore'
import { useSalasStore } from '@/stores/salasStore'
import { useToastStore } from '@/stores/toastStore'

const route = useRoute()
const academic = useAcademicStore()
const cursosStore = useCursosStore()
const salasStore = useSalasStore()
const toastStore = useToastStore()
const { salasActivas: salasDisponibles, cargando: cargandoSalas } = storeToRefs(salasStore)

const cursosListPath = computed(() =>
  route.path.startsWith('/profesor') ? '/profesor/cursos' : '/admin/cursos',
)

const cargando = ref(false)
const errorOperacion = ref('')
const errorProfesor = ref('')
const guardandoProfesor = ref(false)
const errorAsignaturas = ref('')
const guardandoAsignatura = ref(false)

onMounted(async () => {
  cargando.value = true
  errorOperacion.value = ''

  try {
    await Promise.all([
      cursosStore.cargarCursos(),
      academic.cargarAlumnos(),
      academic.cargarAsignaturas(),
      academic.cargarDocentes(),
      academic.cargarPeriodos(academic.anioActivo),
    ])
    await academic.sincronizarAlumnosCurso(cursoId.value)
    await academic.sincronizarAsignaturasCurso(cursoId.value)
  } catch (error) {
    errorOperacion.value =
      error?.response?.data?.mensaje || error?.message || 'No se pudo cargar la configuración del curso.'
  } finally {
    cargando.value = false
  }
})

const cargarSalasSiFaltan = async () => {
  if (salasDisponibles.value.length > 0 || cargandoSalas.value) return

  errorAsignaturas.value = ''

  try {
    await salasStore.cargarSalas()
  } catch (error) {
    errorAsignaturas.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudieron cargar las salas del establecimiento.'
  }
}

const tabActiva = ref('estudiantes')

const mostrarBuscadorEstudiante = ref(false)
const busquedaAlumno = ref('')

const profesorJefeId = ref('')
const periodoSeleccionadoId = ref('')
const asignaturaSeleccionadaId = ref('')
const docenteSeleccionadoId = ref('')
const salaSeleccionadaId = ref('')
const horasSemanalesSeleccionadas = ref('')
const opcionesHorasSemanales = [1, 2, 3, 4, 5, 6]

watch(tabActiva, (tab) => {
  if (tab === 'asignaturas') {
    cargarSalasSiFaltan()
  }
})

watch(periodoSeleccionadoId, (periodoId) => {
  asignaturaSeleccionadaId.value = ''
  docenteSeleccionadoId.value = ''
  salaSeleccionadaId.value = ''
  horasSemanalesSeleccionadas.value = ''

  if (periodoId && tabActiva.value === 'asignaturas') {
    cargarSalasSiFaltan()
  }
})

const periodosDisponibles = computed(() => academic.periodosActivos)

watch(periodosDisponibles, (lista) => {
  if (!periodoSeleccionadoId.value) return

  const periodoValido = lista.some(
    (periodo) => Number(periodo.id) === Number(periodoSeleccionadoId.value),
  )

  if (!periodoValido) {
    periodoSeleccionadoId.value = ''
  }
})

const cursoId = computed(() => Number(route.params.id))
const curso = computed(() => academic.getCursoById(cursoId.value))

watch(
  curso,
  (value) => {
    profesorJefeId.value = value?.profesorJefeId ? String(value.profesorJefeId) : ''
  },
  { immediate: true },
)

const nombreCurso = computed(() => {
  if (!curso.value) return ''
  return academic.getCursoNombre(curso.value)
})

const nombreAlumno = (alumno) => {
  return (
    alumno.nombre ||
    alumno.nombreCompleto ||
    `${alumno.nombres} ${alumno.apellido_paterno || alumno.apellidoPaterno || ''} ${alumno.apellido_materno || alumno.apellidoMaterno || ''}`
      .replace(/\s+/g, ' ')
      .trim()
  )
}

const formatRut = (alumno) => {
  if (!alumno?.rut) return 'No registrado'
  const rut = String(alumno.rut).trim()
  const dv = alumno.dv ? String(alumno.dv).trim() : ''
  return dv ? `${rut}-${dv}` : rut
}

const normalizarTexto = (texto) =>
  String(texto || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[.\-\s]/g, '')

const alumnosDelCurso = computed(() => cursosStore.getEstudiantesCurso(cursoId.value))

const alumnosDisponibles = computed(() => {
  return academic.alumnosFiltrados.filter((alumno) => {
    const alumnoId = alumno.id_alumno ?? alumno.id
    return !alumnosDelCurso.value.some(
      (enCurso) => (enCurso.id_alumno ?? enCurso.id) === alumnoId,
    )
  })
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

const asignaturasCurso = computed(() => academic.getAsignaturasCurso(cursoId.value))

const periodoSeleccionado = computed(() => {
  const periodoId = Number(periodoSeleccionadoId.value)
  if (!periodoId) return null

  return periodosDisponibles.value.find((periodo) => Number(periodo.id) === periodoId) ?? null
})

const tituloSeccionAsignaturas = computed(() => {
  if (!periodoSeleccionado.value) return 'Asignaturas del curso'
  return `Asignaturas del curso para ${periodoSeleccionado.value.nombre}`
})

const asignaturasCursoEnPeriodo = computed(() => {
  if (!periodoSeleccionadoId.value) return []

  const periodoId = Number(periodoSeleccionadoId.value)
  return asignaturasCurso.value.filter(
    (item) => Number(item.id_periodo ?? item.periodoId) === periodoId,
  )
})

const asignaturasDisponibles = computed(() => {
  if (!periodoSeleccionadoId.value) return []

  const periodoId = Number(periodoSeleccionadoId.value)
  const idsAsignadas = new Set(
    asignaturasCurso.value
      .filter((item) => Number(item.id_periodo ?? item.periodoId) === periodoId)
      .map((item) => item.asignaturaId ?? item.id_asignatura),
  )

  return academic.asignaturasFiltradas.filter(
    (asignatura) => !idsAsignadas.has(asignatura.id ?? asignatura.id_asignatura),
  )
})

const abrirBuscadorEstudiante = () => {
  mostrarBuscadorEstudiante.value = !mostrarBuscadorEstudiante.value
  busquedaAlumno.value = ''
}

const agregarAlumnoDirecto = async (alumnoId) => {
  errorOperacion.value = ''

  try {
    await academic.asignarAlumnoACurso(cursoId.value, alumnoId)
    busquedaAlumno.value = ''
    mostrarBuscadorEstudiante.value = false
  } catch (error) {
    errorOperacion.value =
      error?.response?.data?.mensaje || error?.message || 'No se pudo agregar el estudiante.'
  }
}

const quitarAlumno = async (alumnoId) => {
  errorOperacion.value = ''

  try {
    await academic.quitarAlumnoDeCurso(cursoId.value, alumnoId)
  } catch (error) {
    errorOperacion.value =
      error?.response?.data?.mensaje || error?.message || 'No se pudo quitar el estudiante.'
  }
}

const guardarProfesorJefe = async () => {
  errorProfesor.value = ''
  guardandoProfesor.value = true

  try {
    await academic.asignarProfesorJefe(cursoId.value, profesorJefeId.value || null)
  } catch (error) {
    errorProfesor.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudo guardar el profesor jefe.'
  } finally {
    guardandoProfesor.value = false
  }
}

const agregarAsignatura = async () => {
  if (
    !periodoSeleccionadoId.value ||
    !asignaturaSeleccionadaId.value ||
    !docenteSeleccionadoId.value ||
    !salaSeleccionadaId.value ||
    !horasSemanalesSeleccionadas.value
  ) {
    errorAsignaturas.value =
      'Debes seleccionar un periodo arriba y completar asignatura, docente, sala y horas semanales.'
    return
  }

  errorAsignaturas.value = ''
  guardandoAsignatura.value = true

  try {
    await academic.agregarAsignaturaACurso(
      cursoId.value,
      asignaturaSeleccionadaId.value,
      docenteSeleccionadoId.value,
      {
        id_periodo: Number(periodoSeleccionadoId.value),
        horas_semanales: Number(horasSemanalesSeleccionadas.value),
        id_sala: Number(salaSeleccionadaId.value),
      },
    )

    asignaturaSeleccionadaId.value = ''
    docenteSeleccionadoId.value = ''
    salaSeleccionadaId.value = ''
    horasSemanalesSeleccionadas.value = ''
    toastStore.show('Asignatura creada exitosamente')
  } catch (error) {
    errorAsignaturas.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudo agregar la asignatura al curso.'
  } finally {
    guardandoAsignatura.value = false
  }
}

const quitarAsignatura = async (item) => {
  errorAsignaturas.value = ''
  guardandoAsignatura.value = true

  try {
    await academic.quitarAsignaturaDeCurso(
      cursoId.value,
      item.asignaturaId ?? item.id_asignatura,
      item.id_periodo ?? item.periodoId,
    )
  } catch (error) {
    errorAsignaturas.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudo quitar la asignatura del curso.'
  } finally {
    guardandoAsignatura.value = false
  }
}
</script>

<template>
  <div v-if="curso">
    <div class="d-flex justify-content-between align-items-start mb-4">
      <div>
        <h1 class="h3 mb-1">Configurar curso {{ nombreCurso }}</h1>
        <p class="text-muted mb-0">
          Gestión académica del curso para el año {{ academic.anioActivo }}.
        </p>
      </div>

      <div class="text-end">
        <RouterLink :to="cursosListPath" class="btn btn-outline-secondary btn-rounded">
          ← Volver
        </RouterLink>

        <div v-if="tabActiva === 'asignaturas'" class="mt-3" style="min-width: 220px">
          <label class="form-label text-start d-block mb-1">Periodo académico</label>
          <select v-model="periodoSeleccionadoId" class="form-select">
            <option value="">Seleccionar periodo</option>
            <option
              v-for="periodo in periodosDisponibles"
              :key="periodo.id"
              :value="periodo.id"
            >
              {{ periodo.nombre }}
            </option>
          </select>
        </div>
      </div>
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
    <div v-if="tabActiva === 'estudiantes'" class="card curso-tab-card">
      <div class="card-body">
        <div v-if="errorOperacion" class="alert alert-danger">{{ errorOperacion }}</div>

        <div v-if="cargando" class="text-muted mb-3">Cargando estudiantes del curso...</div>

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
              <tr v-for="alumno in alumnosDelCurso" :key="alumno.id_alumno ?? alumno.id">
                <td>{{ nombreAlumno(alumno) }}</td>
                <td>{{ formatRut(alumno) }}</td>
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
    <div v-if="tabActiva === 'profesor'" class="card curso-tab-card">
      <div class="card-body">
        <div v-if="errorProfesor" class="alert alert-danger">{{ errorProfesor }}</div>
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
            <button
              class="btn btn-success btn-rounded w-100"
              :disabled="guardandoProfesor"
              @click="guardarProfesorJefe"
            >
              {{ guardandoProfesor ? 'Guardando...' : 'Guardar profesor jefe' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Asignaturas -->
    <div v-if="tabActiva === 'asignaturas'" class="card curso-tab-card">
      <div class="card-body">
        <div v-if="errorAsignaturas" class="alert alert-danger">{{ errorAsignaturas }}</div>
        <h2 class="h5 mb-3">{{ tituloSeccionAsignaturas }}</h2>

        <div v-if="periodosDisponibles.length === 0" class="alert alert-warning">
          No hay periodos académicos configurados para el año {{ academic.anioActivo }}.
        </div>

        <div v-else-if="!periodoSeleccionadoId" class="alert alert-info">
          Selecciona un periodo académico arriba para ver y agregar asignaturas.
        </div>

        <template v-else>
          <div class="row g-3 align-items-end mb-4">
            <div class="col-md-3">
              <label class="form-label">Asignatura</label>
              <select v-model="asignaturaSeleccionadaId" class="form-select">
                <option value="">Seleccionar asignatura</option>
                <option
                  v-for="asignatura in asignaturasDisponibles"
                  :key="asignatura.id"
                  :value="asignatura.id"
                >
                  {{ asignatura.nombre }}
                </option>
              </select>
            </div>

            <div class="col-md-3">
              <label class="form-label">Docente responsable</label>
              <select v-model="docenteSeleccionadoId" class="form-select">
                <option value="">Seleccionar docente</option>
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
              <label class="form-label">Sala</label>
              <select v-model="salaSeleccionadaId" class="form-select">
                <option value="">Seleccionar sala</option>
                <option v-for="sala in salasDisponibles" :key="sala.id_sala ?? sala.id" :value="sala.id">
                  {{ sala.nombre }}
                </option>
              </select>
            </div>

            <div class="col-md-2">
              <label class="form-label">Horas semanales</label>
              <select v-model="horasSemanalesSeleccionadas" class="form-select">
                <option value="">Seleccionar horas</option>
                <option v-for="horas in opcionesHorasSemanales" :key="horas" :value="horas">
                  {{ horas }} {{ horas === 1 ? 'hora' : 'horas' }}
                </option>
              </select>
            </div>

            <div class="col-md-2">
              <button
                class="btn btn-success btn-rounded w-100"
                :disabled="guardandoAsignatura"
                @click="agregarAsignatura"
              >
                {{ guardandoAsignatura ? 'Agregando...' : 'Agregar' }}
              </button>
            </div>
          </div>

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead class="table-light">
                <tr>
                  <th>Asignatura</th>
                  <th>Docente responsable</th>
                  <th>Sala</th>
                  <th>Horas semanales</th>
                  <th>Estado</th>
                  <th class="text-end">Acciones</th>
                </tr>
              </thead>

              <tbody>
                <tr
                  v-for="item in asignaturasCursoEnPeriodo"
                  :key="item.id_curso_asignatura ?? item.id"
                >
                  <td>{{ item.asignaturaNombre }}</td>
                  <td>{{ item.docenteNombre }}</td>
                  <td>{{ item.salaNombre || item.sala_nombre || '—' }}</td>
                  <td>{{ item.horasSemanales ?? item.horas_semanales ?? '—' }}</td>
                  <td>
                    <span class="badge text-bg-success">
                      {{ item.estado }}
                    </span>
                  </td>
                  <td class="text-end">
                    <button
                      class="btn btn-outline-danger btn-sm btn-rounded"
                      :disabled="guardandoAsignatura"
                      @click="quitarAsignatura(item)"
                    >
                      Quitar
                    </button>
                  </td>
                </tr>

                <tr v-if="asignaturasCursoEnPeriodo.length === 0">
                  <td colspan="6" class="text-center text-muted py-4">
                    Este curso aún no tiene asignaturas en {{ periodoSeleccionado?.nombre }}.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </template>
      </div>
    </div>

    <!--- Planificación académica -->

    <div v-if="tabActiva === 'planificacion'" class="card curso-tab-card border-0 shadow-sm">
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

<style scoped>
.curso-tab-card {
  border-top: 0;
  border-radius: 0 0 10px 10px;
  margin-top: -24px;
}
</style>
