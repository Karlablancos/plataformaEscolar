<!-- src/views/profesores/ProfesoresView.vue -->

<template>
  <div class="container py-4">
    <div class="page-header mb-4">
      <div>
        <h1 class="page-title"><i class="bi bi-person-workspace"></i> Profesores</h1>
        <p class="text-muted mb-0">Gestión de docentes del establecimiento.</p>
      </div>

      <RouterLink to="/admin/profesores/nuevo" class="btn btn-success btn-rounded">
        + Nuevo Profesor
      </RouterLink>
    </div>

    <div class="card">
      <div v-if="cargando" class="card-body text-center py-4">
        <span class="spinner-border spinner-border-sm text-primary me-2" />
        Cargando profesores...
      </div>

      <div v-else class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-light">
            <tr>
              <th>RUT</th>
              <th>Nombre</th>
              <th>Correo</th>
              <th>Teléfono</th>
              <th>Estado</th>
              <th class="text-end">Acciones</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="profesor in profesores" :key="profesor.id_docente">
              <td>{{ profesor.rut }}-{{ profesor.dv }}</td>
              <td>
                {{ profesor.nombres }}
                {{ profesor.apellido_paterno }}
                {{ profesor.apellido_materno }}
              </td>
              <td>{{ profesor.correo_electronico }}</td>
              <td>{{ profesor.telefono }}</td>
              <td>
                <span
                  class="badge rounded-pill"
                  :class="profesor.estado === 'Activo' ? 'text-bg-success' : 'text-bg-secondary'"
                >
                  {{ profesor.estado }}
                </span>
              </td>
              <td class="text-end">
                <RouterLink
                  :to="`/admin/profesores/${profesor.id_docente}`"
                  class="btn btn-sm btn-outline-primary me-2"
                >
                  Ver
                </RouterLink>

                <RouterLink
                  :to="`/admin/profesores/${profesor.id_docente}/editar`"
                  class="btn btn-sm btn-outline-secondary"
                >
                  Editar
                </RouterLink>
              </td>
            </tr>

            <tr v-if="profesores.length === 0">
              <td colspan="6" class="text-center text-muted py-4">
                No hay profesores registrados.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'
import { useDocentesStore } from '@/stores/docentesStore'

const academicStore = useAcademicStore()
const docentesStore = useDocentesStore()

const cargando = computed(() => docentesStore.cargando)

onMounted(() => {
  academicStore.cargarDocentes().catch(() => {})
})

const profesores = computed(() => academicStore.profesoresFiltrados)
</script>
