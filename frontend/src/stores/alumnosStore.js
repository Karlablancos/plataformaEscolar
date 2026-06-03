import { defineStore } from 'pinia'
import {
  alumnosMock,
  apoderadosMock,
  estudianteApoderadoMock,
  tiposNeeMock,
  antecedentesSaludMock,
  antecedentesFamiliaresMock,
  documentosEstudianteMock,
} from '../data'

import { loadFromStorage, saveToStorage } from './shared/persistence'
import { getEstablecimientoId, getNombreCompleto } from './shared/helpers'
import api from '@/api/axios'

const getAlumnoId = (alumno) => alumno.id_alumno ?? alumno.id

const getAlumnoEstablecimientoId = (alumno) => {
  return alumno.id_establecimiento ?? alumno.establecimientoId
}

export const useAlumnosStore = defineStore('alumnos', {
  state: () => ({
    alumnos: loadFromStorage('alumnos', alumnosMock),
    cargando: false,

    apoderados: loadFromStorage('apoderados', apoderadosMock),
    estudianteApoderado: loadFromStorage('estudianteApoderado', estudianteApoderadoMock),

    tiposNee: loadFromStorage('tiposNee', tiposNeeMock),
    antecedentesSalud: loadFromStorage('antecedentesSalud', antecedentesSaludMock),
    antecedentesFamiliares: loadFromStorage('antecedentesFamiliares', antecedentesFamiliaresMock),
    documentosEstudiante: loadFromStorage('documentosEstudiante', documentosEstudianteMock),
  }),

  getters: {
    alumnosFiltrados: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.alumnos.filter(
        (alumno) => getAlumnoEstablecimientoId(alumno) === establecimientoId,
      )
    },

    alumnosNormalizados: (state) => {
      return state.alumnos.map((alumno) => ({
        ...alumno,
        id: getAlumnoId(alumno),
        id_alumno: getAlumnoId(alumno),
        id_establecimiento: getAlumnoEstablecimientoId(alumno),
        establecimientoId: getAlumnoEstablecimientoId(alumno),
        nombreCompleto: getNombreCompleto(alumno),
        estado: alumno.estado || 'Activo',
      }))
    },

    alumnosFiltradosNormalizados() {
      const establecimientoId = getEstablecimientoId()

      return this.alumnosNormalizados.filter(
        (alumno) => Number(alumno.id_establecimiento) === Number(establecimientoId),
      )
    },

    apoderadosFiltrados: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.apoderados.filter(
        (apoderado) =>
          apoderado.id_establecimiento === establecimientoId ||
          apoderado.establecimientoId === establecimientoId,
      )
    },

    getAlumnoById: (state) => {
      return (id) => {
        const alumnoId = Number(id)

        return state.alumnos.find((alumno) => getAlumnoId(alumno) === alumnoId)
      }
    },

    getTipoNeeById: (state) => {
      return (id) => {
        const tipoId = Number(id)

        return state.tiposNee.find((tipo) => tipo.id_tipo_nee === tipoId || tipo.id === tipoId)
      }
    },

    getApoderadosByEstudianteId: (state) => {
      return (estudianteId) => {
        const estudianteIdNumber = Number(estudianteId)

        const relaciones = state.estudianteApoderado.filter(
          (relacion) =>
            relacion.estudianteId === estudianteIdNumber ||
            relacion.id_estudiante === estudianteIdNumber ||
            relacion.id_alumno === estudianteIdNumber,
        )

        return relaciones
          .map((relacion) => {
            const apoderado = state.apoderados.find(
              (item) =>
                item.id === relacion.apoderadoId || item.id_apoderado === relacion.id_apoderado,
            )

            if (!apoderado) return null

            return {
              ...apoderado,
              parentesco: relacion.parentesco,
              esApoderadoTitular: relacion.esApoderadoTitular,
              viveConEstudiante: relacion.viveConEstudiante,
              fechaAsignacion: relacion.fechaAsignacion,
            }
          })
          .filter(Boolean)
      }
    },

    getAntecedenteSaludByEstudianteId: (state) => {
      return (estudianteId) => {
        const estudianteIdNumber = Number(estudianteId)

        return state.antecedentesSalud.find(
          (item) =>
            item.estudianteId === estudianteIdNumber ||
            item.id_estudiante === estudianteIdNumber ||
            item.id_alumno === estudianteIdNumber,
        )
      }
    },

    getAntecedenteFamiliarByEstudianteId: (state) => {
      return (estudianteId) => {
        const estudianteIdNumber = Number(estudianteId)

        return state.antecedentesFamiliares.find(
          (item) =>
            item.estudianteId === estudianteIdNumber ||
            item.id_estudiante === estudianteIdNumber ||
            item.id_alumno === estudianteIdNumber,
        )
      }
    },

    getDocumentosByEstudianteId: (state) => {
      return (estudianteId) => {
        const estudianteIdNumber = Number(estudianteId)

        return state.documentosEstudiante.filter(
          (item) =>
            item.estudianteId === estudianteIdNumber ||
            item.id_estudiante === estudianteIdNumber ||
            item.id_alumno === estudianteIdNumber,
        )
      }
    },
  },

  actions: {
    async cargarAlumnos() {
      const idEstablecimiento =
        getEstablecimientoId() ??
        JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento

      if (!idEstablecimiento) return

      this.cargando = true
      try {
        const { data } = await api.get(`/establecimiento/${idEstablecimiento}/estudiantes`)
        this.alumnos = data.map((e) => ({
          // IDs en ambas convenciones para compatibilidad con el resto del store
          id: e.idEstudiante,
          id_alumno: e.idEstudiante,

          // Establecimiento en ambas convenciones — Number() garantiza que sea número y no string
          id_establecimiento: Number(e.idEstablecimiento),
          establecimientoId: Number(e.idEstablecimiento),

          // Nombre — trim() porque la BD guarda char(8)/char(1) con espacios
          nombres: e.nombres?.trim() ?? '',
          apellido_paterno: e.apellidoPaterno?.trim() ?? '',
          apellido_materno: e.apellidoMaterno?.trim() ?? '',
          nombreCompleto: e.nombreCompleto?.trim() ?? '',

          // RUT — trim() para eliminar espacios del tipo char(8)
          rut: e.rut?.trim() ?? '',
          dv: e.dv?.trim() ?? '',

          // Contacto en ambas convenciones
          correo_electronico: e.correoElectronico ?? '',
          correoElectronico: e.correoElectronico ?? '',
          telefono: e.telefono ?? '',

          // Fechas
          fecha_nacimiento: e.fechaNacimiento ?? null,
          fecha_matricula: e.fechaMatricula ?? null,

          // Estado: la BD guarda ACTIVO/INACTIVO (mayúsculas), la vista espera Activo/Inactivo
          estado: e.estado
            ? e.estado.charAt(0).toUpperCase() + e.estado.slice(1).toLowerCase()
            : 'Activo',

          // Flags booleanos
          prioritario: e.prioritario ?? false,
          preferente: e.preferente ?? false,
          tieneNee: e.tieneNee ?? false,
          enPie: e.enPie ?? false,

          // Campos no provistos por la API — se mantienen en null/default
          cursoId: null,
          id_curso: null,
          asistencia: 0,
          promedio: 0,
        }))

        this.persistir()
      } catch (error) {
        console.warn('cargarAlumnos: usando localStorage como fallback', error?.message)
      } finally {
        this.cargando = false
      }
    },

    persistir() {
      saveToStorage('alumnos', this.alumnos)
      saveToStorage('apoderados', this.apoderados)
      saveToStorage('estudianteApoderado', this.estudianteApoderado)
      saveToStorage('tiposNee', this.tiposNee)
      saveToStorage('antecedentesSalud', this.antecedentesSalud)
      saveToStorage('antecedentesFamiliares', this.antecedentesFamiliares)
      saveToStorage('documentosEstudiante', this.documentosEstudiante)
    },

    agregarAlumno(data) {
      const establecimientoId = getEstablecimientoId()
      const id = Date.now()

      const nuevoAlumno = {
        id_alumno: id,
        id_establecimiento: establecimientoId,

        // Compatibilidad temporal
        id,
        establecimientoId,

        estado: 'Activo',
        asistencia: 0,
        promedio: 0,
        prioritario: false,
        preferente: false,
        tieneNee: false,
        tipoNeeId: null,
        enPie: false,

        cursoId: data.cursoId || null,

        ...data,
      }

      this.alumnos.push(nuevoAlumno)
      this.persistir()

      return nuevoAlumno
    },

    actualizarAlumno(id, data) {
      const alumnoId = Number(id)

      const index = this.alumnos.findIndex((alumno) => getAlumnoId(alumno) === alumnoId)

      if (index === -1) return

      this.alumnos[index] = {
        ...this.alumnos[index],
        ...data,
      }

      this.persistir()
    },

    eliminarAlumno(id) {
      const alumnoId = Number(id)

      this.alumnos = this.alumnos.filter((alumno) => getAlumnoId(alumno) !== alumnoId)

      this.estudianteApoderado = this.estudianteApoderado.filter(
        (relacion) =>
          relacion.estudianteId !== alumnoId &&
          relacion.id_estudiante !== alumnoId &&
          relacion.id_alumno !== alumnoId,
      )

      this.antecedentesSalud = this.antecedentesSalud.filter(
        (item) =>
          item.estudianteId !== alumnoId &&
          item.id_estudiante !== alumnoId &&
          item.id_alumno !== alumnoId,
      )

      this.antecedentesFamiliares = this.antecedentesFamiliares.filter(
        (item) =>
          item.estudianteId !== alumnoId &&
          item.id_estudiante !== alumnoId &&
          item.id_alumno !== alumnoId,
      )

      this.documentosEstudiante = this.documentosEstudiante.filter(
        (item) =>
          item.estudianteId !== alumnoId &&
          item.id_estudiante !== alumnoId &&
          item.id_alumno !== alumnoId,
      )

      this.persistir()
    },

    asignarAlumnoACurso(alumnoId, cursoId) {
      const alumnoIdNumber = Number(alumnoId)
      const cursoIdNumber = Number(cursoId)

      const alumno = this.alumnos.find((item) => getAlumnoId(item) === alumnoIdNumber)

      if (!alumno) return

      alumno.cursoId = cursoIdNumber
      alumno.id_curso = cursoIdNumber

      this.persistir()
    },

    quitarAlumnoDeCurso(alumnoId, cursoId = null) {
      const alumnoIdNumber = Number(alumnoId)

      const alumno = this.alumnos.find((item) => getAlumnoId(item) === alumnoIdNumber)

      if (!alumno) return

      if (!cursoId || alumno.cursoId === Number(cursoId) || alumno.id_curso === Number(cursoId)) {
        alumno.cursoId = null
        alumno.id_curso = null
      }

      this.persistir()
    },

    agregarApoderadoAEstudiante(estudianteId, apoderadoData, relacionData = {}) {
      const establecimientoId = getEstablecimientoId()
      const estudianteIdNumber = Number(estudianteId)
      const seraTitular = Boolean(relacionData?.esApoderadoTitular)
      const idApoderado = Date.now()

      if (seraTitular) {
        this.estudianteApoderado = this.estudianteApoderado.map((relacion) => {
          const mismaRelacion =
            relacion.estudianteId === estudianteIdNumber ||
            relacion.id_estudiante === estudianteIdNumber ||
            relacion.id_alumno === estudianteIdNumber

          if (!mismaRelacion) return relacion

          return {
            ...relacion,
            esApoderadoTitular: false,
          }
        })
      }

      const nuevoApoderado = {
        id: idApoderado,
        id_apoderado: idApoderado,
        establecimientoId,
        id_establecimiento: establecimientoId,
        estado: 'Activo',
        ...apoderadoData,
      }

      this.apoderados.push(nuevoApoderado)

      this.estudianteApoderado.push({
        id: Date.now() + 1,
        estudianteId: estudianteIdNumber,
        id_estudiante: estudianteIdNumber,
        id_alumno: estudianteIdNumber,
        apoderadoId: idApoderado,
        id_apoderado: idApoderado,
        parentesco: relacionData?.parentesco || 'Apoderado',
        esApoderadoTitular: seraTitular,
        viveConEstudiante: Boolean(relacionData?.viveConEstudiante),
        fechaAsignacion: relacionData?.fechaAsignacion || new Date().toISOString().slice(0, 10),
      })

      this.persistir()
    },

    actualizarAntecedenteSalud(estudianteId, data) {
      const estudianteIdNumber = Number(estudianteId)

      const index = this.antecedentesSalud.findIndex(
        (item) =>
          item.estudianteId === estudianteIdNumber ||
          item.id_estudiante === estudianteIdNumber ||
          item.id_alumno === estudianteIdNumber,
      )

      if (index === -1) {
        this.antecedentesSalud.push({
          id: Date.now(),
          estudianteId: estudianteIdNumber,
          id_estudiante: estudianteIdNumber,
          id_alumno: estudianteIdNumber,
          ...data,
        })
      } else {
        this.antecedentesSalud[index] = {
          ...this.antecedentesSalud[index],
          ...data,
        }
      }

      this.persistir()
    },

    actualizarAntecedenteFamiliar(estudianteId, data) {
      const estudianteIdNumber = Number(estudianteId)

      const index = this.antecedentesFamiliares.findIndex(
        (item) =>
          item.estudianteId === estudianteIdNumber ||
          item.id_estudiante === estudianteIdNumber ||
          item.id_alumno === estudianteIdNumber,
      )

      if (index === -1) {
        this.antecedentesFamiliares.push({
          id: Date.now(),
          estudianteId: estudianteIdNumber,
          id_estudiante: estudianteIdNumber,
          id_alumno: estudianteIdNumber,
          ...data,
        })
      } else {
        this.antecedentesFamiliares[index] = {
          ...this.antecedentesFamiliares[index],
          ...data,
        }
      }

      this.persistir()
    },

    resetData() {
      this.alumnos = [...alumnosMock]
      this.apoderados = [...apoderadosMock]
      this.estudianteApoderado = [...estudianteApoderadoMock]
      this.tiposNee = [...tiposNeeMock]
      this.antecedentesSalud = [...antecedentesSaludMock]
      this.antecedentesFamiliares = [...antecedentesFamiliaresMock]
      this.documentosEstudiante = [...documentosEstudianteMock]

      this.persistir()
    },
  },
})
