<template>
  <div class="container-fluid py-4">
    <div class="page-header mb-4">
      <div>
        <h1 class="page-title"><i class="bi bi-building"></i> Establecimiento</h1>
        <p class="page-subtitle">Configuración institucional del libro de clases digital.</p>
      </div>

      <button v-if="!modoEdicion" class="btn btn-success btn-rounded" @click="activarEdicion">
        <i class="bi bi-pencil"></i> Editar información
      </button>
    </div>

    <div class="row g-4">
      <!-- SIDEBAR -->
      <div class="col-lg-4">
        <div class="card border-0 shadow-0 mb-4">
          <div class="card-body">
            <h3 class="sidebar-title">Resumen</h3>

            <div class="summary-item">
              <span>RBD</span>
              <strong>{{ form.rbd || 'Sin definir' }}</strong>
            </div>

            <div class="summary-item">
              <span>Modo aula</span>
              <strong>{{ form.modo_aula }}</strong>
            </div>

            <div class="summary-item">
              <span>Estado</span>
              <strong>{{ form.estado }}</strong>
            </div>

            <div class="summary-item">
              <span>Director</span>
              <strong>{{ form.director || 'Sin definir' }}</strong>
            </div>
          </div>
        </div>

        <div class="card border-0 shadow-0">
          <div class="card-body">
            <h3 class="sidebar-title">Configuración académica</h3>

            <div class="alert alert-info border mb-0">
              <i class="bi bi-info-circle"></i> El <strong>modo aula</strong> define la estructura
              temporal del establecimiento (semestral, trimestral o anual).
            </div>
          </div>
        </div>
      </div>
      <!-- INFORMACIÓN PRINCIPAL -->
      <div class="col-lg-8">
        <div class="card border-0 shadow-0" :class="{ 'focus-mode': modoEdicion }">
          <div class="card-header bg-white border-0 pb-0">
            <h2 class="section-title">Datos generales</h2>
          </div>

          <div class="card-body">
            <form @submit.prevent="guardarCambios">
              <div class="row g-3">
                <div class="col-md-3">
                  <label class="form-label">RBD</label>
                  <input
                    v-model="form.rbd"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-9">
                  <label class="form-label">Nombre establecimiento</label>
                  <input
                    v-model="form.nombre"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-6">
                  <label class="form-label">Tipo establecimiento</label>
                  <select v-model="form.id_tipo_estab" class="form-select" :disabled="!modoEdicion">
                    <option :value="1">Municipal</option>
                    <option :value="2">Particular subvencionado</option>
                    <option :value="3">Particular pagado</option>
                    <option :value="4">Servicio Local de Educación</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select" :disabled="!modoEdicion">
                    <option value="Activo">Activo</option>
                    <option value="Inactivo">Inactivo</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Sostenedor</label>
                  <input
                    v-model="form.sostenedor"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-6">
                  <label class="form-label">Director</label>
                  <input
                    v-model="form.director"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-8">
                  <label class="form-label">Calle</label>
                  <input
                    v-model="form.calle"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-4">
                  <label class="form-label">Número</label>
                  <input
                    v-model="form.numero"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-6">
                  <label class="form-label">Comuna</label>
                  <select v-model="form.id_comuna" class="form-select" :disabled="!modoEdicion">
                    <option :value="1">Santiago</option>
                    <option :value="2">Providencia</option>
                    <option :value="3">Ñuñoa</option>
                    <option :value="4">Maipú</option>
                    <option :value="5">La Florida</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Modo aula</label>
                  <select v-model="form.modo_aula" class="form-select" :disabled="!modoEdicion">
                    <option value="Semestral">Semestral</option>
                    <option value="Trimestral">Trimestral</option>
                    <option value="Anual">Anual</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Teléfono</label>
                  <input
                    v-model="form.telefono"
                    type="text"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>

                <div class="col-md-6">
                  <label class="form-label">Correo electrónico</label>
                  <input
                    v-model="form.correo_electronico"
                    type="email"
                    class="form-control"
                    :disabled="!modoEdicion"
                  />
                </div>
              </div>

              <div v-if="modoEdicion" class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-danger btn-rounded" @click="cancelarEdicion">
                  Cancelar
                </button>

                <button type="submit" class="btn btn-success btn-rounded">Guardar cambios</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAcademicStore } from '@/stores/academicStore'

const academicStore = useAcademicStore()

const modoEdicion = ref(false)

const getEstablecimiento = () => {
  return academicStore.establecimientoActivo || academicStore.establecimiento || {}
}

const form = reactive({
  id_establecimiento: null,
  rbd: '',
  nombre: '',
  id_tipo_estab: 1,
  sostenedor: '',
  director: '',
  calle: '',
  numero: '',
  id_comuna: 1,
  telefono: '',
  correo_electronico: '',
  modo_aula: 'Semestral',
  estado: 'Activo',
  ...getEstablecimiento(),
})

const activarEdicion = () => {
  modoEdicion.value = true
}

const cancelarEdicion = () => {
  Object.assign(form, getEstablecimiento())
  modoEdicion.value = false
}

const guardarCambios = () => {
  academicStore.actualizarEstablecimiento({ ...form })
  modoEdicion.value = false
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
}

.page-subtitle {
  color: #6c757d;
  margin: 0;
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
}

.sidebar-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 0.8rem 0;
  border-bottom: 1px solid #f1f3f5;
}

.summary-item:last-child {
  border-bottom: none;
}

.btn-rounded {
  border-radius: 999px;
}
</style>
