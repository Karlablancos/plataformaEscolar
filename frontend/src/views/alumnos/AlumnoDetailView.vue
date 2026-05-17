<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAcademicStore } from '../../stores/academicStore'
import AlumnoApoderadoModal from '../../components/alumnos/AlumnoApoderadoModal.vue'

const route = useRoute()
const router = useRouter()
const academic = useAcademicStore()

const activeTab = ref('resumen')

const alumnoId = computed(() => Number(route.params.id))
const isEditing = computed(() => route.path.endsWith('/editar'))

const alumno = computed(() => academic.getAlumnoById(alumnoId.value))
const cursos = computed(() => academic.cursosFiltrados)
const tiposNee = computed(() => academic.tiposNee)

const apoderados = computed(() => academic.getApoderadosByEstudianteId(alumnoId.value))
const salud = computed(() => academic.getAntecedenteSaludByEstudianteId(alumnoId.value))
const familiar = computed(() => academic.getAntecedenteFamiliarByEstudianteId(alumnoId.value))
const documentos = computed(() => academic.getDocumentosByEstudianteId(alumnoId.value))

const form = reactive({
  nombres: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  rut: '',
  sexo: '',
  fechaNacimiento: '',
  correoElectronico: '',
  telefono: '',
  calle: '',
  numero: '',
  comuna: '',
  colegioProcedente: '',
  fechaMatricula: '',
  cursoId: '',
  prioritario: false,
  preferente: false,
  tieneNee: false,
  tipoNeeId: null,
  enPie: false,
  estado: 'Activo',
})

watch(
  alumno,
  () => {
    if (!alumno.value) return

    Object.assign(form, {
      nombres: alumno.value.nombres || '',
      apellidoPaterno: alumno.value.apellidoPaterno || '',
      apellidoMaterno: alumno.value.apellidoMaterno || '',
      rut: alumno.value.rut || '',
      sexo: alumno.value.sexo || '',
      fechaNacimiento: normalizarFechaInput(alumno.value.fechaNacimiento),
      correoElectronico: alumno.value.correoElectronico || '',
      telefono: alumno.value.telefono || '',
      calle: alumno.value.calle || '',
      numero: alumno.value.numero || '',
      comuna: alumno.value.comuna || '',
      colegioProcedente: alumno.value.colegioProcedente || '',
      fechaMatricula: normalizarFechaInput(alumno.value.fechaMatricula),
      cursoId: alumno.value.cursoId || '',
      prioritario: Boolean(alumno.value.prioritario),
      preferente: Boolean(alumno.value.preferente),
      tieneNee: Boolean(alumno.value.tieneNee),
      tipoNeeId: alumno.value.tipoNeeId || null,
      enPie: Boolean(alumno.value.enPie),
      estado: alumno.value.estado || 'Activo',
    })
  },
  { immediate: true },
)

const nombreCompleto = computed(() => {
  return `${form.nombres} ${form.apellidoPaterno} ${form.apellidoMaterno}`
    .replace(/\s+/g, ' ')
    .trim()
})

const cursoActual = computed(() => {
  const curso = cursos.value.find((item) => item.id === Number(alumno.value?.cursoId))
  return curso ? curso.nombre : 'Sin curso'
})

const tipoNeeActual = computed(() => {
  if (!alumno.value?.tipoNeeId) return 'No registra'
  const tipo = academic.getTipoNeeById(alumno.value.tipoNeeId)
  return tipo ? `${tipo.nombre} - ${tipo.nombreCompleto}` : 'No registra'
})

const apoderadoTitular = computed(() => {
  return apoderados.value.find((item) => item.esApoderadoTitular) || null
})

const direccionCompleta = computed(() => {
  if (!alumno.value) return 'No registra'

  const direccion = `${alumno.value.calle || ''} ${alumno.value.numero || ''}, ${
    alumno.value.comuna || ''
  }`
    .replace(/\s+/g, ' ')
    .trim()

  return direccion || 'No registra'
})

function normalizarFechaInput(fecha) {
  if (!fecha) return ''
  if (/^\d{4}-\d{2}-\d{2}$/.test(fecha)) return fecha

  const partes = fecha.split('/')
  if (partes.length !== 3) return fecha

  const [dia, mes, anio] = partes
  return `${anio}-${mes.padStart(2, '0')}-${dia.padStart(2, '0')}`
}

