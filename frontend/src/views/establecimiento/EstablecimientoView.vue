<template>
  <div class="container-fluid py-4">
    <div class="page-header mb-4">
      <div>
        <h1 class="page-title"><i class="bi bi-building"></i> Establecimiento</h1>
        <p class="page-subtitle">Configuración institucional del libro de clases digital.</p>
      </div>

      <button
        v-if="!modoEdicion"
        class="btn btn-success btn-rounded"
        @click="activarEdicion"
      >
        <i class="bi bi-pencil"></i> Editar información
      </button>
    </div>

    <div v-if="loading" class="alert alert-info">
      <i class="bi bi-hourglass-split"></i> Cargando información del establecimiento...
    </div>

    <div v-else class="row g-4">
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
              <strong>{{ form.modo_aula || 'No informado' }}</strong>
            </div>

            <div class="summary-item">
              <span>Estado</span>
              <strong>{{ form.estado || 'No informado' }}</strong>
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
              <i class="bi bi-info-circle"></i>
              El <strong>modo aula</strong> define la estructura temporal del
              establecimiento (semestral, trimestral o anual).
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
                    disabled
                    title="El RBD no puede modificarse"
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
                  <select
                    v-model="form.id_tipo_estab"
                    class="form-select"
                    :disabled="!modoEdicion"
                  >
                    <option :value="1">Municipal</option>
                    <option :value="2">Particular subvencionado</option>
                    <option :value="3">Particular pagado</option>
                    <option :value="4">Servicio Local de Educación</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Estado</label>
                  <select
                    v-model="form.estado"
                    class="form-select"
                    :disabled="!modoEdicion"
                  >
                    <option value="ACTIVO">Activo</option>
                    <option value="INACTIVO">Inactivo</option>
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
                  <select
                    v-model="form.id_comuna"
                    class="form-select"
                    :disabled="!modoEdicion"
                  >
                    <option :value="1">Santiago</option>
                    <option :value="2">Providencia</option>
                    <option :value="3">Ñuñoa</option>
                    <option :value="4">Maipú</option>
                    <option :value="5">La Florida</option>
                  </select>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Modo aula</label>
                  <select
                    v-model="form.modo_aula"
                    class="form-select"
                    :disabled="!modoEdicion"
                  >
                    <option value="NORMAL">Normal</option>
                    <option value="SEMESTRAL">Semestral</option>
                    <option value="TRIMESTRAL">Trimestral</option>
                    <option value="ANUAL">Anual</option>
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
                <button
                  type="button"
                  class="btn btn-danger btn-rounded"
                  @click="cancelarEdicion"
                >
                  Cancelar
                </button>

                <button type="submit" class="btn btn-success btn-rounded">
                  Guardar cambios
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-4 mt-1">
      <div class="col-12">
        <div class="card border-0 shadow-0">
          <div class="card-header bg-white border-0 pb-0">
            <div class="d-flex flex-wrap justify-content-between align-items-start gap-3">
              <div>
                <h2 class="section-title mb-1">Periodos académicos</h2>
                <p class="text-muted mb-0 small">
                  Según el modo aula <strong>{{ etiquetaModoAula }}</strong>, configura las fechas de
                  cada periodo del año escolar.
                </p>
              </div>

              <div style="min-width: 140px">
                <label class="form-label mb-1">Año académico</label>
                <input
                  v-model.number="anioPeriodos"
                  type="number"
                  class="form-control"
                  min="2000"
                  max="2100"
                  @change="cargarPeriodosAnio"
                />
              </div>
            </div>
          </div>

          <div class="card-body">
            <div v-if="errorPeriodos" class="alert alert-danger">{{ errorPeriodos }}</div>

            <div v-if="periodosStore.cargando && periodosCards.length === 0" class="text-muted">
              Cargando periodos...
            </div>

            <div v-else class="row g-4">
              <div
                v-for="card in periodosCards"
                :key="card.key"
                :class="columnaPeriodoClass"
              >
                <div class="card periodo-card h-100 border shadow-sm">
                  <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                      <div>
                        <h3 class="h6 fw-bold mb-1">{{ card.nombre }}</h3>
                        <span class="badge" :class="badgeEstadoClass(estadoCard(card))">
                          {{ estadoCard(card) }}
                        </span>
                      </div>
                      <i class="bi bi-calendar-range text-primary fs-4"></i>
                    </div>

                    <div class="row g-3 flex-grow-1">
                      <div class="col-12">
                        <label class="form-label">Fecha inicio</label>
                        <input
                          v-model="card.fecha_inicio"
                          type="date"
                          class="form-control"
                          :disabled="!card.editando"
                          @change="onFechaPeriodoChange(card)"
                        />
                      </div>

                      <div class="col-12">
                        <label class="form-label">Fecha término</label>
                        <input
                          v-model="card.fecha_termino"
                          type="date"
                          class="form-control"
                          :disabled="!card.editando"
                          @change="onFechaPeriodoChange(card)"
                        />
                      </div>
                    </div>

                    <div class="d-flex justify-content-end mt-4">
                      <button
                        type="button"
                        class="btn btn-rounded"
                        :class="card.dirty ? 'btn-success' : 'btn-primary'"
                        :disabled="card.guardando"
                        @click="accionPeriodo(card)"
                      >
                        {{
                          card.guardando
                            ? 'Guardando...'
                            : card.dirty
                              ? 'Guardar'
                              : 'Editar'
                        }}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, reactive, ref, onMounted } from 'vue'
