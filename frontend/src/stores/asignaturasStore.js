import { defineStore } from 'pinia'
import { asignaturasMock, docenteAsignaturaCursoMock, tiposCalificacionMock } from '../data'

import { loadFromStorage, saveToStorage } from './shared/persistence'
import {
  getEstablecimientoId,
  getAsignaturaId,
  getAsignaturaEstablecimientoId,
} from './shared/helpers'

export const useAsignaturasStore = defineStore('asignaturas', {
  state: () => ({
    asignaturas: loadFromStorage('asignaturas', asignaturasMock),
    tiposCalificacion: loadFromStorage('tiposCalificacion', tiposCalificacionMock),
    docenteAsignaturaCurso: loadFromStorage('docenteAsignaturaCurso', docenteAsignaturaCursoMock),
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
      return (cursoId, anioActivo) => {
        const cursoIdNumber = Number(cursoId)

        return state.docenteAsignaturaCurso.filter((item) => {
          const mismoCurso = item.cursoId === cursoIdNumber || item.id_curso === cursoIdNumber
          const mismoAnio = anioActivo
            ? item.anioAcademico === anioActivo || item.anio_academico === anioActivo
            : true

          return mismoCurso && mismoAnio
        })
      }
    },
  },

  actions: {
    persistir() {
      saveToStorage('asignaturas', this.asignaturas)
      saveToStorage('tiposCalificacion', this.tiposCalificacion)
      saveToStorage('docenteAsignaturaCurso', this.docenteAsignaturaCurso)
    },

    agregarAsignatura(data) {
      const establecimientoId = getEstablecimientoId()

      const nuevaAsignatura = {
        id_asignatura: Date.now(),
        id_establecimiento: establecimientoId,
        nombre: '',
        codigo: '',
        id_tipo_calificacion: null,
        estado: 'Activa',
        ...data,

        // Compatibilidad temporal con vistas antiguas
        id: data.id ?? data.id_asignatura ?? Date.now(),
        establecimientoId,
      }

      delete nuevaAsignatura.horas_semanales
      delete nuevaAsignatura.tipo_ensenanza
      delete nuevaAsignatura.tipo_ensenianza

      this.asignaturas.push(nuevaAsignatura)
      this.persistir()
    },

    actualizarAsignatura(id, data) {
      const asignaturaId = Number(id)

      const index = this.asignaturas.findIndex(
        (asignatura) => getAsignaturaId(asignatura) === asignaturaId,
      )

      if (index === -1) return

      const dataLimpia = { ...data }

      delete dataLimpia.horas_semanales
      delete dataLimpia.tipo_ensenanza
      delete dataLimpia.tipo_ensenianza

      this.asignaturas[index] = {
        ...this.asignaturas[index],
        ...dataLimpia,
      }

      this.persistir()
    },

    eliminarAsignatura(id) {
      const asignaturaId = Number(id)

      this.asignaturas = this.asignaturas.filter(
        (asignatura) => getAsignaturaId(asignatura) !== asignaturaId,
      )

      this.docenteAsignaturaCurso = this.docenteAsignaturaCurso.filter(
        (item) => item.asignaturaId !== asignaturaId && item.id_asignatura !== asignaturaId,
      )

      this.persistir()
    },

    agregarAsignaturaACurso(cursoId, asignaturaId, docenteId, data = {}) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)

      const yaExiste = this.docenteAsignaturaCurso.some(
        (item) =>
          (item.cursoId === cursoIdNumber || item.id_curso === cursoIdNumber) &&
          (item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber),
      )

      if (yaExiste) return

      this.docenteAsignaturaCurso.push({
        id: Date.now(),

        // Compatibilidad frontend actual
        cursoId: cursoIdNumber,
        asignaturaId: asignaturaIdNumber,
        docenteId: docenteId ? Number(docenteId) : null,
        anioAcademico: data.anioAcademico || data.anio_academico || new Date().getFullYear(),

        // Modelo más relacional
        id_curso: cursoIdNumber,
        id_asignatura: asignaturaIdNumber,
        id_docente: docenteId ? Number(docenteId) : null,
        anio_academico: data.anioAcademico || data.anio_academico || new Date().getFullYear(),

        horas_semanales: data.horas_semanales ? Number(data.horas_semanales) : null,
        estado: data.estado || 'Activo',
      })

      this.persistir()
    },

    quitarAsignaturaDeCurso(cursoId, asignaturaId) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)

      this.docenteAsignaturaCurso = this.docenteAsignaturaCurso.filter((item) => {
        const mismoCurso = item.cursoId === cursoIdNumber || item.id_curso === cursoIdNumber

        const mismaAsignatura =
          item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber

        return !(mismoCurso && mismaAsignatura)
      })

      this.persistir()
    },

    cambiarDocenteAsignatura(cursoId, asignaturaId, docenteId) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)

      const relacion = this.docenteAsignaturaCurso.find((item) => {
        const mismoCurso = item.cursoId === cursoIdNumber || item.id_curso === cursoIdNumber

        const mismaAsignatura =
          item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber

        return mismoCurso && mismaAsignatura
      })

      if (!relacion) return

      relacion.docenteId = docenteId ? Number(docenteId) : null
      relacion.id_docente = docenteId ? Number(docenteId) : null

      this.persistir()
    },

    actualizarHorasAsignaturaCurso(cursoId, asignaturaId, horasSemanales) {
      const cursoIdNumber = Number(cursoId)
      const asignaturaIdNumber = Number(asignaturaId)

      const relacion = this.docenteAsignaturaCurso.find((item) => {
        const mismoCurso = item.cursoId === cursoIdNumber || item.id_curso === cursoIdNumber

        const mismaAsignatura =
          item.asignaturaId === asignaturaIdNumber || item.id_asignatura === asignaturaIdNumber

        return mismoCurso && mismaAsignatura
      })

      if (!relacion) return

      relacion.horas_semanales = horasSemanales ? Number(horasSemanales) : null

      this.persistir()
    },

    resetData() {
      this.asignaturas = [...asignaturasMock]
      this.tiposCalificacion = [...tiposCalificacionMock]
      this.docenteAsignaturaCurso = [...docenteAsignaturaCursoMock]

      this.persistir()
    },
  },
})
