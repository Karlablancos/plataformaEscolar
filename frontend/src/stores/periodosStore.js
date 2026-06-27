import { defineStore } from 'pinia'
import api from '@/api/axios'
import { filtrarPeriodosPorModoAula } from './shared/periodosPlantillas'
import { useEstablecimientoStore } from './establecimientoStore'

const resolveEstablecimientoId = () => {
  return (
    JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento ??
    JSON.parse(localStorage.getItem('user') || 'null')?.idEstablecimiento
  )
}

const mapPeriodoFromApi = (dto) => ({
  id: dto.idPeriodo,
  id_periodo: dto.idPeriodo,
  id_establecimiento: dto.idEstablecimiento,
  anio: dto.anio,
  nombre: dto.nombrePeriodo?.trim() ?? '',
  nombrePeriodo: dto.nombrePeriodo?.trim() ?? '',
  nombre_periodo: dto.nombrePeriodo?.trim() ?? '',
  fecha_inicio: dto.fechaInicio ?? null,
  fecha_termino: dto.fechaTermino ?? null,
  estado: dto.estado ?? 'ACTIVO',
})

const toPeriodoPayload = (data) => ({
  nombrePeriodo: String(data.nombre || data.nombre_periodo || '').trim(),
  anio: Number(data.anio),
  fechaInicio: data.fecha_inicio || data.fechaInicio,
  fechaTermino: data.fecha_termino || data.fechaTermino,
  estado: String(data.estado || 'ACTIVO').trim().toUpperCase(),
})

export const usePeriodosStore = defineStore('periodos', {
  state: () => ({
    periodos: [],
    cargando: false,
    error: null,
  }),

  getters: {
    periodosActivos: (state) => {
      const modoAula = useEstablecimientoStore().establecimientoActivo?.modo_aula ?? 'SEMESTRAL'

      const activos = state.periodos.filter((periodo) => {
        const estado = String(periodo.estado || '').toUpperCase()
        return estado === 'ACTIVO' || estado === 'PENDIENTE'
      })

      return filtrarPeriodosPorModoAula(activos, modoAula).sort(
        (a, b) => new Date(a.fecha_inicio) - new Date(b.fecha_inicio),
      )
    },
  },

  actions: {
    async cargarPeriodos(anio) {
      const idEstablecimiento = resolveEstablecimientoId()
      const anioNumber = Number(anio)

      if (!idEstablecimiento || !anioNumber) {
        throw new Error('No se encontró el establecimiento o año académico.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data } = await api.get(`/establecimiento/${idEstablecimiento}/periodos`, {
          params: { anio: anioNumber },
        })
        this.periodos = data.map(mapPeriodoFromApi)
        return this.periodos
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async crearPeriodo(data) {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: creado } = await api.post(
          `/establecimiento/${idEstablecimiento}/periodos`,
          toPeriodoPayload(data),
        )
        const periodo = mapPeriodoFromApi(creado)
        this.periodos = [...this.periodos, periodo].sort(
          (a, b) => new Date(a.fecha_inicio) - new Date(b.fecha_inicio),
        )
        return periodo
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async actualizarPeriodo(id, data) {
      const idEstablecimiento = resolveEstablecimientoId()
      const periodoId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: actualizado } = await api.put(
          `/establecimiento/${idEstablecimiento}/periodos/${periodoId}`,
          toPeriodoPayload(data),
        )
        const periodo = mapPeriodoFromApi(actualizado)
        const index = this.periodos.findIndex((item) => Number(item.id) === periodoId)

        if (index !== -1) {
          const periodos = [...this.periodos]
          periodos[index] = periodo
          this.periodos = periodos.sort(
            (a, b) => new Date(a.fecha_inicio) - new Date(b.fecha_inicio),
          )
        }

        return periodo
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },
  },
})