import { useEstablecimientoStore } from '@/stores/establecimientoStore'
import { usePeriodosStore } from '@/stores/periodosStore'
import { useToastStore } from '@/stores/toastStore'
import {
  calcularEstadoPeriodo,
  fechasDefectoPeriodo,
  plantillasPorModoAula,
} from '@/stores/shared/periodosPlantillas'

const establecimientoStore = useEstablecimientoStore()
const periodosStore = usePeriodosStore()
const toastStore = useToastStore()

const modoEdicion = ref(false)
const loading = ref(false)
const anioPeriodos = ref(Number(localStorage.getItem('anioActivo')) || new Date().getFullYear())
const errorPeriodos = ref('')
const periodosCards = ref([])

const getEstablecimiento = () => {
  return establecimientoStore.establecimientoActivo || {}
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
  modo_aula: 'NORMAL',
  estado: 'ACTIVO',
})

const etiquetaModoAula = computed(() => {
  const modo = String(form.modo_aula || 'SEMESTRAL').toUpperCase()
  if (modo === 'TRIMESTRAL') return 'trimestral (3 periodos)'
  if (modo === 'ANUAL' || modo === 'NORMAL') return 'anual (1 periodo)'
  return 'semestral (2 periodos)'
})

const columnaPeriodoClass = computed(() => {
  const total = periodosCards.value.length
  if (total === 1) return 'col-12 col-lg-6'
  if (total === 3) return 'col-12 col-md-6 col-xl-4'
  return 'col-12 col-md-6'
})

onMounted(async () => {
  if (!establecimientoStore.establecimientoActivo) {
    const rbd = localStorage.getItem('rbd')

    if (rbd) {
      loading.value = true
      await establecimientoStore.cargarPorRbd(rbd)
      loading.value = false
    }
  }

  Object.assign(form, getEstablecimiento())
  await cargarPeriodosAnio()
})

const toInputFecha = (fecha) => {
  if (!fecha) return ''
  return String(fecha).slice(0, 10)
}

