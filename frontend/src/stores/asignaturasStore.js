import { defineStore } from 'pinia'
import api from '@/api/axios'
import {
  getEstablecimientoId,
  getAsignaturaId,
  getAsignaturaEstablecimientoId,
} from './shared/helpers'
import { mapCursoAsignaturaFromApi } from './shared/cursoAsignaturaMapper'

const resolveEstablecimientoId = () => {
  return (
    getEstablecimientoId() ??
    JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento
  )
}

const mapEstadoFromApi = (estado) => {
  if (!estado) return 'Activa'
  const normalizado = String(estado).trim().toUpperCase()
  return normalizado.startsWith('INACTIV') ? 'Inactiva' : 'Activa'
}

const mapEstadoToApi = (estado) => {
  const normalizado = String(estado || '').trim().toLowerCase()
  return normalizado.startsWith('inactiv') ? 'INACTIVO' : 'ACTIVO'
}

const mapAsignaturaFromApi = (dto) => ({
  id: dto.idAsignatura,
  id_asignatura: dto.idAsignatura,
  id_establecimiento: Number(dto.idEstablecimiento),
  establecimientoId: Number(dto.idEstablecimiento),
  nombre: dto.nombre?.trim() ?? '',
  codigo: dto.codigo?.trim() ?? '',
  id_tipo_calificacion: dto.idTipoCalificacion,
  estado: mapEstadoFromApi(dto.estado),
})

const mapTipoCalificacionFromApi = (dto) => ({
  id_tipo_calificacion: dto.idTipoCalificacion,
  nombre: dto.nombre,
  escala: dto.escala,
  entra_promedio: dto.entraPromedio,
  minimo_aprobacion: dto.minimoAprobacion,
})