function guardarCambios() {
  if (!form.nombres || !form.apellidoPaterno || !form.rut) {
    alert('Completa al menos nombres, apellido paterno y RUT.')
    return
  }

  academic.actualizarAlumno(alumnoId.value, {
    ...form,
    nombre: nombreCompleto.value,
    cursoId: form.cursoId ? Number(form.cursoId) : null,
    tipoNeeId: form.tipoNeeId ? Number(form.tipoNeeId) : null,
  })

  if (form.cursoId) {
    academic.asignarAlumnoACurso(form.cursoId, alumnoId.value)
  }

  router.push(`/admin/alumnos/${alumnoId.value}`)
}

function cancelarEdicion() {
  router.push(`/admin/alumnos/${alumnoId.value}`)
}

const nuevoApoderado = reactive({
  rut: '',
  nombres: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  correoElectronico: '',
  telefonoPrincipal: '',
  telefonoSecundario: '',
  calle: '',
  numero: '',
  comuna: '',
  parentesco: '',
  esApoderadoTitular: false,
  viveConEstudiante: false,
})

function limpiarNuevoApoderado() {
  Object.assign(nuevoApoderado, {
    rut: '',
    nombres: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    correoElectronico: '',
    telefonoPrincipal: '',
    telefonoSecundario: '',
    calle: '',
    numero: '',
    comuna: '',
    parentesco: '',
    esApoderadoTitular: false,
    viveConEstudiante: false,
  })
}

function guardarNuevoApoderado() {
  if (
    !nuevoApoderado.nombres ||
    !nuevoApoderado.apellidoPaterno ||
    !nuevoApoderado.telefonoPrincipal
  ) {
    alert('Completa al menos nombres, apellido paterno y teléfono principal.')
    return
  }

  const nombreCompletoApoderado =
    `${nuevoApoderado.nombres} ${nuevoApoderado.apellidoPaterno} ${nuevoApoderado.apellidoMaterno}`
      .replace(/\s+/g, ' ')
      .trim()

  academic.agregarApoderadoAEstudiante(
    alumnoId.value,
    {
      rut: nuevoApoderado.rut,
      nombres: nuevoApoderado.nombres,
      apellidoPaterno: nuevoApoderado.apellidoPaterno,
      apellidoMaterno: nuevoApoderado.apellidoMaterno,
      nombre: nombreCompletoApoderado,
      correoElectronico: nuevoApoderado.correoElectronico,
      telefonoPrincipal: nuevoApoderado.telefonoPrincipal,
      telefonoSecundario: nuevoApoderado.telefonoSecundario,
      calle: nuevoApoderado.calle,
      numero: nuevoApoderado.numero,
      comuna: nuevoApoderado.comuna,
    },
    {
      parentesco: nuevoApoderado.parentesco,
      esApoderadoTitular: nuevoApoderado.esApoderadoTitular,
      viveConEstudiante: nuevoApoderado.viveConEstudiante,
    },
  )

  limpiarNuevoApoderado()

  const modalElement = document.getElementById('modalAgregarApoderado')
  const modal = window.bootstrap.Modal.getInstance(modalElement)
  modal?.hide()
}
function guardarApoderado(payload) {
  academic.agregarApoderadoAEstudiante(alumnoId.value, payload.apoderado, payload.relacion)
}
</script>

