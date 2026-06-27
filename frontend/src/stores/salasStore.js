import { defineStore } from 'pinia'
import api from '@/api/axios'
import { getEstablecimientoId } from './shared/helpers'

const resolveEstablecimientoId = () => {
  return (
    getEstablecimientoId() ??
    JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento
  )
}

export const mapSalaFromApi = (dto) => ({
  id: dto.idSala,
  id_sala: dto.idSala,
  numero: dto.numero ?? null,
  nombre: dto.nombre?.trim() ?? '',
  capacidad: dto.capacidad ?? null,
  tipo: dto.tipo?.trim() ?? '',
  piso: dto.piso ?? null,
  estado: dto.estado?.trim() ?? 'ACTIVO',
})

const fetchWithRetry = async (fn, retries = 3, delayMs = 1500) => {
  let lastError

  for (let attempt = 0; attempt < retries; attempt++) {
    try {
      return await fn()
    } catch (error) {
      lastError = error
      const status = error?.response?.status
      const esReintentable = !status || status >= 500

      if (!esReintentable || attempt === retries - 1) {
        throw error
      }

      await new Promise((resolve) => setTimeout(resolve, delayMs))
    }
  }

  throw lastError
}

export const useSalasStore = defineStore('salas', {
  state: () => ({
    salas: [],
    cargando: false,
    error: null,
  }),

  getters: {
    salasActivas: (state) => state.salas,
  },

  actions: {
    async cargarSalas() {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) return []

      this.cargando = true
      this.error = null

      try {
        const { data } = await fetchWithRetry(() =>
          api.get(`/establecimiento/${idEstablecimiento}/salas`),
        )
        this.salas = data.map(mapSalaFromApi)
        return this.salas
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },
  },
})
