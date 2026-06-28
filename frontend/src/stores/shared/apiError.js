export const extractApiError = (error, fallback = 'Ocurrió un error inesperado.') => {
  const data = error?.response?.data

  if (typeof data === 'string' && data.trim()) {
    return data.trim()
  }

  if (data?.mensaje) return data.mensaje
  if (data?.message) return data.message

  if (error?.message) return error.message

  return fallback
}
