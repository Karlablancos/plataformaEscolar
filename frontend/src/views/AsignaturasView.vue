<template>
  <div class="d-flex justify-content-between align-items-center mb-4">
    <div>
      <h1 class="fw-bold mb-1">Gestión de asignaturas</h1>
      <p class="text-muted mb-0">Crea y administra asignaturas del establecimiento.</p>
    </div>
  </div>

  <div class="row g-4">
    <div class="col-lg-4">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <h2 class="h5 fw-bold mb-3">Nueva asignatura</h2>

          <form @submit.prevent="agregarAsignatura">
            <div class="mb-3">
              <label class="form-label">Nombre</label>
              <input
                v-model="nueva.nombre"
                class="form-control"
                placeholder="Matemática"
                required
              />
            </div>

            <div class="mb-3">
              <label class="form-label">Código</label>
              <input v-model="nueva.codigo" class="form-control" placeholder="MAT" required />
            </div>

            <button class="btn btn-primary w-100" type="submit">Crear asignatura</button>
          </form>
        </div>
      </div>
    </div>

    <div class="col-lg-8">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="h5 fw-bold mb-0">Listado de asignaturas</h2>
            <span class="badge text-bg-primary"> {{ asignaturas.length }} asignaturas </span>
          </div>

          <div class="table-responsive">
            <table class="table align-middle">
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Código</th>
                  <th class="text-end">Acciones</th>
                </tr>
              </thead>

              <tbody>
                <tr v-for="asignatura in asignaturas" :key="asignatura.id">
                  <td class="fw-semibold">{{ asignatura.nombre }}</td>
                  <td>{{ asignatura.codigo }}</td>
                  <td class="text-end">
                    <button
                      class="btn btn-sm btn-outline-danger"
                      @click="eliminarAsignatura(asignatura.id)"
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>

                <tr v-if="asignaturas.length === 0">
                  <td colspan="3" class="text-center text-muted py-4">
                    No hay asignaturas registradas.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useAcademicStore } from '../stores/academicStore'

const academic = useAcademicStore()

const asignaturas = computed(() => academic.asignaturasFiltradas)

const nueva = ref({
  nombre: '',
  codigo: '',
})

const agregarAsignatura = () => {
  academic.agregarAsignatura({
    nombre: nueva.value.nombre,
    codigo: nueva.value.codigo.toUpperCase(),
  })

  nueva.value = {
    nombre: '',
    codigo: '',
  }
}

const eliminarAsignatura = (id) => {
  academic.eliminarAsignatura(id)
}
</script>
