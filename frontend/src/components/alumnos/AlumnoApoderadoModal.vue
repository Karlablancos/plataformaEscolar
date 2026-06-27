<script setup>
import { reactive, ref, onMounted } from 'vue'
import api from '@/api/axios'

const comunas = ref([])

onMounted(async () => {
  const { data } = await api.get('/establecimiento/comunas')
  comunas.value = data
})

const emit = defineEmits(['guardar'])

const props = defineProps({
  modalId: {
    type: String,
    default: 'modalAgregarApoderado',
  },
})

const form = reactive({
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

function limpiarFormulario() {
  Object.assign(form, {
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

function guardar() {
  if (!form.nombres || !form.apellidoPaterno || !form.telefonoPrincipal) {
    alert('Completa al menos nombres, apellido paterno y teléfono principal.')
    return
  }

  const nombreCompleto = `${form.nombres} ${form.apellidoPaterno} ${form.apellidoMaterno}`
    .replace(/\s+/g, ' ')
    .trim()

  emit('guardar', {
    apoderado: {
      rut: form.rut,
      nombres: form.nombres,
      apellidoPaterno: form.apellidoPaterno,
      apellidoMaterno: form.apellidoMaterno,
      nombre: nombreCompleto,
      correoElectronico: form.correoElectronico,
      telefonoPrincipal: form.telefonoPrincipal,
      telefonoSecundario: form.telefonoSecundario,
      calle: form.calle,
      numero: form.numero,
      comuna: form.comuna,
    },
    relacion: {
      parentesco: form.parentesco,
      esApoderadoTitular: form.esApoderadoTitular,
      viveConEstudiante: form.viveConEstudiante,
    },
  })

  limpiarFormulario()

  const modalElement = document.getElementById(props.modalId)

  if (modalElement && window.bootstrap) {
    const modal = window.bootstrap.Modal.getInstance(modalElement)
    modal?.hide()
  }
}
</script>

<template>
  <div
    class="modal fade"
    :id="modalId"
    tabindex="-1"
    :aria-labelledby="`${modalId}Label`"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
      <div class="modal-content">
        <form @submit.prevent="guardar">
          <div class="modal-header">
            <h5 class="modal-title" :id="`${modalId}Label`">Agregar apoderado</h5>

            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Cerrar"
            ></button>
          </div>

          <div class="modal-body">
            <div class="row g-3">
              <div class="col-md-4">
                <label class="form-label">RUT</label>
                <input v-model="form.rut" type="text" class="form-control" />
              </div>

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
                <label class="form-label">Parentesco</label>
                <select v-model="form.parentesco" class="form-select">
                  <option value="">Seleccionar</option>
                  <option value="Madre">Madre</option>
                  <option value="Padre">Padre</option>
                  <option value="Abuelo/a">Abuelo/a</option>
                  <option value="Tutor legal">Tutor legal</option>
                  <option value="Hermano/a">Hermano/a</option>
                  <option value="Otro">Otro</option>
                </select>
              </div>

              <div class="col-md-4">
                <label class="form-label">Teléfono principal</label>
                <input v-model="form.telefonoPrincipal" type="text" class="form-control" />
              </div>

              <div class="col-md-6">
                <label class="form-label">Correo electrónico</label>
                <input v-model="form.correoElectronico" type="email" class="form-control" />
              </div>

              <div class="col-md-6">
                <label class="form-label">Teléfono secundario</label>
                <input v-model="form.telefonoSecundario" type="text" class="form-control" />
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
                <select v-model="form.comuna" class="form-select">
                  <option value="">Seleccionar comuna</option>

                  <option v-for="comuna in comunas" :key="comuna.idComuna" :value="comuna.idComuna">
                    {{ comuna.nombreComuna }}
                  </option>
                </select>
              </div>

              <div class="col-12">
                <div class="form-check">
                  <input
                    id="nuevoApoderadoTitular"
                    v-model="form.esApoderadoTitular"
                    type="checkbox"
                    class="form-check-input"
                  />
                  <label for="nuevoApoderadoTitular" class="form-check-label">
                    Marcar como apoderado titular
                  </label>
                </div>

                <div class="form-check mt-2">
                  <input
                    id="nuevoApoderadoVive"
                    v-model="form.viveConEstudiante"
                    type="checkbox"
                    class="form-check-input"
                  />
                  <label for="nuevoApoderadoVive" class="form-check-label">
                    Vive con el estudiante
                  </label>
                </div>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn btn-rounded btn-danger" data-bs-dismiss="modal">
              Cancelar
            </button>

            <button type="submit" class="btn btn-rounded btn-success">Guardar apoderado</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
