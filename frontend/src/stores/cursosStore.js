import { defineStore } from 'pinia'
import api from '@/api/axios'
import { saveToStorage } from './shared/persistence'
import { useAlumnosStore } from './alumnosStore'
import { mapEstudianteFromApi } from './shared/estudianteMapper'
import {
  getEstablecimientoId,
  getCursoId,
  getCursoEstablecimientoId,
  getCursoAnio,
  getCursoNombre,
} from './shared/helpers'

const resolveEstablecimientoId = () => {
  return (
    getEstablecimientoId() ??
    JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento
  )
}

const mapEstadoFromApi = (estado) => {
  if (!estado) return 'Activo'
  const normalizado = String(estado).trim().toUpperCase()
  if (normalizado.startsWith('INACTIV')) return 'Inactivo'
  if (normalizado.startsWith('CERRAD')) return 'Cerrado'
  return 'Activo'
}

const mapEstadoToApi = (estado) => {
  const normalizado = String(estado || '').trim().toLowerCase()
  if (normalizado.startsWith('inactiv')) return 'INACTIVO'
  if (normalizado.startsWith('cerrad')) return 'CERRADO'
  return 'ACTIVO'
}

const sortCursosRecientesPrimero = (cursos) => {
  return [...cursos].sort((a, b) => {
    const ordenA = a._ordenReciente ?? getCursoId(a) ?? 0
    const ordenB = b._ordenReciente ?? getCursoId(b) ?? 0
    return ordenB - ordenA
  })
}

const mapCursoFromApi = (dto) => {
  const esNivelSimce = dto.esNivelSimce ?? false

  return {
    id: dto.idCurso,
    id_curso: dto.idCurso,
    id_establecimiento: Number(dto.idEstablecimiento),
    establecimientoId: Number(dto.idEstablecimiento),
    numero: dto.numero,
    letra: dto.letra?.trim() ?? '',
    tipo_ensenanza: dto.tipoEnsenanza?.trim() ?? '',
    modalidad: dto.modalidad?.trim() ?? '',
    anio: dto.anioAcademico,
    anio_academico: dto.anioAcademico,
    es_nivel_simce: esNivelSimce,
    es_nivel_since: esNivelSimce,
    nombre: dto.nombre?.trim() ?? '',
    estado: mapEstadoFromApi(dto.estado),
    profesorJefeId: dto.idDocenteJefe ?? null,
    alumnos: [],
    asignaturas: [],
  }
}

const aplicarCursoActualizado = (cursos, cursoId, dto) => {
  const curso = mapCursoFromApi(dto)
  const index = cursos.findIndex((item) => getCursoId(item) === Number(cursoId))
  const cursoConOrden = { ...curso, _ordenReciente: Date.now() }

  if (index === -1) {
    return [...cursos, cursoConOrden]
  }

  const alumnos = cursos[index].alumnos || []
  const asignaturas = cursos[index].asignaturas || []

  const actualizados = [...cursos]
  actualizados[index] = { ...cursoConOrden, alumnos, asignaturas }
  return actualizados
}

