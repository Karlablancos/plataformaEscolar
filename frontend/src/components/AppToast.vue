<script setup>
import { useToastStore } from '@/stores/toastStore'

const toastStore = useToastStore()
</script>

<template>
  <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1080">
    <div
      v-for="toast in toastStore.toasts"
      :key="toast.id"
      class="toast show align-items-center text-bg-success border-0 mb-2"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
    >
      <div class="d-flex">
        <div class="toast-body">{{ toast.message }}</div>
        <button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          aria-label="Cerrar"
          @click="toastStore.remove(toast.id)"
        />
      </div>
    </div>
  </div>
</template>
