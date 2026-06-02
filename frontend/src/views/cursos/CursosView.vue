<script setup>
import { computed, reactive, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useAcademicStore } from '@/stores/academicStore'

const academic = useAcademicStore()

const cursos = computed(() => academic.cursosFiltradosNormalizados)

const showModal = ref(false)
const modoEdicion = ref(false)
const cursoEditandoId = ref(null)

const form = reactive({
  numero: '',
  letra: '',
  tipo_ensenanza: '',
  modalidad: 'Regular',
  anio_academico: academic.anioActivo,
  es_nivel_since: false,
  estado: 'Activo',
})

const abrirNuevoCurso = () => {
  modoEdicion.value = false
  cursoEditandoId.value = null

  Object.assign(form, {
    numero: '',
    letra: '',
    tipo_ensenanza: '',
    modalidad: 'Regular',
    anio_academico: academic.anioActivo,
    es_nivel_since: false,
    estado: 'Activo',
  })

  showModal.value = true
}

const abrirEditarCurso = (curso) => {
  modoEdicion.value = true
  cursoEditandoId.value = curso.id

  Object.assign(form, {
    numero: curso.numero || '',
    letra: curso.letra || '',
    tipo_ensenanza: curso.tipo_ensenanza || '',
    modalidad: curso.modalidad || 'Regular',
    anio_academico: curso.anio_academico || curso.anio || academic.anioActivo,
    es_nivel_since: Boolean(curso.es_nivel_since),
    estado: curso.estado || 'Activo',
  })

  showModal.value = true
}

const cerrarModal = () => {
  showModal.value = false
}

const guardarCurso = () => {
  if (!form.numero || !form.letra || !form.tipo_ensenanza) {
    alert('Completa número, letra y tipo de enseñanza.')
    return
  }

  const data = {
    ...form,
    numero: Number(form.numero),
    anio_academico: Number(form.anio_academico),
  }

  if (modoEdicion.value) {
    academic.actualizarCurso(cursoEditandoId.value, data)
  } else {
    academic.agregarCurso(data)
  }

  cerrarModal()
}

const eliminarCurso = (curso) => {
  const confirmar = confirm(`¿Seguro que deseas eliminar el curso ${curso.nombre}?`)

  if (!confirmar) return

  academic.eliminarCurso(curso.id)
}
</script>

<template>
  <div class="p-4">
    <div class="d-flex justify-content-between page-header align-items-center mb-4">
      <div>
        <h1 class="h3 mb-1 page-title"><i class="bi bi-easel"></i> Cursos</h1>
        <p class="text-muted mb-0">
          Administración de cursos del año académico {{ academic.anioActivo }}.
        </p>
      </div>

      <button class="btn btn-success btn-rounded" @click="abrirNuevoCurso">
        <i class="bi bi-plus-circle me-1"></i>
        Nuevo curso
      </button>
    </div>

    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-light">
            <tr>
              <th>Curso</th>
              <th>Tipo enseñanza</th>
              <th>Modalidad</th>
              <th>Año</th>
              <th>SIMCE</th>
              <th>Estado</th>
              <th class="text-end">Acciones</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="curso in cursos" :key="curso.id">
              <td class="fw-semibold">
                {{ curso.nombre }}
              </td>

              <td>{{ curso.tipo_ensenanza }}</td>
              <td>{{ curso.modalidad }}</td>
              <td>{{ curso.anio }}</td>

              <td>
                <span
                  class="badge rounded-pill"
                  :class="curso.es_nivel_since ? 'text-bg-primary' : 'text-bg-secondary'"
                >
                  {{ curso.es_nivel_since ? 'Sí' : 'No' }}
                </span>
              </td>

              <td>
                <span
                  class="badge rounded-pill"
                  :class="curso.estado === 'Activo' ? 'text-bg-success' : 'text-bg-secondary'"
                >
                  {{ curso.estado }}
                </span>
              </td>

              <td class="text-end">
                <div class="d-flex justify-content-end gap-2">
                  <RouterLink
                    :to="`/admin/cursos/${curso.id}`"
                    class="btn btn-primary btn-sm btn-rounded"
                  >
                    Configurar
                  </RouterLink>

                  <button class="btn btn-edit btn-sm btn-rounded" @click="abrirEditarCurso(curso)">
                    Editar
                  </button>

                  <button
                    class="btn btn-outline-danger btn-sm btn-rounded"
                    @click="eliminarCurso(curso)"
                  >
                    Eliminar
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="cursos.length === 0">
              <td colspan="7" class="text-center text-muted py-4">
                No hay cursos registrados para este año académico.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal crear / editar -->
    <div
      v-if="showModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.45)"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <form @submit.prevent="guardarCurso">
            <div class="modal-header">
              <h5 class="modal-title">
                {{ modoEdicion ? 'Editar curso' : 'Nuevo curso' }}
              </h5>

              <button type="button" class="btn-close" @click="cerrarModal"></button>
            </div>

            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-3">
                  <label class="form-label">Número</label>
                  <input
                    v-model="form.numero"
                    type="number"
                    min="1"
                    class="form-control"
                    required
                  />
                </div>

                <div class="col-md-3">
                  <label class="form-label">Letra</label>
                  <input
                    v-model="form.letra"
                    type="text"
                    maxlength="1"
                    class="form-control text-uppercase"
                    required
                  />
                </div>

                <div class="col-md-6">
                  <label class="form-label">Tipo de enseñanza</label>
                  <select v-model="form.tipo_ensenanza" class="form-select" required>
                    <option value="">Seleccionar</option>
                    <option value="Parvularia">Parvularia</option>
                    <option value="Básica">Básica</option>
                    <option value="Media">Media</option>
                    <option value="Técnico Profesional">Técnico Profesional</option>
                    <option value="Especial">Especial</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Modalidad</label>
                  <select v-model="form.modalidad" class="form-select">
                    <option value="Regular">Regular</option>
                    <option value="Humanista-Científica">Humanista-Científica</option>
                    <option value="Técnico Profesional">Técnico Profesional</option>
                    <option value="Adultos">Adultos</option>
                  </select>
                </div>

                <div class="col-md-3">
                  <label class="form-label">Año académico</label>
                  <input
                    v-model="form.anio_academico"
                    type="number"
                    class="form-control"
                    required
                  />
                </div>

                <div class="col-md-3">
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select">
                    <option value="Activo">Activo</option>
                    <option value="Inactivo">Inactivo</option>
                    <option value="Cerrado">Cerrado</option>
                  </select>
                </div>

                <div class="col-12">
                  <div class="form-check">
                    <input
                      id="esNivelSince"
                      v-model="form.es_nivel_since"
                      type="checkbox"
                      class="form-check-input"
                    />

                    <label for="esNivelSince" class="form-check-label"> Es nivel SIMCE </label>
                  </div>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-danger btn-rounded" @click="cerrarModal">
                Cancelar
              </button>

              <button type="submit" class="btn btn-success btn-rounded">Guardar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
