import { defineStore } from 'pinia'
import api from '@/api/axios'

export const useAsistenciaStore = defineStore('asistencia', {
  state: () => ({
    cargando: false,
    guardando: false,
  }),

  actions: {
    async registrarAsistencia(dto) {
      const { data } = await api.post('/asistencia', dto)
      return data
    },

    async obtenerPorEstudiante(idEstudiante) {
      this.cargando = true
      try {
        const { data } = await api.get(`/asistencia/estudiante/${idEstudiante}`)
        return data
      } finally {
        this.cargando = false
      }
    },

    async obtenerPorFecha(idEstudiante, fecha) {
      this.cargando = true
      try {
        const { data } = await api.get(`/asistencia/estudiante/${idEstudiante}/fecha/${fecha}`)
        return data
      } finally {
        this.cargando = false
      }
    },

    async obtenerResumen(idEstudiante) {
      this.cargando = true
      try {
        const { data } = await api.get(`/asistencia/estudiante/${idEstudiante}/resumen`)
        return data
      } finally {
        this.cargando = false
      }
    },
  },
})