const toAsignaturaPayload = (data) => ({
  nombre: data.nombre?.trim() ?? '',
  codigo: data.codigo?.trim().toUpperCase() ?? '',
  idTipoCalificacion: Number(data.id_tipo_calificacion),
  estado: mapEstadoToApi(data.estado),
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

export const useAsignaturasStore = defineStore('asignaturas', {
  state: () => ({
    asignaturas: [],
    tiposCalificacion: [],
    asignaturasPorCurso: {},
    docenteAsignaturaCurso: [],
    cargando: false,
    error: null,
  }),

  getters: {
    asignaturasFiltradas: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.asignaturas.filter(
        (asignatura) => getAsignaturaEstablecimientoId(asignatura) === establecimientoId,
      )
    },

    asignaturasNormalizadas: (state) => {
      return state.asignaturas.map((asignatura) => ({
        ...asignatura,
        id: getAsignaturaId(asignatura),
        id_asignatura: getAsignaturaId(asignatura),
        id_establecimiento: getAsignaturaEstablecimientoId(asignatura),
        establecimientoId: getAsignaturaEstablecimientoId(asignatura),
        estado: asignatura.estado || 'Activa',
      }))
    },

    asignaturasFiltradasNormalizadas: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.asignaturas
        .filter((asignatura) => getAsignaturaEstablecimientoId(asignatura) === establecimientoId)
        .map((asignatura) => ({
          ...asignatura,
          id: getAsignaturaId(asignatura),
          id_asignatura: getAsignaturaId(asignatura),
          id_establecimiento: getAsignaturaEstablecimientoId(asignatura),
          establecimientoId: getAsignaturaEstablecimientoId(asignatura),
          estado: asignatura.estado || 'Activa',
        }))
    },

    tiposCalificacionActivos: (state) => {
      return state.tiposCalificacion
    },

    getAsignaturaById: (state) => {
      return (id) => {
        const idNumber = Number(id)

        return state.asignaturas.find((asignatura) => getAsignaturaId(asignatura) === idNumber)
      }
    },

    getTipoCalificacionById: (state) => {
      return (id) => {
        const idNumber = Number(id)

        return state.tiposCalificacion.find(
          (tipo) => tipo.id_tipo_calificacion === idNumber || tipo.id === idNumber,
        )
      }
    },

    getAsignaturasByCursoId: (state) => {
      return (cursoId) => state.asignaturasPorCurso[Number(cursoId)] || []
    },

    getAsignaturasCurso: (state) => {
      return (cursoId) => state.asignaturasPorCurso[Number(cursoId)] || []
    },
  },

  actions: {
    persistir() {},

    async sincronizarAsignaturasCurso(cursoId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)

      if (!idEstablecimiento || !cursoIdNumber) {
        throw new Error('No se encontró el establecimiento o curso activo.')
      }

      const { data } = await fetchWithRetry(() =>
        api.get(`/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/asignaturas`),
      )

      const asignaturas = data.map(mapCursoAsignaturaFromApi)
      this.asignaturasPorCurso = {
        ...this.asignaturasPorCurso,
        [cursoIdNumber]: asignaturas,
      }

      localStorage.removeItem('docenteAsignaturaCurso')
      return asignaturas
    },

    async _fetchAsignaturas(idEstablecimiento) {
      const { data } = await fetchWithRetry(() =>
        api.get(`/establecimiento/${idEstablecimiento}/asignaturas`),
      )
      this.asignaturas = data.map(mapAsignaturaFromApi)
    },

    async _fetchTiposCalificacion() {
      const { data } = await fetchWithRetry(() =>
        api.get('/establecimiento/tipos-calificacion'),
      )
      this.tiposCalificacion = data.map(mapTipoCalificacionFromApi)
    },

    async cargarTiposCalificacion() {
      try {
        await this._fetchTiposCalificacion()
      } catch (error) {
        console.warn('cargarTiposCalificacion: error al cargar desde API', error?.message)
        throw error
      }
    },

    async cargarAsignaturas() {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) return

      this.cargando = true
      this.error = null

      try {
        await this._fetchAsignaturas(idEstablecimiento)
      } catch (error) {
        this.error = error
        console.warn('cargarAsignaturas: error al cargar desde API', error?.message)
        throw error
      } finally {
        this.cargando = false
      }
    },

    async cargarDatos() {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) return

      this.cargando = true
      this.error = null

      const [asignaturasResult, tiposResult] = await Promise.allSettled([
        this._fetchAsignaturas(idEstablecimiento),
        this._fetchTiposCalificacion(),
      ])

      if (asignaturasResult.status === 'rejected') {
        this.error = asignaturasResult.reason
        throw asignaturasResult.reason
      }

      if (tiposResult.status === 'rejected') {
        console.warn('cargarDatos: tipos de calificación no disponibles', tiposResult.reason?.message)
      }

      this.cargando = false
    },

    async agregarAsignatura(data) {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: creada } = await api.post(
          `/establecimiento/${idEstablecimiento}/asignaturas`,
          toAsignaturaPayload(data),
        )
        const asignatura = mapAsignaturaFromApi(creada)
        this.asignaturas.push(asignatura)
        return asignatura
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async actualizarAsignatura(id, data) {
      const idEstablecimiento = resolveEstablecimientoId()
      const asignaturaId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: actualizada } = await api.put(
          `/establecimiento/${idEstablecimiento}/asignaturas/${asignaturaId}`,
          toAsignaturaPayload(data),
        )

        const asignatura = mapAsignaturaFromApi(actualizada)
        const index = this.asignaturas.findIndex(
          (item) => getAsignaturaId(item) === asignaturaId,
        )

        if (index !== -1) {
          this.asignaturas[index] = asignatura
        }

        return asignatura
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async eliminarAsignatura(id) {
      const idEstablecimiento = resolveEstablecimientoId()
      const asignaturaId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.delete(
          `/establecimiento/${idEstablecimiento}/asignaturas/${asignaturaId}`,
        )

        this.asignaturas = this.asignaturas.filter(
          (asignatura) => getAsignaturaId(asignatura) !== asignaturaId,
        )

        this.asignaturasPorCurso = Object.fromEntries(
          Object.entries(this.asignaturasPorCurso).map(([cursoKey, items]) => [
            cursoKey,
            items.filter(
              (item) =>
                item.asignaturaId !== asignaturaId && item.id_asignatura !== asignaturaId,
            ),
          ]),
        )
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async agregarAsignaturaACurso(cursoId, asignaturaId, docenteId, data = {}) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      if (!docenteId) {
        throw new Error('El docente responsable es obligatorio.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.post(`/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/asignaturas`, {
          idAsignatura: Number(asignaturaId),
          idDocente: Number(docenteId),
          horasSemanales: data.horas_semanales ? Number(data.horas_semanales) : 4,
        })

        return this.sincronizarAsignaturasCurso(cursoIdNumber)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async quitarAsignaturaDeCurso(cursoId, asignaturaId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.delete(
          `/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/asignaturas/${asignaturaIdNumber}`,
        )

        return this.sincronizarAsignaturasCurso(cursoIdNumber)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    cambiarDocenteAsignatura(cursoId, asignaturaId, docenteId) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)
      const items = this.asignaturasPorCurso[cursoIdNumber] || []
      const relacion = items.find(
        (item) =>
          item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber,
      )

      if (!relacion || !docenteId) return

      return this.agregarAsignaturaACurso(cursoIdNumber, asignaturaIdNumber, docenteId, {
        horas_semanales: relacion.horas_semanales ?? relacion.horasSemanales ?? 4,
      })
    },

    actualizarHorasAsignaturaCurso(cursoId, asignaturaId, horasSemanales) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)
      const items = this.asignaturasPorCurso[cursoIdNumber] || []
      const relacion = items.find(
        (item) =>
          item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber,
      )

      if (!relacion?.docenteId && !relacion?.id_docente) return

      return this.agregarAsignaturaACurso(
        cursoIdNumber,
        asignaturaIdNumber,
        relacion.docenteId ?? relacion.id_docente,
        { horas_semanales: horasSemanales },
      )
    },

    resetData() {
      this.asignaturas = []
      this.tiposCalificacion = []
      this.asignaturasPorCurso = {}
      this.docenteAsignaturaCurso = []
    },
  },
})
