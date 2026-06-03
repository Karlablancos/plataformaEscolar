<script setup>
import { computed, ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useAlumnosStore } from '@/stores/alumnosStore'
import { useCursosStore } from '@/stores/cursosStore'

const alumnosStore = useAlumnosStore()
const cursosStore = useCursosStore()

const cargando = computed(() => alumnosStore.cargando)

onMounted(() => {
  alumnosStore.cargarAlumnos()
})

const search = ref('')
const cursoFiltro = ref('')
const estadoFiltro = ref('')
const pieFiltro = ref('')

const alumnos = computed(() => alumnosStore.alumnosFiltradosNormalizados)
const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)

const normalizarTexto = (texto) =>
  String(texto || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')

const getAlumnoId = (alumno) => alumno.id_alumno ?? alumno.id

const getCursoId = (curso) => curso.id_curso ?? curso.id

const getNombreAlumno = (alumno) => {
  if (alumno.nombreCompleto) return alumno.nombreCompleto
  if (alumno.nombre) return alumno.nombre

  return [
    alumno.nombres,
    alumno.apellido_paterno || alumno.apellidoPaterno,
    alumno.apellido_materno || alumno.apellidoMaterno,
  ]
    .filter(Boolean)
    .join(' ')
    .replace(/\s+/g, ' ')
    .trim()
}

const getCorreoAlumno = (alumno) => {
  return alumno.correo_electronico || alumno.correoElectronico || 'Sin correo registrado'
}

const getCursoNombre = (cursoId) => {
  if (!cursoId) return 'Sin curso'

  const curso = cursos.value.find((item) => getCursoId(item) === Number(cursoId))

  return curso?.nombre || 'Sin curso'
}

const getCursoAlumnoId = (alumno) => {
  return alumno.id_curso ?? alumno.cursoId ?? null
}

const getApoderadoNombre = (apoderado) => {
  if (!apoderado) return 'Sin apoderado'
  if (apoderado.nombreCompleto) return apoderado.nombreCompleto
  if (apoderado.nombre) return apoderado.nombre

  return (
    [
      apoderado.nombres,
      apoderado.apellido_paterno || apoderado.apellidoPaterno,
      apoderado.apellido_materno || apoderado.apellidoMaterno,
    ]
      .filter(Boolean)
      .join(' ')
      .replace(/\s+/g, ' ')
      .trim() || 'Sin apoderado'
  )
}

const getApoderadoTitular = (alumnoId) => {
  const apoderados = alumnosStore.getApoderadosByEstudianteId(alumnoId)
  const titular = apoderados.find((apoderado) => apoderado.esApoderadoTitular)

  return getApoderadoNombre(titular || apoderados[0])
}

const alumnosFiltrados = computed(() => {
  const term = normalizarTexto(search.value)

  return alumnos.value.filter((alumno) => {
    const alumnoId = getAlumnoId(alumno)
    const cursoId = getCursoAlumnoId(alumno)
    const nombreAlumno = getNombreAlumno(alumno)
    const cursoNombre = getCursoNombre(cursoId)
    const apoderadoTitular = getApoderadoTitular(alumnoId)

    const matchSearch =
      normalizarTexto(nombreAlumno).includes(term) ||
      normalizarTexto(alumno.rut).includes(term) ||
      normalizarTexto(cursoNombre).includes(term) ||
      normalizarTexto(apoderadoTitular).includes(term)

    const matchCurso = !cursoFiltro.value || Number(cursoFiltro.value) === Number(cursoId)

    const matchEstado = !estadoFiltro.value || alumno.estado === estadoFiltro.value

    const enPie = alumno.enPie ?? alumno.alumnoPie ?? alumno.programaIntegracionEscolar ?? false

    const matchPie =
      !pieFiltro.value ||
      (pieFiltro.value === 'si' && enPie) ||
      (pieFiltro.value === 'no' && !enPie)

    return matchSearch && matchCurso && matchEstado && matchPie
  })
})

const eliminarAlumno = (alumno) => {
  const confirmar = window.confirm(`¿Seguro que deseas eliminar a ${getNombreAlumno(alumno)}?`)

  if (!confirmar) return

  alumnosStore.eliminarAlumno(getAlumnoId(alumno))
}
</script>

<template>
  <section>
    <div class="d-flex p-4 justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <div>
        <h1 class="h3 fw-bold mb-1">
          <i class="bi bi-people me-2"></i>
          Alumnos
        </h1>
        <p class="text-muted mb-0">Listado general de estudiantes del establecimiento.</p>
      </div>

      <RouterLink to="/admin/alumnos/nuevo" class="btn btn-success btn-rounded">
        <i class="bi bi-plus-circle me-2"></i>
        Agregar alumno
      </RouterLink>
    </div>

    <div class="card border-0 shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-md-5">
            <label class="form-label">Buscar alumno</label>
            <input
              v-model="search"
              type="text"
              class="form-control"
              placeholder="Buscar por nombre, RUT, curso o apoderado"
            />
          </div>

          <div class="col-md-3">
            <label class="form-label">Curso</label>
            <select v-model="cursoFiltro" class="form-select">
              <option value="">Todos los cursos</option>
              <option v-for="curso in cursos" :key="getCursoId(curso)" :value="getCursoId(curso)">
                {{ curso.nombre }}
              </option>
            </select>
          </div>

          <div class="col-md-2">
            <label class="form-label">Estado</label>
            <select v-model="estadoFiltro" class="form-select">
              <option value="">Todos</option>
              <option value="Activo">Activo</option>
              <option value="Retirado">Retirado</option>
              <option value="Egresado">Egresado</option>
            </select>
          </div>

          <div class="col-md-2">
            <label class="form-label">PIE</label>
            <select v-model="pieFiltro" class="form-select">
              <option value="">Todos</option>
              <option value="si">PIE</option>
              <option value="no">No PIE</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h2 class="h5 fw-bold mb-0">Estudiantes registrados</h2>

          <span class="d-flex align-items-center gap-2">
            <span v-if="cargando" class="spinner-border spinner-border-sm text-primary" />
            <span class="badge rounded-pill text-bg-primary">
              {{ alumnosFiltrados.length }} alumnos
            </span>
          </span>
        </div>

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>N°</th>
                <th>Alumno</th>
                <th>RUT</th>
                <th>Curso</th>
                <th>Apoderado titular</th>
                <th>PIE</th>
                <th>Prioritario</th>
                <th>Estado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="(alumno, index) in alumnosFiltrados" :key="getAlumnoId(alumno)">
                <td>{{ index + 1 }}</td>

                <td>
                  <RouterLink
                    :to="`/admin/alumnos/${getAlumnoId(alumno)}`"
                    class="text-decoration-none"
                  >
                    <div class="fw-semibold text-dark">
                      {{ getNombreAlumno(alumno) }}
                    </div>

                    <small class="text-muted">
                      {{ getCorreoAlumno(alumno) }}
                    </small>
                  </RouterLink>
                </td>

                <td>{{ alumno.rut || 'No registrado' }}</td>

                <td>
                  <span class="badge rounded-pill text-bg-light border">
                    {{ getCursoNombre(getCursoAlumnoId(alumno)) }}
                  </span>
                </td>

                <td>{{ getApoderadoTitular(getAlumnoId(alumno)) }}</td>

                <td>
                  <span
                    class="badge rounded-pill"
                    :class="
                      alumno.enPie || alumno.alumnoPie || alumno.programaIntegracionEscolar
                        ? 'text-bg-success'
                        : 'text-bg-secondary'
                    "
                  >
                    {{
                      alumno.enPie || alumno.alumnoPie || alumno.programaIntegracionEscolar
                        ? 'Sí'
                        : 'No'
                    }}
                  </span>
                </td>

                <td>
                  <span
                    class="badge rounded-pill"
                    :class="alumno.prioritario ? 'text-bg-warning' : 'text-bg-secondary'"
                  >
                    {{ alumno.prioritario ? 'Sí' : 'No' }}
                  </span>
                </td>

                <td>
                  <span
                    class="badge rounded-pill"
                    :class="alumno.estado === 'Activo' ? 'text-bg-success' : 'text-bg-secondary'"
                  >
                    {{ alumno.estado || 'Activo' }}
                  </span>
                </td>

                <td class="text-end">
                  <div class="dropdown">
                    <button
                      class="btn btn-outline-secondary btn-sm btn-rounded dropdown-toggle"
                      type="button"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      Acciones
                    </button>

                    <ul class="dropdown-menu dropdown-menu-end">
                      <li>
                        <RouterLink
                          class="dropdown-item"
                          :to="`/admin/alumnos/${getAlumnoId(alumno)}`"
                        >
                          <i class="bi bi-eye me-2"></i>
                          Ver ficha
                        </RouterLink>
                      </li>

                      <li>
                        <RouterLink
                          class="dropdown-item"
                          :to="`/admin/alumnos/${getAlumnoId(alumno)}/editar`"
                        >
                          <i class="bi bi-pencil-square me-2"></i>
                          Editar
                        </RouterLink>
                      </li>

                      <li><hr class="dropdown-divider" /></li>

                      <li>
                        <button
                          type="button"
                          class="dropdown-item text-danger"
                          @click="eliminarAlumno(alumno)"
                        >
                          <i class="bi bi-trash me-2"></i>
                          Eliminar
                        </button>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>

              <tr v-if="alumnosFiltrados.length === 0">
                <td colspan="9" class="text-center text-muted py-4">
                  No se encontraron alumnos con los filtros aplicados.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </section>
</template>
