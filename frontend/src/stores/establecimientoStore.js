import { defineStore } from 'pinia'
import api from '@/api/axios'
import { loadFromStorage, saveToStorage } from './shared/persistence'
import { getEstablecimientoId } from './shared/helpers'

const API_URL = 'http://localhost:8080'

const mapEstablecimiento = (data) => ({
  id_establecimiento: data.idEstablecimiento,
  rbd: data.rbd,
  nombre: data.nombre,
  id_tipo_estab: data.idTipoEstab ?? 1,
  sostenedor: data.sostenedor ?? '',
  director: data.director ?? '',
  calle: data.calle ?? '',
  numero: data.numero ?? '',
  id_comuna: data.idComuna ?? 1,
  telefono: data.telefono ?? '',
  correo_electronico: data.correoElectronico ?? '',
  modo_aula: data.modoAula ?? 'NORMAL',
  estado: data.estado ?? 'ACTIVO',
})

export const useEstablecimientoStore = defineStore('establecimiento', {
  state: () => ({
    establecimiento: loadFromStorage('establecimiento', null),
    loading: false,
    error: null,
  }),

  getters: {
    establecimientoActivo: (state) => state.establecimiento,
  },

  actions: {
    persistir() {
      saveToStorage('establecimiento', this.establecimiento)
    },

    async cargarPorRbd(rbd) {
      try {
        this.loading = true
        this.error = null

        const { data } = await api.get(`/establecimiento/rbd/${rbd}`)

        this.establecimiento = mapEstablecimiento(data)
        this.persistir()

        return this.establecimiento
      } catch (error) {
        this.error = error
        console.error('Error cargando establecimiento:', error)
        return null
      } finally {
        this.loading = false
      }
    },

    actualizarEstablecimiento(data) {
      this.establecimiento = {
        ...this.establecimiento,
        ...data,
      }

      this.persistir()
    },

    resetData() {
      this.establecimiento = null
      localStorage.removeItem('establecimiento')
    },
  },
})