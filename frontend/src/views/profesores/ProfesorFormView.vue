<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const academicStore = useAcademicStore()

const profesorId = computed(() => Number(route.params.id))
const esEdicion = computed(() => Boolean(route.params.id))

const comunas = ref([])
const categoriasSned = computed(() => academicStore.categoriasSned)

const formInicial = {
  id_usuario: null,
  id_categoria_sned: null,
  anio_evaluacion_sned: new Date().getFullYear(),

  rut: '',
  dv: '',
  nombres: '',
  apellido_paterno: '',
  apellido_materno: '',
  fecha_nacimiento: '',
  correo_electronico: '',
  telefono: '',

  calle: '',
  numero: '',
  id_comuna: null,

  fecha_contratacion: '',
  estado: 'Activo',
}

const form = reactive({ ...formInicial })

watch(
  () => route.path,
  async () => {
    try {
      const { data } = await api.get('/establecimiento/comunas')
      comunas.value = Array.isArray(data) ? data : []
    } catch (error) {
      console.error('Error al cargar comunas:', error)
    }

    Object.assign(form, formInicial)

    if (!esEdicion.value) return

    const profesor = academicStore.getProfesorById(profesorId.value)

    if (!profesor) {
      router.push('/admin/profesores')
      return
    }

    Object.assign(form, profesor)
  },
  { immediate: true },
)

const guardar = () => {
  if (esEdicion.value) {
    academicStore.actualizarDocente(profesorId.value, { ...form })
  } else {
    academicStore.agregarDocente({ ...form })
  }

  router.push('/admin/profesores')
}
</script>

<template>
  <div class="container py-4">
    <div class="mb-4">
      <h1 class="h3 mb-1">
        {{ esEdicion ? 'Editar profesor' : 'Nuevo profesor' }}
      </h1>
      <p class="text-muted mb-0">Completa los datos personales y laborales del docente.</p>
    </div>

    <form class="card border-0" @submit.prevent="guardar">
      <div class="card-body">
        <h2 class="h5 mb-3">Datos personales</h2>

        <div class="row g-3 mb-4">
          <div class="col-md-3">
            <label class="form-label">RUT</label>
            <input v-model="form.rut" type="text" class="form-control" required />
          </div>

          <div class="col-md-2">
            <label class="form-label">DV</label>
            <input v-model="form.dv" type="text" class="form-control" required />
          </div>

          <div class="col-md-7">
            <label class="form-label">Nombres</label>
            <input v-model="form.nombres" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido paterno</label>
            <input v-model="form.apellido_paterno" type="text" class="form-control" required />
          </div>

          <div class="col-md-4">
            <label class="form-label">Apellido materno</label>
            <input v-model="form.apellido_materno" type="text" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">Fecha nacimiento</label>
            <input v-model="form.fecha_nacimiento" type="date" class="form-control" />
          </div>
        </div>

        <h2 class="h5 mb-3">Contacto</h2>

        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <label class="form-label">Correo electrónico</label>
            <input v-model="form.correo_electronico" type="email" class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Teléfono</label>
            <input v-model="form.telefono" type="text" class="form-control" />
          </div>
        </div>

        <h2 class="h5 mb-3">Dirección</h2>

        <div class="row g-3 mb-4">
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
            <select v-model.number="form.id_comuna" class="form-select">
              <option :value="null">Seleccionar comuna</option>
              <option v-for="comuna in comunas" :key="comuna.idComuna" :value="comuna.idComuna">
                {{ comuna.nombreComuna }}
              </option>
            </select>
          </div>
        </div>

        <h2 class="h5 mb-3">Datos laborales</h2>

        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Fecha contratación</label>
            <input v-model="form.fecha_contratacion" type="date" class="form-control" />
          </div>

          <div class="col-md-4">
            <label class="form-label">Categoría SNED</label>
            <select v-model.number="form.id_categoria_sned" class="form-select">
              <option :value="null">Sin categoría</option>
              <option
                v-for="categoria in categoriasSned"
                :key="categoria.id_categoria_sned"
                :value="categoria.id_categoria_sned"
              >
                {{ categoria.nombre }}
              </option>
            </select>
          </div>

          <div class="col-md-4">
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-select">
              <option value="Activo">Activo</option>
              <option value="Inactivo">Inactivo</option>
            </select>
          </div>
        </div>
      </div>

      <div class="card-footer bg-white pt-3 mt-3 d-flex justify-content-end gap-2">
        <RouterLink to="/admin/profesores" class="btn btn-rounded btn-danger">
          Cancelar
        </RouterLink>

        <button type="submit" class="btn btn-success btn-rounded">Guardar profesor</button>
      </div>
    </form>
  </div>
</template>
