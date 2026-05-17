import { defineStore } from 'pinia'
import { cursosMock } from '../data'
import { loadFromStorage, saveToStorage } from './shared/persistence'
import {
  getEstablecimientoId,
  getCursoId,
  getCursoEstablecimientoId,
  getCursoAnio,
  getCursoNombre,
} from './shared/helpers'

export const useCursosStore = defineStore('cursos', {
  state: () => ({
    cursos: loadFromStorage('cursos', cursosMock),
    anioActivo: Number(localStorage.getItem('anioActivo')) || new Date().getFullYear(),
  }),

  getters: {
    cursosFiltrados: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.cursos.filter(
        (curso) =>
          getCursoEstablecimientoId(curso) === establecimientoId &&
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
      const establecimientoId = getEstablecimientoId()

      return this.cursosNormalizados.filter(
        (curso) =>
          curso.id_establecimiento === establecimientoId &&
          curso.anio_academico === this.anioActivo,
      )
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
  },

  actions: {
    persistir() {
      saveToStorage('cursos', this.cursos)
      saveToStorage('anioActivo', this.anioActivo)
    },

    cambiarAnioActivo(anio) {
      this.anioActivo = Number(anio)
      this.persistir()
    },

    agregarCurso(data) {
      const establecimientoId = getEstablecimientoId()
      const id = Date.now()

      this.cursos.push({
        id_curso: id,
        id_establecimiento: establecimientoId,

        // Compatibilidad temporal
        id,
        establecimientoId,

        numero: data.numero ? Number(data.numero) : null,
        letra: data.letra || '',
        tipo_ensenanza: data.tipo_ensenanza || data.tipo_ensenianza || '',
        modalidad: data.modalidad || 'Regular',
        anio_academico: data.anio_academico ? Number(data.anio_academico) : this.anioActivo,
        es_nivel_since: Boolean(data.es_nivel_since),
        estado: data.estado || 'Activo',

        profesorJefeId: data.profesorJefeId || null,
        alumnos: [],
        asignaturas: [],

        ...data,
      })

      this.persistir()
    },

    actualizarCurso(id, data) {
      const cursoId = Number(id)

      const index = this.cursos.findIndex((curso) => getCursoId(curso) === cursoId)

      if (index === -1) return

      this.cursos[index] = {
        ...this.cursos[index],
        ...data,
        numero: data.numero !== undefined ? Number(data.numero) : this.cursos[index].numero,
        anio_academico:
          data.anio_academico !== undefined
            ? Number(data.anio_academico)
            : getCursoAnio(this.cursos[index]),
      }

      this.persistir()
    },

    eliminarCurso(id) {
      const cursoId = Number(id)

      this.cursos = this.cursos.filter((curso) => getCursoId(curso) !== cursoId)

      this.persistir()
    },

    asignarProfesorJefe(cursoId, docenteId) {
      const curso = this.cursos.find((curso) => getCursoId(curso) === Number(cursoId))

      if (!curso) return

      curso.profesorJefeId = docenteId ? Number(docenteId) : null

      this.persistir()
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

      this.persistir()
    },

    quitarAlumnoDeCurso(cursoId, alumnoId) {
      const cursoIdNumber = Number(cursoId)
      const alumnoIdNumber = Number(alumnoId)

      const curso = this.cursos.find((curso) => getCursoId(curso) === cursoIdNumber)

      if (!curso) return

      curso.alumnos = (curso.alumnos || []).filter((id) => id !== alumnoIdNumber)

      this.persistir()
    },

    resetData() {
      this.cursos = [...cursosMock]
      this.anioActivo = new Date().getFullYear()

      this.persistir()
    },
  },
})
