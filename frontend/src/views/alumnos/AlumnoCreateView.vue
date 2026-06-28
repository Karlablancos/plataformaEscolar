<script setup>
import { computed, reactive, ref, watch, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAcademicStore } from '../../stores/academicStore'
import { useCursosStore } from '@/stores/cursosStore'
import api from '@/api/axios'
import { extractApiError } from '@/stores/shared/apiError'

const router = useRouter()
const academic = useAcademicStore()
const cursosStore = useCursosStore()

const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)
const tiposNee = computed(() => academic.tiposNee)

const regiones = ref([])
const regionId = ref(null)
const guardando = ref(false)
const mensajeError = ref('')
const rutFormateado = ref('')

const comunasFiltradas = computed(() => {
  if (!regionId.value) return []
  const region = regiones.value.find((r) => r.idRegion === regionId.value)
  return region?.comunas ?? []
})

const form = reactive({
  nombres: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  fechaNacimiento: '',
  correoElectronico: '',
  telefono: '',
  calle: '',
  numero: '',
  comunaId: null,
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

watch(regionId, () => {
  form.comunaId = null
})

onMounted(async () => {
  const { data } = await api.get('/establecimiento/regiones')
  regiones.value = data
})

// ── RUT chileno auto-formato ──────────────────────────────────────────────────
const formatearRut = (valor) => {
  const limpio = valor.replace(/[^0-9kK]/g, '').toUpperCase()
  if (!limpio) return ''
  const dv = limpio.slice(-1)
  const numero = limpio.slice(0, -1)
  if (!numero) return dv
  const conPuntos = numero.replace(/\B(?=(\d{3})+(?!\d))/g, '.')
  return `${conPuntos}-${dv}`
}

const onRutInput = (e) => {
  rutFormateado.value = formatearRut(e.target.value)
}

const extraerRutParts = () => {
  const limpio = rutFormateado.value.replace(/[^0-9kK]/gi, '').toUpperCase()
  return { rut: limpio.slice(0, -1), dv: limpio.slice(-1) }
}
// ─────────────────────────────────────────────────────────────────────────────

const guardarAlumno = async () => {
  mensajeError.value = ''

  const { rut, dv } = extraerRutParts()

  if (!form.nombres || !form.apellidoPaterno) {
    mensajeError.value = 'Nombres y apellido paterno son obligatorios.'
    return
  }
  if (!rut || !dv) {
    mensajeError.value = 'Ingresa un RUT válido con dígito verificador.'
    return
  }
  if (!form.fechaNacimiento) {
    mensajeError.value = 'La fecha de nacimiento es obligatoria.'
    return
  }
  if (!form.correoElectronico) {
    mensajeError.value = 'El correo electrónico es obligatorio.'
    return
  }

  guardando.value = true
  try {
    await academic.agregarAlumno({ ...form, rut, dv })
    router.push('/admin/alumnos')
  } catch (error) {
    mensajeError.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'Error al guardar el alumno. Intenta nuevamente.'
  } finally {
    guardando.value = false
  }
}
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h1 class="h3 mb-1">Agregar alumno</h1>
        <p class="text-muted mb-0">Registro básico del estudiante.</p>
      </div>
      <RouterLink to="/admin/alumnos" class="btn btn-outline-secondary btn-rounded">
        ← Volver al listado
      </RouterLink>
    </div>

    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <form class="card border-0 shadow-sm" @submit.prevent="guardarAlumno">
      <div class="card-body">

        <!-- Datos personales -->
        <h2 class="h5 mb-3" style="color:#1B4F9C">
          <i class="bi bi-person me-2"></i>Datos personales
        </h2>

        <div class="row g-3 mb-4">
          <div class="col-md-4">
            <label class="form-label">Nombres <span class="text-danger">*</span></label>
            <input v-model="form.nombres" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido paterno <span class="text-danger">*</span></label>
            <input v-model="form.apellidoPaterno" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido materno</label>
            <input v-model="form.apellidoMaterno" type="text" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">RUT <span class="text-danger">*</span></label>
            <input
              :value="rutFormateado"
              @input="onRutInput"
              type="text"
              class="form-control"
              placeholder="21.345.678-9"
              maxlength="12"
              required
            />
            <div class="form-text">Se formatea automáticamente.</div>
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
            <label class="form-label">Fecha de nacimiento <span class="text-danger">*</span></label>
            <input v-model="form.fechaNacimiento" type="date" class="form-control" required />
          </div>
        </div>

        <hr class="my-3">

        <!-- Contacto y dirección -->
        <h2 class="h5 mb-3" style="color:#1B4F9C">
          <i class="bi bi-geo-alt me-2"></i>Contacto y dirección
        </h2>

        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <label class="form-label">Correo electrónico <span class="text-danger">*</span></label>
            <input v-model="form.correoElectronico" type="email" class="form-control" required />
          </div>

          <div class="col-md-6">
            <label class="form-label">Teléfono</label>
            <input v-model="form.telefono" type="text" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">Calle</label>
            <input v-model="form.calle" type="text" class="form-control" />
          </div>

          <div class="col-md-2">
            <label class="form-label">Número</label>
            <input v-model="form.numero" type="text" class="form-control" />
          </div>

          <div class="col-md-3">
            <label class="form-label">Región</label>
            <select v-model="regionId" class="form-select">
              <option :value="null">Seleccionar región</option>
              <option v-for="region in regiones" :key="region.idRegion" :value="region.idRegion">
                {{ region.nombreRegion }}
              </option>
            </select>
          </div>

          <div class="col-md-3">
            <label class="form-label">Comuna</label>
            <select v-model="form.comunaId" class="form-select" :disabled="!regionId">
              <option :value="null">Seleccionar comuna</option>
              <option v-for="comuna in comunasFiltradas" :key="comuna.idComuna" :value="comuna.idComuna">
                {{ comuna.nombreComuna }}
              </option>
            </select>
          </div>
        </div>

        <hr class="my-3">

        <!-- Información académica -->
        <h2 class="h5 mb-3" style="color:#1B4F9C">
          <i class="bi bi-book me-2"></i>Información académica
        </h2>

        <div class="row g-3 mb-4">
          <div class="col-md-4">
            <label class="form-label">Curso</label>
            <select v-model="form.cursoId" class="form-select">
              <option value="">Sin curso</option>
              <option v-for="curso in cursos" :key="curso.id" :value="curso.id">
                {{ curso.nombre }}
              </option>
            </select>
          </div>

          <div class="col-md-4">
            <label class="form-label">Colegio procedente</label>
            <input v-model="form.colegioProcedente" type="text" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">Fecha matrícula</label>
            <input v-model="form.fechaMatricula" type="date" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-select">
              <option value="Activo">Activo</option>
              <option value="Retirado">Retirado</option>
              <option value="Egresado">Egresado</option>
            </select>
          </div>
        </div>

        <hr class="my-3">

        <!-- Indicadores -->
        <h2 class="h5 mb-3" style="color:#1B4F9C">
          <i class="bi bi-flag me-2"></i>Indicadores
        </h2>

        <div class="row g-3">
          <div class="col-md-3">
            <div class="form-check">
              <input id="prioritario" v-model="form.prioritario" class="form-check-input" type="checkbox" />
              <label class="form-check-label" for="prioritario">Alumno prioritario</label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input id="preferente" v-model="form.preferente" class="form-check-input" type="checkbox" />
              <label class="form-check-label" for="preferente">Alumno preferente</label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input id="tieneNee" v-model="form.tieneNee" class="form-check-input" type="checkbox" />
              <label class="form-check-label" for="tieneNee">Tiene NEE</label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input id="enPie" v-model="form.enPie" class="form-check-input" type="checkbox" />
              <label class="form-check-label" for="enPie">Programa PIE</label>
            </div>
          </div>

          <div v-if="form.tieneNee" class="col-md-6">
            <label class="form-label">Tipo NEE</label>
            <select v-model="form.tipoNeeId" class="form-select">
              <option :value="null">Seleccionar tipo NEE</option>
              <option v-for="tipo in tiposNee" :key="tipo.id" :value="tipo.id">
                {{ tipo.nombre }}
              </option>
            </select>
          </div>
        </div>

      </div>

      <div class="card-footer d-flex justify-content-end gap-2">
        <RouterLink to="/admin/alumnos" class="btn btn-rounded btn-danger">
          Cancelar
        </RouterLink>

        <button type="submit" class="btn btn-success btn-rounded" :disabled="guardando">
          <span v-if="guardando" class="spinner-border spinner-border-sm me-2"></span>
          Guardar alumno
        </button>
      </div>
    </form>
  </div>
</template>
