// src/stores/academicStore.js

import { defineStore } from 'pinia'

import { useEstablecimientoStore } from './establecimientoStore'
import { useAlumnosStore } from './alumnosStore'
import { useCursosStore } from './cursosStore'
import { useDocentesStore } from './docentesStore'
import { useAsignaturasStore } from './asignaturasStore'

export const useAcademicStore = defineStore('academic', {
  getters: {
    establecimiento: () => useEstablecimientoStore().establecimiento,
    establecimientoActivo: () => useEstablecimientoStore().establecimientoActivo,

    alumnos: () => useAlumnosStore().alumnos,
    alumnosFiltrados: () => useAlumnosStore().alumnosFiltrados,
    alumnosNormalizados: () => useAlumnosStore().alumnosNormalizados,
    alumnosFiltradosNormalizados: () => useAlumnosStore().alumnosFiltradosNormalizados,

    apoderados: () => useAlumnosStore().apoderados,
    apoderadosFiltrados: () => useAlumnosStore().apoderadosFiltrados,
    estudianteApoderado: () => useAlumnosStore().estudianteApoderado,
    tiposNee: () => useAlumnosStore().tiposNee,
    antecedentesSalud: () => useAlumnosStore().antecedentesSalud,
    antecedentesFamiliares: () => useAlumnosStore().antecedentesFamiliares,
    documentosEstudiante: () => useAlumnosStore().documentosEstudiante,

    cursos: () => useCursosStore().cursos,
    cursosFiltrados: () => useCursosStore().cursosFiltrados,
    cursosNormalizados: () => useCursosStore().cursosNormalizados,
    cursosFiltradosNormalizados: () => useCursosStore().cursosFiltradosNormalizados,
    anioActivo: () => useCursosStore().anioActivo,

    docentes: () => useDocentesStore().docentes,
    docentesFiltrados: () => useDocentesStore().docentesFiltrados,
    docentesNormalizados: () => useDocentesStore().docentesNormalizados,
    docentesFiltradosNormalizados: () => useDocentesStore().docentesFiltradosNormalizados,

    profesoresFiltrados: () => useDocentesStore().profesoresFiltrados,
    categoriasSned: () => useDocentesStore().categoriasSned,
    categoriasSnedActivas: () => useDocentesStore().categoriasSnedActivas,

    asignaturas: () => useAsignaturasStore().asignaturas,
    asignaturasFiltradas: () => useAsignaturasStore().asignaturasFiltradas,
    asignaturasNormalizadas: () => useAsignaturasStore().asignaturasNormalizadas,
    asignaturasFiltradasNormalizadas: () => useAsignaturasStore().asignaturasFiltradasNormalizadas,

    tiposCalificacion: () => useAsignaturasStore().tiposCalificacion,
    tiposCalificacionActivos: () => useAsignaturasStore().tiposCalificacionActivos,

    docenteAsignaturaCurso: () => {
      const store = useAsignaturasStore()
      return Object.values(store.asignaturasPorCurso).flat()
    },

    getEstablecimientoById: () => useEstablecimientoStore().getEstablecimientoById,

    getAlumnoById: () => useAlumnosStore().getAlumnoById,
    getTipoNeeById: () => useAlumnosStore().getTipoNeeById,
    getApoderadosByEstudianteId: () => useAlumnosStore().getApoderadosByEstudianteId,
    getAntecedenteSaludByEstudianteId: () => useAlumnosStore().getAntecedenteSaludByEstudianteId,
    getAntecedenteFamiliarByEstudianteId: () =>
      useAlumnosStore().getAntecedenteFamiliarByEstudianteId,
    getDocumentosByEstudianteId: () => useAlumnosStore().getDocumentosByEstudianteId,

    getCursoById: () => useCursosStore().getCursoById,
    getCursoNombre: () => useCursosStore().getCursoNombre,
    getEstudiantesCurso: () => useCursosStore().getEstudiantesCurso,

    getDocenteById: () => useDocentesStore().getDocenteById,
    getProfesorById: () => useDocentesStore().getProfesorById,
    getCategoriaSnedById: () => useDocentesStore().getCategoriaSnedById,

    getAsignaturaById: () => useAsignaturasStore().getAsignaturaById,
    getTipoCalificacionById: () => useAsignaturasStore().getTipoCalificacionById,
    getAsignaturasByCursoId: () => useAsignaturasStore().getAsignaturasCurso,
    getAsignaturasCurso: () => useAsignaturasStore().getAsignaturasCurso,
  },

  actions: {
    persistir() {
      useEstablecimientoStore().persistir()
      useAlumnosStore().persistir()
      useCursosStore().persistir()
      useDocentesStore().persistir()
      useAsignaturasStore().persistir()
    },

    actualizarEstablecimiento(data) {
      useEstablecimientoStore().actualizarEstablecimiento(data)
    },

    cambiarAnioActivo(anio) {
      useCursosStore().cambiarAnioActivo(anio)
    },

    agregarAlumno(data) {
      const alumno = useAlumnosStore().agregarAlumno(data)

      if (data.cursoId && alumno?.id) {
        useCursosStore().asignarAlumnoACurso(data.cursoId, alumno.id)
      }

      return alumno
    },

    actualizarAlumno(id, data) {
      useAlumnosStore().actualizarAlumno(id, data)
    },

    eliminarAlumno(id) {
      useAlumnosStore().eliminarAlumno(id)

      const cursosStore = useCursosStore()

      cursosStore.cursos.forEach((curso) => {
        const cursoId = curso.id_curso ?? curso.id
        cursosStore.quitarAlumnoDeCurso(cursoId, id)
      })
    },

    async cargarAlumnos() {
      return useAlumnosStore().cargarAlumnos()
    },

    async cargarAsignaturas() {
      return useAsignaturasStore().cargarAsignaturas()
    },

    async cargarDocentes() {
      return useDocentesStore().cargarDocentes()
    },

    asignarAlumnoACurso(cursoId, alumnoId) {
      return useCursosStore().matricularEstudiante(cursoId, alumnoId)
    },

    quitarAlumnoDeCurso(cursoId, alumnoId) {
      return useCursosStore().desmatricularEstudiante(cursoId, alumnoId)
    },

    async sincronizarAlumnosCurso(cursoId) {
      return useCursosStore().sincronizarAlumnosCurso(cursoId)
    },

    agregarCurso(data) {
      return useCursosStore().agregarCurso(data)
    },

    actualizarCurso(id, data) {
      return useCursosStore().actualizarCurso(id, data)
    },

    async eliminarCurso(id) {
      await useCursosStore().eliminarCurso(id)

      const asignaturasStore = useAsignaturasStore()
      const cursoId = Number(id)
      const { [cursoId]: _, ...resto } = asignaturasStore.asignaturasPorCurso
      asignaturasStore.asignaturasPorCurso = resto
    },

    agregarDocente(data) {
      useDocentesStore().agregarDocente(data)
    },

    actualizarDocente(id, data) {
      useDocentesStore().actualizarDocente(id, data)
    },

    eliminarDocente(id) {
      useDocentesStore().eliminarDocente(id)

      const cursosStore = useCursosStore()
      const docenteId = Number(id)

      cursosStore.cursos = cursosStore.cursos.map((curso) => ({
        ...curso,
        profesorJefeId: curso.profesorJefeId === docenteId ? null : curso.profesorJefeId,
      }))

      cursosStore.persistir()
    },

    agregarProfesor(data) {
      useDocentesStore().agregarProfesor(data)
    },

    actualizarProfesor(id, data) {
      useDocentesStore().actualizarProfesor(id, data)
    },

    eliminarProfesor(id) {
      this.eliminarDocente(id)
    },

    agregarAsignatura(data) {
      useAsignaturasStore().agregarAsignatura(data)
    },

    actualizarAsignatura(id, data) {
      useAsignaturasStore().actualizarAsignatura(id, data)
    },

    eliminarAsignatura(id) {
      useAsignaturasStore().eliminarAsignatura(id)
    },

    asignarProfesorJefe(cursoId, docenteId) {
      return useCursosStore().asignarProfesorJefe(cursoId, docenteId)
    },

    async sincronizarAsignaturasCurso(cursoId) {
      return useAsignaturasStore().sincronizarAsignaturasCurso(cursoId)
    },

    agregarAsignaturaACurso(cursoId, asignaturaId, docenteId, data = {}) {
      return useAsignaturasStore().agregarAsignaturaACurso(
        cursoId,
        asignaturaId,
        docenteId,
        data,
      )
    },

    quitarAsignaturaDeCurso(cursoId, asignaturaId) {
      return useAsignaturasStore().quitarAsignaturaDeCurso(cursoId, asignaturaId)
    },

    cambiarDocenteAsignatura(cursoId, asignaturaId, docenteId) {
      return useAsignaturasStore().cambiarDocenteAsignatura(cursoId, asignaturaId, docenteId)
    },

    cambiarProfesorAsignatura(cursoId, asignaturaId, profesorId) {
      return this.cambiarDocenteAsignatura(cursoId, asignaturaId, profesorId)
    },

    actualizarHorasAsignaturaCurso(cursoId, asignaturaId, horasSemanales) {
      return useAsignaturasStore().actualizarHorasAsignaturaCurso(
        cursoId,
        asignaturaId,
        horasSemanales,
      )
    },

    agregarApoderadoAEstudiante(estudianteId, apoderadoData, relacionData) {
      useAlumnosStore().agregarApoderadoAEstudiante(estudianteId, apoderadoData, relacionData)
    },

    actualizarAntecedenteSalud(estudianteId, data) {
      useAlumnosStore().actualizarAntecedenteSalud(estudianteId, data)
    },

    actualizarAntecedenteFamiliar(estudianteId, data) {
      useAlumnosStore().actualizarAntecedenteFamiliar(estudianteId, data)
    },

    resetData() {
      useEstablecimientoStore().resetData()
      useAlumnosStore().resetData()
      useCursosStore().resetData()
      useDocentesStore().resetData()
      useAsignaturasStore().resetData()
    },
  },
})
