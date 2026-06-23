import { defineStore } from 'pinia'
import api from '@/api/axios'
import { categoriasSnedMock } from '../data'
import { loadFromStorage, saveToStorage } from './shared/persistence'
import { getEstablecimientoId, getDocenteId, getNombreCompleto } from './shared/helpers'
import { mapDocenteFromApi } from './shared/docenteMapper'

export const useDocentesStore = defineStore('docentes', {
  state: () => ({
    docentes: [],
    cargando: false,
    categoriasSned: loadFromStorage('categoriasSned', categoriasSnedMock),
  }),

  getters: {
    docentesFiltrados: (state) => {
      const establecimientoId = getEstablecimientoId()

      return state.docentes.filter(
        (docente) =>
          Number(docente.id_establecimiento ?? docente.establecimientoId) ===
          Number(establecimientoId),
      )
    },

    profesoresFiltrados() {
      return this.docentesFiltrados
    },

    docentesNormalizados: (state) => {
      return state.docentes.map((docente) => ({
        ...docente,
        id: getDocenteId(docente),
        id_docente: getDocenteId(docente),
        nombreCompleto: getNombreCompleto(docente),
        id_establecimiento: docente.id_establecimiento ?? docente.establecimientoId,
        establecimientoId: docente.id_establecimiento ?? docente.establecimientoId,
        estado: docente.estado || 'Activo',
      }))
    },

    docentesFiltradosNormalizados() {
      const establecimientoId = getEstablecimientoId()

      return this.docentesNormalizados.filter(
        (docente) => Number(docente.id_establecimiento) === Number(establecimientoId),
      )
    },

    categoriasSnedActivas: (state) => {
      return state.categoriasSned
    },

    getDocenteById: (state) => {
      return (id) => {
        const docenteId = Number(id)

        return state.docentes.find((docente) => getDocenteId(docente) === docenteId)
      }
    },

    getProfesorById() {
      return this.getDocenteById
    },

    getCategoriaSnedById: (state) => {
      return (id) => {
        const categoriaId = Number(id)

        return state.categoriasSned.find(
          (categoria) =>
            categoria.id_categoria_sned === categoriaId || categoria.id === categoriaId,
        )
      }
    },
  },

  actions: {
    async cargarDocentes() {
      const idEstablecimiento =
        getEstablecimientoId() ??
        JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')?.idEstablecimiento

      if (!idEstablecimiento) {
        throw new Error('No se encontró el establecimiento activo.')
      }

      this.cargando = true
      try {
        const { data } = await api.get(`/establecimiento/${idEstablecimiento}/docentes`)
        this.docentes = data.map(mapDocenteFromApi)
        localStorage.removeItem('docentes')
      } catch (error) {
        this.docentes = []
        throw error
      } finally {
        this.cargando = false
      }
    },

    persistir() {
      saveToStorage('categoriasSned', this.categoriasSned)
    },

    agregarDocente(data) {
      const establecimientoId = getEstablecimientoId()
      const id = Date.now()

      this.docentes.push({
        id_docente: id,
        id_establecimiento: establecimientoId,
        id,
        establecimientoId,
        id_usuario: data.id_usuario || null,
        id_asignatura: data.id_asignatura || null,
        id_categoria_sned: data.id_categoria_sned || null,
        anio_evaluacion_sned: data.anio_evaluacion_sned || null,
        rut: data.rut || '',
        dv: data.dv || '',
        nombres: data.nombres || '',
        apellido_paterno: data.apellido_paterno || '',
        apellido_materno: data.apellido_materno || '',
        fecha_nacimiento: data.fecha_nacimiento || '',
        correo_electronico: data.correo_electronico || '',
        telefono: data.telefono || '',
        calle: data.calle || '',
        numero: data.numero || '',
        id_comuna: data.id_comuna || null,
        fecha_contratacion: data.fecha_contratacion || '',
        estado: data.estado || 'Activo',
        ...data,
      })
    },

    actualizarDocente(id, data) {
      const docenteId = Number(id)
      const index = this.docentes.findIndex((docente) => getDocenteId(docente) === docenteId)

      if (index === -1) return

      this.docentes[index] = {
        ...this.docentes[index],
        ...data,
      }
    },

    eliminarDocente(id) {
      const docenteId = Number(id)
      this.docentes = this.docentes.filter((docente) => getDocenteId(docente) !== docenteId)
    },

    agregarProfesor(data) {
      this.agregarDocente(data)
    },

    actualizarProfesor(id, data) {
      this.actualizarDocente(id, data)
    },

    eliminarProfesor(id) {
      this.eliminarDocente(id)
    },

    resetData() {
      this.docentes = []
      this.categoriasSned = [...categoriasSnedMock]
      this.persistir()
    },
  },
})
