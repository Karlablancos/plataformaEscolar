<template>
  <div class="container-fluid py-4">

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <div>
        <h1 class="h3 fw-bold mb-1">
          <i class="bi bi-award me-2" style="color:#1B4F9C"></i>
          Promoción de Alumnos
        </h1>
        <p class="text-muted mb-0">
          Calcula el estado de promoción según Decreto 67/2018.
        </p>
      </div>
    </div>

    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <!-- Filtro -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-12 col-md-5">
            <label class="form-label fw-semibold">Curso</label>
            <select v-model.number="idCurso" class="form-select">
              <option value="">Seleccionar curso</option>
              <option v-for="c in cursos" :key="c.id_curso" :value="c.id_curso">
                {{ c.nombre || `${c.numero}° ${c.letra}` }}
              </option>
            </select>
          </div>
          <div class="col-12 col-md-3">
            <button class="btn btn-primary rounded-pill px-4 w-100"
              :disabled="!idCurso || calculando"
              @click="calcularPromociones">
              <span v-if="calculando" class="spinner-border spinner-border-sm me-2"></span>
              <i v-else class="bi bi-calculator me-2"></i>
              Calcular promociones
            </button>
          </div>
          <div class="col-12 col-md-4">
            <div class="d-flex gap-2" v-if="resultados.length">
              <span class="badge rounded-pill text-bg-success">
                {{ aprobados }} aprobados
              </span>
              <span class="badge rounded-pill text-bg-danger">
                {{ reprobados }} reprobados
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Resultados -->
    <div class="card border-0 shadow-sm rounded-4">
      <div class="card-body">
        <div v-if="!resultados.length && !calculando" class="text-center text-muted py-5">
          <i class="bi bi-people fs-2 d-block mb-2"></i>
          Selecciona un curso y presiona "Calcular promociones".
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>#</th>
                <th>Alumno</th>
                <th>RUT</th>
                <th class="text-center">Estado de Promoción</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(res, i) in resultados" :key="res.idAlumno">
                <td>{{ i + 1 }}</td>
                <td>
                  <strong>{{ res.nombreCompleto }}</strong>
                </td>
                <td class="text-muted">{{ res.rut }}</td>
                <td class="text-center">
                  <span v-if="res.cargando" class="spinner-border spinner-border-sm text-primary" />
                  <span v-else-if="res.estado" class="badge rounded-pill fs-6 px-3"
                    :style="res.estado === 'APROBADO'
                      ? 'background-color:#198754; color:#fff;'
                      : 'background-color:#CC1F2D; color:#fff;'">
                    <i class="bi me-1"
                      :class="res.estado === 'APROBADO' ? 'bi-check-circle' : 'bi-x-circle'"></i>
                    {{ res.estado }}
                  </span>
                  <span v-else class="text-muted small">Sin datos</span>
                </td>
              </tr>
              <tr v-if="resultados.length === 0 && calculando">
                <td colspan="4" class="text-center py-4">
                  <span class="spinner-border spinner-border-sm text-primary me-2"></span>
                  Calculando...
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="resultados.length" class="mt-3 pt-3 border-top">
          <small class="text-muted">
            <i class="bi bi-info-circle me-1"></i>
            Decreto 67/2018: APROBADO si todas las asignaturas ≥ 4.0,
            o promedio general ≥ 4.5 con máximo 1 asignatura reprobada.
          </small>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { useCursosStore } from '@/stores/cursosStore'
import { useAlumnosStore } from '@/stores/alumnosStore'
import api from '@/api/axios'

const authStore = useAuthStore()
const cursosStore = useCursosStore()
const alumnosStore = useAlumnosStore()

const idCurso = ref('')
const calculando = ref(false)
const mensajeError = ref('')
const resultados = ref([])

const cursos = computed(() => cursosStore.cursosFiltradosNormalizados)
const alumnos = computed(() => alumnosStore.alumnosFiltradosNormalizados)

const aprobados = computed(() =>
  resultados.value.filter((r) => r.estado === 'APROBADO').length
)
const reprobados = computed(() =>
  resultados.value.filter((r) => r.estado === 'REPROBADO').length
)

const calcularPromociones = async () => {
  if (!idCurso.value) return
  mensajeError.value = ''

  resultados.value = alumnos.value.map((a) => ({
    idAlumno: a.id_alumno,
    nombreCompleto: a.nombreCompleto,
    rut: a.rut,
    estado: null,
    cargando: true,
  }))

  calculando.value = true

  await Promise.all(
    resultados.value.map(async (res) => {
      try {
        const { data } = await api.get('/notas/promocion', {
          params: { idEstudiante: res.idAlumno },
        })
        res.estado = typeof data === 'string' ? data : data.estado || String(data)
      } catch {
        res.estado = null
      } finally {
        res.cargando = false
      }
    })
  )

  calculando.value = false
}

onMounted(async () => {
  await alumnosStore.cargarAlumnos()
})
</script>
