import { defineStore } from 'pinia'
import { establecimientoMock } from '../data'
import { loadFromStorage, saveToStorage } from './shared/persistence'
import { getEstablecimientoId } from './shared/helpers'

export const useEstablecimientoStore = defineStore('establecimiento', {
  state: () => ({
    establecimiento: loadFromStorage('establecimiento', establecimientoMock),
  }),

  getters: {
    establecimientoActivo: (state) => {
      const establecimientoId = Number(getEstablecimientoId())

      if (!establecimientoId) return state.establecimiento

      if (Array.isArray(state.establecimiento)) {
        return (
          state.establecimiento.find((item) => {
            const itemId = Number(item.id_establecimiento ?? item.establecimientoId ?? item.id)
            return itemId === establecimientoId
          }) || null
        )
      }

      const itemId = Number(
        state.establecimiento.id_establecimiento ??
          state.establecimiento.establecimientoId ??
          state.establecimiento.id,
      )

      return itemId === establecimientoId ? state.establecimiento : null
    },

    getEstablecimientoById: (state) => {
      return (id) => {
        const idNumber = Number(id)

        if (Array.isArray(state.establecimiento)) {
          return (
            state.establecimiento.find((item) => {
              const itemId = Number(item.id_establecimiento ?? item.establecimientoId ?? item.id)
              return itemId === idNumber
            }) || null
          )
        }

        const establecimientoId = Number(
          state.establecimiento.id_establecimiento ??
            state.establecimiento.establecimientoId ??
            state.establecimiento.id,
        )

        return establecimientoId === idNumber ? state.establecimiento : null
      }
    },
  },

  actions: {
    persistir() {
      saveToStorage('establecimiento', this.establecimiento)
    },

    actualizarEstablecimiento(data) {
      const establecimientoId = Number(
        data.id_establecimiento ?? data.establecimientoId ?? data.id ?? getEstablecimientoId(),
      )

      if (Array.isArray(this.establecimiento)) {
        const index = this.establecimiento.findIndex((item) => {
          const itemId = Number(item.id_establecimiento ?? item.establecimientoId ?? item.id)
          return itemId === establecimientoId
        })

        if (index === -1) return

        this.establecimiento[index] = {
          ...this.establecimiento[index],
          ...data,
          id_establecimiento: establecimientoId,
        }
      } else {
        this.establecimiento = {
          ...this.establecimiento,
          ...data,
          id_establecimiento: establecimientoId,
        }
      }

      this.persistir()
    },

    resetData() {
      this.establecimiento = Array.isArray(establecimientoMock)
        ? establecimientoMock.map((item) => ({ ...item }))
        : { ...establecimientoMock }

      this.persistir()
    },
  },
})
