import { defineStore } from 'pinia'

let toastId = 0

export const useToastStore = defineStore('toast', {
  state: () => ({
    toasts: [],
  }),

  actions: {
    show(message, options = {}) {
      const id = ++toastId
      const duration = options.duration ?? 3500

      this.toasts.push({
        id,
        message,
        variant: options.variant ?? 'success',
      })

      window.setTimeout(() => {
        this.remove(id)
      }, duration)
    },

    remove(id) {
      this.toasts = this.toasts.filter((toast) => toast.id !== id)
    },
  },
})
