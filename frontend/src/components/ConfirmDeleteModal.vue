<template>
  <div
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-labelledby="confirmDeleteModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content rounded-4 border-0">
        <div class="modal-header">
          <h5 id="confirmDeleteModalLabel" class="modal-title">
            {{ title }}
          </h5>
          <button
            type="button"
            class="btn-close"
            aria-label="Cerrar"
            :disabled="loading"
            @click="cerrar"
          ></button>
        </div>

        <div class="modal-body">
          <p class="mb-2">{{ message }}</p>

          <div v-if="itemLabel || $slots.default" class="alert alert-light border mb-0">
            <strong v-if="itemLabel">{{ itemLabel }}</strong>
            <slot />
          </div>

          <p v-if="detail" class="text-muted small mt-3 mb-0">
            {{ detail }}
          </p>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-outline-secondary rounded-pill"
            :disabled="loading"
            @click="cerrar"
          >
            Cancelar
          </button>

          <button
            type="button"
            class="btn btn-danger rounded-pill"
            :disabled="loading"
            @click="$emit('confirm')"
          >
            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
            Eliminar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Modal } from 'bootstrap'
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: 'Confirmar eliminación',
  },
  message: {
    type: String,
    required: true,
  },
  itemLabel: {
    type: String,
    default: '',
  },
  detail: {
    type: String,
    default: '',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'confirm'])

const modalRef = ref(null)
let modalInstance = null

const onHidden = () => {
  emit('update:modelValue', false)
}

const cerrar = () => {
  emit('update:modelValue', false)
}

onMounted(() => {
  modalInstance = new Modal(modalRef.value)
  modalRef.value.addEventListener('hidden.bs.modal', onHidden)
})

onBeforeUnmount(() => {
  modalRef.value?.removeEventListener('hidden.bs.modal', onHidden)
  modalInstance?.dispose()
})

watch(
  () => props.modelValue,
  (visible) => {
    if (!modalInstance) return

    if (visible) {
      modalInstance.show()
      return
    }

    Modal.getInstance(modalRef.value)?.hide()
  },
)
</script>