<template>
  <main class="container-fluid py-4">
    <div v-if="!alumno" class="alert alert-warning">
      <h1 class="h4 mb-2">Alumno no encontrado</h1>
      <RouterLink to="/admin/alumnos" class="btn btn-outline-secondary btn-sm">
        Volver al listado
      </RouterLink>
    </div>

    <template v-else>
      <div class="d-flex flex-column flex-md-row justify-content-between gap-3 mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-2">
              <li class="breadcrumb-item">
                <i class="bi bi-house"></i>
              </li>
              <li class="breadcrumb-item">
                <RouterLink to="/admin/alumnos">Alumnos</RouterLink>
              </li>
              <li class="breadcrumb-item active">
                {{ isEditing ? 'Editar alumno' : 'Ficha alumno' }}
              </li>
            </ol>
          </nav>

          <h1 class="h3 mt-3 mb-1">{{ alumno.nombre }}</h1>

          <div class="d-flex flex-wrap pb-3 gap-2">
            <span class="badge rounded-pill text-bg-primary">{{ cursoActual }}</span>
            <span
              class="badge rounded-pill"
              :class="alumno.estado === 'Activo' ? 'text-bg-success' : 'text-bg-warning'"
            >
              {{ alumno.estado }}
            </span>
            <span
              class="badge rounded-pill"
              :class="alumno.enPie ? 'text-bg-info' : 'text-bg-secondary'"
            >
              PIE: {{ alumno.enPie ? 'Sí' : 'No' }}
            </span>
            <span v-if="alumno.prioritario" class="badge rounded-pill text-bg-warning"
              >Prioritario</span
            >
          </div>
        </div>

        <div class="d-flex align-items-start gap-2">
          <RouterLink to="/admin/alumnos" class="btn btn-rounded btn-outline-secondary">
            ← Volver
          </RouterLink>

          <RouterLink
            v-if="!isEditing"
            :to="`/admin/alumnos/${alumno.id}/editar`"
            class="btn btn-success btn-rounded"
          >
            Editar Alumno/a
          </RouterLink>
        </div>
      </div>

      <form v-if="isEditing" @submit.prevent="guardarCambios">
        <div class="row g-3">
          <div class="col-lg-8">
            <div class="card mb-3">
              <div class="card-header fw-semibold">Datos personales</div>

              <div class="card-body">
                <div class="row g-3">
                  <div class="col-md-4">
                    <label class="form-label">Nombres</label>
                    <input v-model="form.nombres" type="text" class="form-control" />
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">Apellido paterno</label>
                    <input v-model="form.apellidoPaterno" type="text" class="form-control" />
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">Apellido materno</label>
                    <input v-model="form.apellidoMaterno" type="text" class="form-control" />
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">RUT</label>
                    <input v-model="form.rut" type="text" class="form-control" />
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">Sexo</label>
                    <select v-model="form.sexo" class="form-select">
                      <option value="">Seleccionar</option>
                      <option value="F">Femenino</option>
                      <option value="M">Masculino</option>
                      <option value="Otro">Otro</option>
                    </select>
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">Fecha nacimiento</label>
                    <input v-model="form.fechaNacimiento" type="date" class="form-control" />
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-3">
              <div class="card-header fw-semibold">Contacto y dirección</div>

              <div class="card-body">
                <div class="row g-3">
                  <div class="col-md-6">
                    <label class="form-label">Correo electrónico</label>
                    <input v-model="form.correoElectronico" type="email" class="form-control" />
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Teléfono</label>
                    <input v-model="form.telefono" type="text" class="form-control" />
                  </div>

                  <div class="col-md-5">
                    <label class="form-label">Calle</label>
                    <input v-model="form.calle" type="text" class="form-control" />
                  </div>

                  <div class="col-md-3">
                    <label class="form-label">Número</label>
                    <input v-model="form.numero" type="text" class="form-control" />
                  </div>

                  <div class="col-md-4">
                    <label class="form-label">Comuna</label>
                    <input v-model="form.comuna" type="text" class="form-control" />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-lg-4">
            <div class="card mb-3">
              <div class="card-header fw-semibold">Información académica</div>

              <div class="card-body">
                <div class="mb-3">
                  <label class="form-label">Curso</label>
                  <select v-model="form.cursoId" class="form-select">
                    <option value="">Sin curso</option>
                    <option v-for="curso in cursos" :key="curso.id" :value="curso.id">
                      {{ curso.nombre }}
                    </option>
                  </select>
                </div>

                <div class="mb-3">
                  <label class="form-label">Colegio procedente</label>
                  <input v-model="form.colegioProcedente" type="text" class="form-control" />
                </div>

                <div class="mb-3">
                  <label class="form-label">Fecha matrícula</label>
                  <input v-model="form.fechaMatricula" type="date" class="form-control" />
                </div>

                <div>
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select">
                    <option value="Activo">Activo</option>
                    <option value="Retirado">Retirado</option>
                    <option value="Egresado">Egresado</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="card mb-3">
              <div class="card-header fw-semibold">PIE / NEE</div>

              <div class="card-body">
                <div class="form-check mb-2">
                  <input
                    id="prioritario"
                    v-model="form.prioritario"
                    type="checkbox"
                    class="form-check-input"
                  />
                  <label for="prioritario" class="form-check-label">Alumno prioritario</label>
                </div>

                <div class="form-check mb-2">
                  <input
                    id="preferente"
                    v-model="form.preferente"
                    type="checkbox"
                    class="form-check-input"
                  />
                  <label for="preferente" class="form-check-label">Alumno preferente</label>
                </div>

                <div class="form-check mb-2">
                  <input
                    id="tieneNee"
                    v-model="form.tieneNee"
                    type="checkbox"
                    class="form-check-input"
                  />
                  <label for="tieneNee" class="form-check-label">Tiene NEE</label>
                </div>

                <div class="mb-3">
                  <label class="form-label">Tipo NEE</label>
                  <select v-model="form.tipoNeeId" class="form-select">
                    <option :value="null">No registra</option>
                    <option v-for="tipo in tiposNee" :key="tipo.id" :value="tipo.id">
                      {{ tipo.nombre }} - {{ tipo.nombreCompleto }}
                    </option>
                  </select>
                </div>

                <div class="form-check">
                  <input id="enPie" v-model="form.enPie" type="checkbox" class="form-check-input" />
                  <label for="enPie" class="form-check-label">Programa PIE</label>
                </div>
              </div>
            </div>

            <div class="d-grid gap-2">
              <button type="submit" class="btn btn-success">Guardar cambios</button>
              <button type="button" class="btn btn-outline-secondary" @click="cancelarEdicion">
                Cancelar
              </button>
            </div>
          </div>
        </div>
      </form>

      <template v-else>
        <ul class="nav nav-tabs mb-3">
          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'resumen' }"
              @click="activeTab = 'resumen'"
            >
              Resumen
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'datos' }"
              @click="activeTab = 'datos'"
            >
              Datos personales
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'apoderados' }"
              @click="activeTab = 'apoderados'"
            >
              Apoderados
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'salud' }"
              @click="activeTab = 'salud'"
            >
              Salud
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'familia' }"
              @click="activeTab = 'familia'"
            >
              Familia
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'documentos' }"
              @click="activeTab = 'documentos'"
            >
              Documentos
            </button>
          </li>

          <li class="nav-item">
            <button
              type="button"
              class="nav-link"
              :class="{ active: activeTab === 'historial' }"
              @click="activeTab = 'historial'"
            >
              Historial académico
            </button>
          </li>
        </ul>

        <div v-if="activeTab === 'resumen'" class="row g-3">
          <div class="col-lg-4">
            <div class="card h-100">
              <div class="card-body text-center">
                <div
                  class="bg-light mt-4 rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                  style="width: 96px; height: 96px"
                >
                  <span class="h2 mb-0">{{ alumno.nombres?.charAt(0) || 'A' }}</span>
                </div>

                <h2 class="h5 mb-1">{{ alumno.nombre }}</h2>
                <p class="text-muted mb-3">{{ alumno.rut }}</p>
              </div>
            </div>
          </div>

          <div class="col-lg-8">
            <div class="card h-100">
              <div class="card-header fw-semibold">Resumen general</div>
              <div class="card-body">
                <div class="row g-3">
                  <div class="col-md-6">
                    <div class="text-muted small">Apoderado titular</div>
                    <div class="fw-semibold">
                      {{ apoderadoTitular?.nombre || 'Sin apoderado titular' }}
                    </div>
                  </div>

                  <div class="col-md-6">
                    <div class="text-muted small">Teléfono apoderado</div>
                    <div class="fw-semibold">
                      {{ apoderadoTitular?.telefonoPrincipal || 'No registra' }}
                    </div>
                  </div>

                  <div class="col-md-6">
                    <div class="text-muted small">Contacto emergencia</div>
                    <div class="fw-semibold">
                      {{ salud?.contactoEmergencia || 'No registra' }}
                    </div>
                  </div>

                  <div class="col-md-6">
                    <div class="text-muted small">Teléfono emergencia</div>
                    <div class="fw-semibold">
                      {{ salud?.telefonoEmergencia || 'No registra' }}
                    </div>
                  </div>

                  <div class="col-md-6">
                    <div class="text-muted small">Tipo NEE</div>
                    <div class="fw-semibold">{{ tipoNeeActual }}</div>
                  </div>

                  <div class="col-md-6">
                    <div class="text-muted small">Dirección</div>
                    <div class="fw-semibold">{{ direccionCompleta }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="activeTab === 'datos'" class="card">
          <div class="card-header fw-semibold">Datos personales y académicos</div>
          <div class="card-body">
            <div class="row g-3">
              <div class="col-md-4">
                <span class="text-muted small d-block">RUT</span><strong>{{ alumno.rut }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Sexo</span
                ><strong>{{ alumno.sexo || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Fecha nacimiento</span
                ><strong>{{ alumno.fechaNacimiento || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Correo</span
                ><strong>{{ alumno.correoElectronico || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Teléfono</span
                ><strong>{{ alumno.telefono || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Dirección</span
                ><strong>{{ direccionCompleta }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Curso</span
                ><strong>{{ cursoActual }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Colegio procedente</span
                ><strong>{{ alumno.colegioProcedente || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Fecha matrícula</span
                ><strong>{{ alumno.fechaMatricula || 'No registra' }}</strong>
              </div>
            </div>
          </div>
        </div>

        <div v-if="activeTab === 'apoderados'" class="card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <span class="fw-semibold">Apoderados</span>

            <button
              type="button"
              class="btn btn-primary btn-rounded btn-sm"
              data-bs-toggle="modal"
              data-bs-target="#modalAgregarApoderado"
            >
              + Agregar apoderado
            </button>
          </div>

          <div class="card-body">
            <div v-if="apoderados.length" class="table-responsive">
              <table class="table table-sm table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Parentesco</th>
                    <th>Teléfono</th>
                    <th>Correo</th>
                    <th>Vive con estudiante</th>
                    <th>Titular</th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="apoderado in apoderados" :key="apoderado.id">
                    <td>{{ apoderado.nombre }}</td>
                    <td>{{ apoderado.parentesco }}</td>
                    <td>{{ apoderado.telefonoPrincipal || 'No registra' }}</td>
                    <td>{{ apoderado.correoElectronico || 'No registra' }}</td>
                    <td>{{ apoderado.viveConEstudiante ? 'Sí' : 'No' }}</td>
                    <td>
                      <span
                        class="badge"
                        :class="
                          apoderado.esApoderadoTitular ? 'text-bg-success' : 'text-bg-secondary'
                        "
                      >
                        {{ apoderado.esApoderadoTitular ? 'Sí' : 'No' }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div v-else class="text-center py-4">
              <p class="text-muted mb-3">Sin apoderados registrados.</p>

              <button
                type="button"
                class="btn btn-primary"
                data-bs-toggle="modal"
                data-bs-target="#modalAgregarApoderado"
              >
                Agregar primer apoderado
              </button>
            </div>
          </div>
        </div>

        <div v-if="activeTab === 'salud'" class="card">
          <div class="card-header fw-semibold">Antecedentes de salud</div>
          <div class="card-body">
            <div v-if="salud" class="row g-3">
              <div class="col-md-4">
                <span class="text-muted small d-block">Grupo sanguíneo</span
                ><strong>{{ salud.grupoSanguineo || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Sistema salud</span
                ><strong>{{ salud.sistemaSalud || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Alergias</span
                ><strong>{{ salud.alergias || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Enfermedades</span
                ><strong>{{ salud.enfermedades || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Medicamentos</span
                ><strong>{{ salud.medicamentos || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Contacto emergencia</span
                ><strong>{{ salud.contactoEmergencia || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Teléfono emergencia</span
                ><strong>{{ salud.telefonoEmergencia || 'No registra' }}</strong>
              </div>
            </div>

            <p v-else class="text-muted mb-0">Sin antecedentes de salud registrados.</p>
          </div>
        </div>

        <div v-if="activeTab === 'familia'" class="card">
          <div class="card-header fw-semibold">Antecedentes familiares</div>
          <div class="card-body">
            <div v-if="familiar" class="row g-3">
              <div class="col-md-4">
                <span class="text-muted small d-block">Vive con</span
                ><strong>{{ familiar.viveCon || 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">N° hermanos</span
                ><strong>{{ familiar.numeroHermanos ?? 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Lugar entre hermanos</span
                ><strong>{{ familiar.lugarEntreHermanos ?? 'No registra' }}</strong>
              </div>
              <div class="col-md-4">
                <span class="text-muted small d-block">Hermanos en establecimiento</span
                ><strong>{{ familiar.hermanosEnEstablecimiento ? 'Sí' : 'No' }}</strong>
              </div>
              <div class="col-12">
                <span class="text-muted small d-block">Observación</span
                ><strong>{{ familiar.observacion || 'No registra' }}</strong>
              </div>
            </div>

            <p v-else class="text-muted mb-0">Sin antecedentes familiares registrados.</p>
          </div>
        </div>

        <div v-if="activeTab === 'documentos'" class="card">
          <div class="card-header fw-semibold">Documentos</div>
          <div class="card-body">
            <ul v-if="documentos.length" class="list-group list-group-flush">
              <li
                v-for="documento in documentos"
                :key="documento.id"
                class="list-group-item d-flex justify-content-between gap-3 px-0"
              >
                <span>{{ documento.tipoDocumento }}</span>
                <span class="text-muted">{{ documento.nombreArchivo }}</span>
              </li>
            </ul>

            <p v-else class="text-muted mb-0">Sin documentos registrados.</p>
          </div>
        </div>

        <div v-if="activeTab === 'historial'" class="card">
          <div class="card-header fw-semibold">Historial académico</div>
          <div class="card-body">
            <div class="alert alert-secondary mb-0">
              Esta sección queda preparada para asistencia, notas, anotaciones y evaluaciones cuando
              conectemos la API.
            </div>
          </div>
        </div>
      </template>
    </template>
    <AlumnoApoderadoModal modal-id="modalAgregarApoderado" @guardar="guardarApoderado" />
  </main>
</template>
