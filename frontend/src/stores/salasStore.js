import { defineStore } from 'pinia'
import api from '@/api/axios'
import { getEstablecimientoId } from './shared/helpers'

const resolveEstablecimientoId = () => {
  return (
    getEstablecimientoId() ??
    JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento
  )
}

const mapEstadoFromApi = (estado) => {
  if (!estado) return 'ACTIVO'
  const normalizado = String(estado).trim().toUpperCase()
  return normalizado.startsWith('INACTIV') ? 'INACTIVO' : 'ACTIVO'
}

const mapSalaFromApi = (dto) => ({
  id: dto.idSala,
  id_sala: dto.idSala,
  id_establecimiento: Number(dto.idEstablecimiento),
  establecimientoId: Number(dto.idEstablecimiento),
  numero: dto.numero ?? null,
  nombre: dto.nombre?.trim() ?? '',
  capacidad: dto.capacidad,
  tipo: dto.tipo?.trim() ?? '',
  piso: dto.piso,
  estado: mapEstadoFromApi(dto.estado),
})

const toSalaPayload = (data) => ({
  numero: data.numero ? Number(data.numero) : null,
  nombre: data.nombre?.trim() ?? '',
  capacidad: Number(data.capacidad),
  tipo: data.tipo?.trim().toUpperCase() ?? '',
  piso: Number(data.piso),
  estado: data.estado ?? 'ACTIVO',
})

const getSalaId = (sala) => sala.id_sala ?? sala.id

export const useSalasStore = defineStore('salas', {
  state: () => ({
    salas: [],
    cargando: false,
    error: null,
  }),

  getters: {
    salasFiltradasNormalizadas: (state) => {
      const establecimientoId = resolveEstablecimientoId()

      return state.salas
        .filter((sala) => Number(sala.id_establecimiento) === Number(establecimientoId))
        .map((sala) => ({
          ...sala,
          id: getSalaId(sala),
          id_sala: getSalaId(sala),
        }))
    },

    getSalaById: (state) => {
      return (id) => {
        const salaId = Number(id)
        return state.salas.find((sala) => getSalaId(sala) === salaId)
      }
    },
  },

  actions: {
    async cargarSalas() {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) return

      this.cargando = true
      this.error = null

      try {
        const { data } = await api.get(`/establecimiento/${idEstablecimiento}/salas`)
        this.salas = data.map(mapSalaFromApi)
      } catch (error) {
        this.error = error
        console.warn('cargarSalas: error al cargar desde API', error?.message)
        throw error
      } finally {
        this.cargando = false
      }
    },

    async agregarSala(data) {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: creada } = await api.post(
          `/establecimiento/${idEstablecimiento}/salas`,
          toSalaPayload(data),
        )
        const sala = mapSalaFromApi(creada)
        this.salas.push(sala)
        return sala
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async actualizarSala(id, data) {
      const idEstablecimiento = resolveEstablecimientoId()
      const salaId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: actualizada } = await api.put(
          `/establecimiento/${idEstablecimiento}/salas/${salaId}`,
          toSalaPayload(data),
        )

        const sala = mapSalaFromApi(actualizada)
        const index = this.salas.findIndex((item) => getSalaId(item) === salaId)

        if (index !== -1) {
          this.salas[index] = sala
        }

        return sala
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async eliminarSala(id) {
      const idEstablecimiento = resolveEstablecimientoId()
      const salaId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.delete(`/establecimiento/${idEstablecimiento}/salas/${salaId}`)
        this.salas = this.salas.filter((sala) => getSalaId(sala) !== salaId)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    resetData() {
      this.salas = []
    },
  },
})
