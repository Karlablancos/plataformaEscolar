const mapEstadoCursoAsignaturaFromApi = (estado) => {
  if (!estado) return 'Activo'
  const normalizado = String(estado).trim().toUpperCase()
  return normalizado.startsWith('INACTIV') ? 'Inactivo' : 'Activo'
}

export const mapCursoAsignaturaFromApi = (dto) => ({
  id: dto.idCursoAsignatura,
  id_curso_asignatura: dto.idCursoAsignatura,
  cursoId: dto.idCurso,
  id_curso: dto.idCurso,
  asignaturaId: dto.idAsignatura,
  id_asignatura: dto.idAsignatura,
  docenteId: dto.idDocente,
  id_docente: dto.idDocente,
  periodoId: dto.idPeriodo ?? null,
  id_periodo: dto.idPeriodo ?? null,
  nombrePeriodo: dto.nombrePeriodo?.trim() ?? '',
  nombre_periodo: dto.nombrePeriodo?.trim() ?? '',
  asignaturaNombre: dto.asignaturaNombre?.trim() ?? '',
  docenteNombre: dto.docenteNombre?.trim() ?? '',
  horas_semanales: dto.horasSemanales ?? null,
  horasSemanales: dto.horasSemanales ?? null,
  idSala: dto.idSala ?? null,
  id_sala: dto.idSala ?? null,
  salaNombre: dto.salaNombre?.trim() ?? '',
  sala_nombre: dto.salaNombre?.trim() ?? '',
  estado: mapEstadoCursoAsignaturaFromApi(dto.estado),
})