const toCursoPayload = (data) => ({
  numero: Number(data.numero),
  letra: String(data.letra || '').trim().toUpperCase(),
  tipoEnsenanza: String(data.tipo_ensenanza || '').trim(),
  modalidad: String(data.modalidad || 'Regular').trim(),
  anioAcademico: Number(data.anio_academico ?? data.anio),
  esNivelSimce: Boolean(data.es_nivel_simce ?? data.es_nivel_since),
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

export const useCursosStore = defineStore('cursos', {
  state: () => ({
    cursos: [],
    estudiantesPorCurso: {},
    anioActivo: Number(localStorage.getItem('anioActivo')) || new Date().getFullYear(),
    cargando: false,
    error: null,
  }),

  getters: {
    cursosFiltrados: (state) => {
      const establecimientoId = resolveEstablecimientoId()

      return state.cursos.filter(
        (curso) =>
          getCursoEstablecimientoId(curso) === Number(establecimientoId) &&
          getCursoAnio(curso) === state.anioActivo,
      )
    },

    cursosNormalizados: (state) => {
      return state.cursos.map((curso) => ({
        ...curso,
        id: getCursoId(curso),
        id_curso: getCursoId(curso),
        establecimientoId: getCursoEstablecimientoId(curso),
        id_establecimiento: getCursoEstablecimientoId(curso),
        anio: getCursoAnio(curso),
        anio_academico: getCursoAnio(curso),
        nombre: getCursoNombre(curso),
        estado: curso.estado || 'Activo',
        alumnos: curso.alumnos || [],
        asignaturas: curso.asignaturas || [],
      }))
    },

    cursosFiltradosNormalizados() {
      const establecimientoId = Number(resolveEstablecimientoId())

      const filtrados = this.cursosNormalizados.filter(
        (curso) =>
          curso.id_establecimiento === establecimientoId &&
          curso.anio_academico === this.anioActivo,
      )

      return sortCursosRecientesPrimero(filtrados)
    },

    getCursoById: (state) => {
      return (id) => {
        const cursoId = Number(id)

        return state.cursos.find((curso) => getCursoId(curso) === cursoId)
      }
    },

    getCursoNombre: () => {
      return (curso) => getCursoNombre(curso)
    },

    getEstudiantesCurso: (state) => {
      return (cursoId) => state.estudiantesPorCurso[Number(cursoId)] || []
    },
  },

  actions: {
    persistirAnioActivo() {
      localStorage.setItem('anioActivo', String(this.anioActivo))
    },

    persistir() {
      this.persistirAnioActivo()
    },

    cambiarAnioActivo(anio) {
      this.anioActivo = Number(anio)
      this.persistirAnioActivo()
    },

    async cargarCursos() {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) return

      this.cargando = true
      this.error = null

      try {
        const { data } = await fetchWithRetry(() =>
          api.get(`/establecimiento/${idEstablecimiento}/cursos`),
        )
        this.cursos = data.map(mapCursoFromApi)
      } catch (error) {
        this.error = error
        console.warn('cargarCursos: error al cargar desde API', error?.message)
        throw error
      } finally {
        this.cargando = false
      }
    },

    async agregarCurso(data) {
      const idEstablecimiento = resolveEstablecimientoId()
      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const payload = toCursoPayload({
          ...data,
          anio_academico: data.anio_academico ?? data.anio ?? this.anioActivo,
        })

        const { data: creado } = await api.post(
          `/establecimiento/${idEstablecimiento}/cursos`,
          payload,
        )

        const curso = { ...mapCursoFromApi(creado), _ordenReciente: Date.now() }
        this.cursos.push(curso)
        return curso
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async actualizarCurso(id, data) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data: actualizado } = await api.put(
          `/establecimiento/${idEstablecimiento}/cursos/${cursoId}`,
          toCursoPayload(data),
        )

        const curso = mapCursoFromApi(actualizado)
        this.cursos = aplicarCursoActualizado(this.cursos, cursoId, actualizado)

        return curso
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async eliminarCurso(id) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoId = Number(id)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.delete(`/establecimiento/${idEstablecimiento}/cursos/${cursoId}`)

        this.cursos = this.cursos.filter((curso) => getCursoId(curso) !== cursoId)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async asignarProfesorJefe(cursoId, docenteId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        const { data } = await api.put(
          `/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/profesor-jefe`,
          { idDocente: docenteId ? Number(docenteId) : null },
        )

        this.cursos = aplicarCursoActualizado(this.cursos, cursoIdNumber, data)
        return mapCursoFromApi(data)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    sincronizarAlumnosLocal(cursoId, alumnoIds) {
      const cursoIdNumber = Number(cursoId)
      const ids = alumnoIds.map((id) => Number(id))

      this.cursos = this.cursos.map((curso) => {
        if (getCursoId(curso) !== cursoIdNumber) {
          return {
            ...curso,
            alumnos: (curso.alumnos || []).filter((id) => !ids.includes(Number(id))),
          }
        }

        return {
          ...curso,
          alumnos: ids,
        }
      })
    },

    async sincronizarAlumnosCurso(cursoId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)

      if (!idEstablecimiento || !cursoIdNumber) {
        throw new Error('No se encontró el establecimiento o curso activo.')
      }

      const { data } = await fetchWithRetry(() =>
        api.get(`/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/estudiantes`),
      )

      const estudiantes = data.map((item) => mapEstudianteFromApi({ ...item, idCurso: cursoIdNumber }))
      const alumnoIds = estudiantes.map((estudiante) => estudiante.id)

      this.estudiantesPorCurso = {
        ...this.estudiantesPorCurso,
        [cursoIdNumber]: estudiantes,
      }
      this.sincronizarAlumnosLocal(cursoIdNumber, alumnoIds)

      const alumnosStore = useAlumnosStore()
      alumnosStore.upsertEstudiantesDesdeApi(data)

      return estudiantes
    },

    async matricularEstudiante(cursoId, alumnoId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)
      const alumnoIdNumber = Number(alumnoId)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.post(
          `/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/estudiantes/${alumnoIdNumber}`,
        )

        return this.sincronizarAlumnosCurso(cursoIdNumber)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    async desmatricularEstudiante(cursoId, alumnoId) {
      const idEstablecimiento = resolveEstablecimientoId()
      const cursoIdNumber = Number(cursoId)
      const alumnoIdNumber = Number(alumnoId)

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      this.error = null

      try {
        await api.delete(
          `/establecimiento/${idEstablecimiento}/cursos/${cursoIdNumber}/estudiantes/${alumnoIdNumber}`,
        )

        useAlumnosStore().quitarAlumnoDeCurso(alumnoIdNumber, cursoIdNumber)
        return this.sincronizarAlumnosCurso(cursoIdNumber)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.cargando = false
      }
    },

    asignarAlumnoACurso(cursoId, alumnoId) {
      const cursoIdNumber = Number(cursoId)
      const alumnoIdNumber = Number(alumnoId)

      this.cursos = this.cursos.map((curso) => ({
        ...curso,
        alumnos: (curso.alumnos || []).filter((id) => id !== alumnoIdNumber),
      }))

      const curso = this.cursos.find((item) => getCursoId(item) === cursoIdNumber)

      if (!curso) return

      if (!Array.isArray(curso.alumnos)) {
        curso.alumnos = []
      }

      if (!curso.alumnos.includes(alumnoIdNumber)) {
        curso.alumnos.push(alumnoIdNumber)
      }
    },

    quitarAlumnoDeCurso(cursoId, alumnoId) {
      const cursoIdNumber = Number(cursoId)
      const alumnoIdNumber = Number(alumnoId)

      const curso = this.cursos.find((curso) => getCursoId(curso) === cursoIdNumber)

      if (!curso) return

      curso.alumnos = (curso.alumnos || []).filter((id) => id !== alumnoIdNumber)
    },

    resetData() {
      this.cursos = []
      this.estudiantesPorCurso = {}
      this.anioActivo = new Date().getFullYear()
      this.persistirAnioActivo()
    },
  },
})