const normalizarNombre = (nombre) =>
  String(nombre || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim()

const badgeEstadoClass = (estado) => {
  const valor = String(estado || '').toUpperCase()
  if (valor === 'ACTIVO') return 'text-bg-success'
  if (valor === 'PENDIENTE') return 'text-bg-warning'
  if (valor === 'CERRADO') return 'text-bg-secondary'
  return 'text-bg-secondary'
}

const estadoCard = (card) => {
  if (card.editando && card.fecha_inicio && card.fecha_termino) {
    return calcularEstadoPeriodo(card.fecha_inicio, card.fecha_termino)
  }
  return card.estado
}

const crearCardPeriodo = (plantilla, index, periodo) => {
  const fechaInicio = toInputFecha(periodo?.fecha_inicio)
  const fechaTermino = toInputFecha(periodo?.fecha_termino)
  const estado = periodo?.estado || calcularEstadoPeriodo(fechaInicio, fechaTermino)

  return reactive({
    key: `periodo-${index}-${plantilla.nombre}`,
    id: periodo?.id ?? null,
    nombre: plantilla.nombre,
    anio: anioPeriodos.value,
    fecha_inicio: fechaInicio,
    fecha_termino: fechaTermino,
    estado,
    editando: false,
    guardando: false,
    dirty: false,
    snapshot: {
      fecha_inicio: fechaInicio,
      fecha_termino: fechaTermino,
    },
  })
}

const iniciarEdicionPeriodo = (card) => {
  card.editando = true
  card.dirty = false
  card.snapshot = {
    fecha_inicio: card.fecha_inicio,
    fecha_termino: card.fecha_termino,
  }
}

const onFechaPeriodoChange = async (card) => {
  await nextTick()

  if (!card.editando) {
    iniciarEdicionPeriodo(card)
  }

  card.dirty =
    card.fecha_inicio !== card.snapshot.fecha_inicio ||
    card.fecha_termino !== card.snapshot.fecha_termino
}

const finalizarEdicionPeriodo = (card, periodoActualizado = null) => {
  if (periodoActualizado) {
    card.estado = periodoActualizado.estado
    card.fecha_inicio = toInputFecha(periodoActualizado.fecha_inicio)
    card.fecha_termino = toInputFecha(periodoActualizado.fecha_termino)
  }

  card.editando = false
  card.dirty = false
  card.snapshot = {
    fecha_inicio: card.fecha_inicio,
    fecha_termino: card.fecha_termino,
  }
}

const construirCardsDesdeApi = (plantillas) => {
  return plantillas.map((plantilla, index) => {
    const periodo =
      periodosStore.periodos.find(
        (item) => normalizarNombre(item.nombre) === normalizarNombre(plantilla.nombre),
      ) || periodosStore.periodos[index]

    return crearCardPeriodo(plantilla, index, periodo)
  })
}

const asegurarPeriodosSegunModo = async () => {
  const plantillas = plantillasPorModoAula(form.modo_aula)

  for (let index = 0; index < plantillas.length; index++) {
    const plantilla = plantillas[index]
    const existe = periodosStore.periodos.some(
      (item) => normalizarNombre(item.nombre) === normalizarNombre(plantilla.nombre),
    )

    if (!existe) {
      const fechas = fechasDefectoPeriodo(anioPeriodos.value, form.modo_aula, index)
      await periodosStore.crearPeriodo({
        nombre: plantilla.nombre,
        anio: anioPeriodos.value,
        ...fechas,
      })
    }
  }

  await periodosStore.cargarPeriodos(anioPeriodos.value)
  periodosCards.value = construirCardsDesdeApi(plantillas)
}

const cargarPeriodosAnio = async () => {
  errorPeriodos.value = ''
  periodosCards.value = []

  try {
    await periodosStore.cargarPeriodos(anioPeriodos.value)
    await asegurarPeriodosSegunModo()
  } catch (error) {
    errorPeriodos.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudieron cargar los periodos académicos.'
  }
}

const accionPeriodo = async (card) => {
  if (card.guardando) return

  if (!card.editando) {
    iniciarEdicionPeriodo(card)
    return
  }

  if (!card.dirty) {
    finalizarEdicionPeriodo(card)
    return
  }

  if (!card.fecha_inicio || !card.fecha_termino) {
    errorPeriodos.value = 'Debes indicar fecha de inicio y término.'
    return
  }

  if (card.fecha_termino < card.fecha_inicio) {
    errorPeriodos.value = 'La fecha de término debe ser posterior a la de inicio.'
    return
  }

  errorPeriodos.value = ''
  card.guardando = true

  try {
    const payload = {
      nombre: card.nombre,
      anio: card.anio,
      fecha_inicio: card.fecha_inicio,
      fecha_termino: card.fecha_termino,
    }

    let guardado
    if (card.id) {
      guardado = await periodosStore.actualizarPeriodo(card.id, payload)
    } else {
      guardado = await periodosStore.crearPeriodo(payload)
      card.id = guardado.id
    }

    finalizarEdicionPeriodo(card, guardado)
    toastStore.show('Periodo actualizado exitosamente')
  } catch (error) {
    errorPeriodos.value =
      error?.response?.data?.mensaje ||
      error?.message ||
      'No se pudo guardar el periodo académico.'
  } finally {
    card.guardando = false
  }
}

const activarEdicion = () => {
  modoEdicion.value = true
}

const cancelarEdicion = () => {
  Object.assign(form, getEstablecimiento())
  modoEdicion.value = false
}

const guardarCambios = async () => {
  establecimientoStore.actualizarEstablecimiento({ ...form })
  modoEdicion.value = false
  await cargarPeriodosAnio()
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

.periodo-card {
  transition: box-shadow 0.2s ease;
}

.periodo-card:hover {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.08) !important;
}

.periodo-card input[type='date']:disabled {
  background-color: #f8f9fa;
  color: #495057;
}
</style>