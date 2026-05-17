export const loadFromStorage = (key, fallback) => {
  const data = localStorage.getItem(key)

  try {
    return data ? JSON.parse(data) : fallback
  } catch (error) {
    console.warn(`Error leyendo ${key} desde localStorage`, error)
    return fallback
  }
}

export const saveToStorage = (key, data) => {
  localStorage.setItem(key, JSON.stringify(data))
}
