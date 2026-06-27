// src/stores/authStore.js

import { defineStore } from 'pinia'
import api from '@/api/axios'

const getStoredUser = () => {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null')
  } catch {
    return null
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    user: getStoredUser(),
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,

    isAdmin: (state) => {
      return state.user?.rol === 'ADMINISTRADOR'
    },

    isDirector: (state) => {
      return state.user?.rol === 'DIRECTOR'
    },

    isProfesor: (state) => {
      return state.user?.rol === 'DOCENTE'
    },

    establecimientoId: (state) => {
      return state.user?.establecimientoId || state.user?.idEstablecimiento || null
    },

    displayName: (state) => {
      return state.user?.username || state.user?.nombre || 'Usuario'
    },
  },

  actions: {
    async login(rbd, username, password) {
      try {
        const rbdLimpio = (rbd || localStorage.getItem('rbd') || '').split('-')[0]

        const { data } = await api.post('/auth/login', {
          rbd: rbdLimpio,
          username,
          password,
        })

        this.token = data.token

        this.user = {
          username: data.username,
          nombre: data.username,
          rol: data.rol,
          nombreColegio: data.nombreColegio,
          establecimientoNombre: data.nombreColegio || data.establecimientoNombre,
          rbd: data.rbd,
          establecimientoId: data.establecimientoId || data.idEstablecimiento || null,
        }

        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))

        if (data.rbd) {
          localStorage.setItem('rbd', data.rbd)
        }

        return true
      } catch (error) {
        console.error('Error login:', error)
        this.token = null
        this.user = null
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        return false
      }
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})