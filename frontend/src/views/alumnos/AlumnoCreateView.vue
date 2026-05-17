<script setup>
import { computed, reactive } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAcademicStore } from '../../stores/academicStore'
import { comunasMock } from '@/data'

const router = useRouter()
const academic = useAcademicStore()

const cursos = computed(() => academic.cursosFiltrados)
const comunas = computed(() => comunasMock)
const tiposNee = computed(() => academic.tiposNee)

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

const nombreCompleto = computed(() =>
  `${form.nombres} ${form.apellidoPaterno} ${form.apellidoMaterno}`.replace(/\s+/g, ' ').trim(),
)

const guardarAlumno = () => {
  if (!form.nombres || !form.apellidoPaterno || !form.rut) {
    alert('Completa al menos nombres, apellido paterno y RUT.')
    return
  }

  const nuevoAlumno = {
    ...form,
    nombre: nombreCompleto.value,
    cursoId: form.cursoId ? Number(form.cursoId) : null,
    comunaId: form.comunaId ? Number(form.comunaId) : null,
    tipoNeeId: form.tieneNee && form.tipoNeeId ? Number(form.tipoNeeId) : null,
  }

  academic.agregarAlumno(nuevoAlumno)
  router.push('/admin/alumnos')
}
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h1 class="h3 mb-1">Agregar alumno</h1>
        <p class="text-muted mb-0">Registro básico del estudiante.</p>
      </div>

      <RouterLink to="/admin/alumnos" class="btn btn-outline-info">← Volver al listado </RouterLink>
    </div>

    <form class="card" @submit.prevent="guardarAlumno">
      <div class="card-body">
        <h2 class="h5 mb-3">Datos personales</h2>

        <div class="row g-3 mb-4">
          <div class="col-md-4">
            <label class="form-label">Nombres</label>
            <input v-model="form.nombres" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido paterno</label>
            <input v-model="form.apellidoPaterno" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido materno</label>
            <input v-model="form.apellidoMaterno" type="text" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">RUT</label>
            <input
              v-model="form.rut"
              type="text"
              class="form-control"
              placeholder="12.345.678-9"
              required
            />
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
            <label class="form-label">Fecha de nacimiento</label>
            <input v-model="form.fechaNacimiento" type="date" class="form-control" />
          </div>
        </div>

        <h2 class="h5 mb-3">Contacto y dirección</h2>

        <div class="row g-3 mb-4">
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

          <div class="col-md-2">
            <label class="form-label">Número</label>
            <input v-model="form.numero" type="text" class="form-control" />
          </div>

          <div class="col-md-5">
            <label class="form-label">Comuna</label>
            <select v-model="form.comunaId" class="form-select">
              <option :value="null">Seleccionar comuna</option>
              <option v-for="comuna in comunas" :key="comuna.id" :value="comuna.id">
                {{ comuna.nombre }}
              </option>
            </select>
          </div>
        </div>

        <h2 class="h5 mb-3">Información académica</h2>

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

        <h2 class="h5 mb-3">Indicadores</h2>

        <div class="row g-3">
          <div class="col-md-3">
            <div class="form-check">
              <input
                id="prioritario"
                v-model="form.prioritario"
                class="form-check-input"
                type="checkbox"
              />
              <label class="form-check-label" for="prioritario"> Alumno prioritario </label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input
                id="preferente"
                v-model="form.preferente"
                class="form-check-input"
                type="checkbox"
              />
              <label class="form-check-label" for="preferente"> Alumno preferente </label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input
                id="tieneNee"
                v-model="form.tieneNee"
                class="form-check-input"
                type="checkbox"
              />
              <label class="form-check-label" for="tieneNee"> Tiene NEE </label>
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-check">
              <input id="enPie" v-model="form.enPie" class="form-check-input" type="checkbox" />
              <label class="form-check-label" for="enPie"> Programa PIE </label>
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
        <RouterLink to="/admin/alumnos" class="btn btn-rounded btn-danger"> Cancelar </RouterLink>

        <button type="submit" class="btn btn-success btn-rounded">Guardar alumno</button>
      </div>
    </form>
  </div>
</template>
