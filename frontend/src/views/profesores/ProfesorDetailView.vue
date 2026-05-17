<template>
  <div class="container py-4">
    <div v-if="profesor">
      <div class="d-flex justify-content-between align-items-start mb-4">
        <div>
          <h1 class="h3 mb-1">
            {{ profesor.nombres }}
            {{ profesor.apellido_paterno }}
            {{ profesor.apellido_materno }}
          </h1>

          <p class="text-muted mb-0">RUT {{ profesor.rut }}-{{ profesor.dv }}</p>
        </div>

        <div class="d-flex gap-2">
          <RouterLink to="/admin/profesores" class="btn btn-outline-secondary"> Volver </RouterLink>

          <RouterLink
            :to="`/admin/profesores/${profesor.id_docente}/editar`"
            class="btn btn-primary"
          >
            Editar
          </RouterLink>
        </div>
      </div>

      <ul class="nav nav-tabs mb-3">
        <li class="nav-item">
          <button
            class="nav-link"
            :class="{ active: tabActiva === 'datos' }"
            @click="tabActiva = 'datos'"
          >
            Datos generales
          </button>
        </li>

        <li class="nav-item">
          <button
            class="nav-link"
            :class="{ active: tabActiva === 'academico' }"
            @click="tabActiva = 'academico'"
          >
            Carga académica
          </button>
        </li>
      </ul>

      <div v-if="tabActiva === 'datos'" class="card">
        <div class="card-body">
          <h2 class="h5 mb-3">Información personal</h2>

          <div class="row g-3">
            <div class="col-md-4">
              <strong>Correo</strong>
              <p class="mb-0">{{ profesor.correo_electronico || 'No registrado' }}</p>
            </div>

            <div class="col-md-4">
              <strong>Teléfono</strong>
              <p class="mb-0">{{ profesor.telefono || 'No registrado' }}</p>
            </div>

            <div class="col-md-4">
              <strong>Fecha nacimiento</strong>
              <p class="mb-0">{{ profesor.fecha_nacimiento || 'No registrada' }}</p>
            </div>

            <div class="col-md-4">
              <strong>Dirección</strong>
              <p class="mb-0">
                {{ profesor.calle || 'Sin calle' }}
                {{ profesor.numero || '' }}
              </p>
            </div>

            <div class="col-md-4">
              <strong>Comuna</strong>
              <p class="mb-0">{{ comunaNombre }}</p>
            </div>

            <div class="col-md-4">
              <strong>Estado</strong>
              <p class="mb-0">
                <span
                  class="badge"
                  :class="profesor.estado === 'Activo' ? 'text-bg-success' : 'text-bg-secondary'"
                >
                  {{ profesor.estado }}
                </span>
              </p>
            </div>
          </div>

          <hr />

          <h2 class="h5 mb-3">Información laboral</h2>

          <div class="row g-3">
            <div class="col-md-4">
              <strong>Fecha contratación</strong>
              <p class="mb-0">{{ profesor.fecha_contratacion || 'No registrada' }}</p>
            </div>

            <div class="col-md-4">
              <strong>Categoría SNED</strong>
              <p class="mb-0">{{ categoriaSnedNombre }}</p>
            </div>

            <div class="col-md-4">
              <strong>Año evaluación SNED</strong>
              <p class="mb-0">{{ profesor.anio_evaluacion_sned || 'No registrado' }}</p>
            </div>
          </div>
        </div>
      </div>

      <div v-if="tabActiva === 'academico'" class="card">
        <div class="card-body">
          <h2 class="h5 mb-3">Carga académica</h2>

          <p class="text-muted mb-0">
            Aquí luego mostraremos las asignaturas y cursos asociados al profesor.
          </p>
        </div>
      </div>
    </div>

    <div v-else class="alert alert-warning">Profesor no encontrado.</div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, RouterLink } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import { comunasMock } from '@/data'

const route = useRoute()
const academicStore = useAcademicStore()

const tabActiva = ref('datos')

const profesorId = computed(() => Number(route.params.id))

const profesor = computed(() => academicStore.getProfesorById(profesorId.value))

const comunaNombre = computed(() => {
  const comuna = comunasMock.find((item) => item.id === profesor.value?.id_comuna)
  return comuna?.nombre || 'No registrada'
})

const categoriaSnedNombre = computed(() => {
  const categoria = academicStore.getCategoriaSnedById(profesor.value?.id_categoria_sned)
  return categoria?.nombre || 'Sin categoría'
})
</script>
