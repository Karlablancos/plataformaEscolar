-- ============================================================
-- POBLADO DE TABLAS: 12 registros por tabla
-- Colegio Bernardo O'Higgins - Sistema de GestiÃ³n Escolar
-- ============================================================

-- ============================================================
-- REGION (12)
-- ============================================================
INSERT INTO region (nombre_region) VALUES ('Metropolitana de Santiago');
INSERT INTO region (nombre_region) VALUES ('ValparaÃ­so');
INSERT INTO region (nombre_region) VALUES ('BiobÃ­o');
INSERT INTO region (nombre_region) VALUES ('La AraucanÃ­a');
INSERT INTO region (nombre_region) VALUES ('O''Higgins');
INSERT INTO region (nombre_region) VALUES ('Maule');
INSERT INTO region (nombre_region) VALUES ('Los Lagos');
INSERT INTO region (nombre_region) VALUES ('Antofagasta');
INSERT INTO region (nombre_region) VALUES ('Atacama');
INSERT INTO region (nombre_region) VALUES ('Coquimbo');
INSERT INTO region (nombre_region) VALUES ('AysÃ©n');
INSERT INTO region (nombre_region) VALUES ('Magallanes');

-- ============================================================
-- COMUNA (12)
-- ============================================================
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Ã‘uÃ±oa', 1);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Santiago Centro', 1);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Providencia', 1);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Los Andes', 2);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('ValparaÃ­so', 2);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('ConcepciÃ³n', 3);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Temuco', 4);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Rancagua', 5);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Talca', 6);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Puerto Montt', 7);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('Antofagasta', 8);
INSERT INTO comuna (nombre_comuna, id_region) VALUES ('La Serena', 10);

-- ============================================================
-- TIPO_ESTABLECIMIENTO (12)
-- ============================================================
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('Municipal', 'PÃºblico', false, 'Decreto 67/2018', 'Establecimiento municipal de enseÃ±anza bÃ¡sica y media');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('Particular Subvencionado', 'Mixto', true, 'Decreto 67/2018', 'Establecimiento particular con subvenciÃ³n estatal');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('Particular Pagado', 'Privado', true, 'Decreto 67/2018', 'Establecimiento particular sin subvenciÃ³n');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('CorporaciÃ³n Municipal', 'PÃºblico', false, 'Decreto 67/2018', 'Administrado por corporaciÃ³n municipal');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('SLEP', 'PÃºblico', false, 'Decreto 67/2018', 'Servicio Local de EducaciÃ³n PÃºblica');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('FundaciÃ³n', 'Mixto', true, 'Decreto 67/2018', 'Administrado por fundaciÃ³n educacional');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('CongregaciÃ³n Religiosa', 'Privado', true, 'Decreto 67/2018', 'Administrado por congregaciÃ³n religiosa');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('Cooperativa', 'Mixto', true, 'Decreto 67/2018', 'Administrado por cooperativa educacional');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('TÃ©cnico Profesional Municipal', 'PÃºblico', false, 'Decreto 67/2018', 'Liceo tÃ©cnico profesional municipal');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('TÃ©cnico Profesional Particular', 'Privado', true, 'Decreto 67/2018', 'Liceo tÃ©cnico profesional particular');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('ArtÃ­stico', 'PÃºblico', false, 'Decreto 67/2018', 'Liceo de orientaciÃ³n artÃ­stica');
INSERT INTO tipo_establecimiento (dependencia, financiamiento, puede_cobrar_mensualidad, normativa_aplicable, descripcion)
VALUES ('Especial', 'PÃºblico', false, 'Decreto 67/2018', 'Establecimiento de educaciÃ³n especial');

-- ============================================================
-- ESTABLECIMIENTO (12)
-- ============================================================
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12345', 'Colegio Bernardo O''Higgins', 1, 'Municipalidad de Santiago', 'Roberto Fuentes Mora', 'Av. Bernardo O''Higgins', '1234', 2, '223456789', 'contacto@bohiggins.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12346', 'Colegio San Eugenio', 2, 'FundaciÃ³n San Eugenio', 'Ana PÃ©rez Rojas', 'Av. IrarrÃ¡zaval', '890', 1, '223456790', 'contacto@seugenio.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12347', 'Colegio Providencia', 3, 'Sociedad Educacional Providencia', 'Carlos MuÃ±oz Silva', 'Av. Providencia', '456', 3, '223456791', 'contacto@providencia.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12348', 'Colegio Los Andes', 1, 'Municipalidad de Los Andes', 'MarÃ­a GonzÃ¡lez LÃ³pez', 'Calle Principal', '100', 4, '223456792', 'contacto@losandes.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12349', 'Liceo de ValparaÃ­so', 1, 'Municipalidad de ValparaÃ­so', 'Pedro Soto Vega', 'Av. Argentina', '234', 5, '223456793', 'contacto@liceovapo.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12350', 'Colegio ConcepciÃ³n', 2, 'FundaciÃ³n ConcepciÃ³n', 'Laura Rivas Torres', 'Calle O''Higgins', '567', 6, '223456794', 'contacto@colegioconce.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12351', 'Liceo Temuco', 1, 'Municipalidad de Temuco', 'Jorge Pino Castro', 'Av. Alemania', '789', 7, '223456795', 'contacto@liceotemucho.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12352', 'Colegio Rancagua', 4, 'CorporaciÃ³n Municipal Rancagua', 'Elena Mora DÃ­az', 'Av. Libertador', '321', 8, '223456796', 'contacto@colegiorancagua.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12353', 'Colegio Talca', 2, 'FundaciÃ³n Talca', 'Ricardo Vega Soto', 'Calle 1 Sur', '432', 9, '223456797', 'contacto@colegioTalca.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12354', 'Liceo Puerto Montt', 1, 'Municipalidad de Puerto Montt', 'Silvia Lagos Ruiz', 'Av. Diego Portales', '654', 10, '223456798', 'contacto@liceoPM.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12355', 'Colegio Antofagasta', 3, 'Sociedad Antofagasta', 'Fernando RÃ­os Pino', 'Av. Balmaceda', '876', 11, '223456799', 'contacto@colegioAnto.cl', 'NORMAL', 'ACTIVO');
INSERT INTO establecimiento (rbd, nombre, id_tipo_estab, sostenedor, director, calle, numero, id_comuna, telefono, correo_electronico, modo_aula, estado)
VALUES ('12356', 'Colegio La Serena', 2, 'FundaciÃ³n La Serena', 'Patricia Araya Cruz', 'Av. Francisco de Aguirre', '987', 12, '223456800', 'contacto@colegioSerena.cl', 'NORMAL', 'ACTIVO');

-- ============================================================
-- CALENDARIO_ESCOLAR (12)
-- ============================================================
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (1, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (2, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (3, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (4, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (5, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (6, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (7, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (8, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (9, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (10, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (11, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');
INSERT INTO calendario_escolar (id_establecimiento, anio, fecha_inicio_clases, fecha_termino_clases, inicio_vacaciones_invierno, fin_vacaciones_invierno, estado)
VALUES (12, 2026, '2026-03-02', '2026-12-18', '2026-07-13', '2026-07-31', 'ACTIVO');

-- ============================================================
-- CALENDARIO_FERIADO (12)
-- ============================================================
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-04-03', 'Viernes Santo');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-05-01', 'DÃ­a del Trabajo');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-05-21', 'Glorias Navales');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-06-29', 'San Pedro y San Pablo');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-07-16', 'Virgen del Carmen');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-08-15', 'AsunciÃ³n de la Virgen');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-09-18', 'Fiestas Patrias');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-09-19', 'DÃ­a de las Glorias del EjÃ©rcito');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-10-12', 'DÃ­a del Encuentro de Dos Mundos');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-10-31', 'DÃ­a de las Iglesias EvangÃ©licas');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-11-01', 'DÃ­a de Todos los Santos');
INSERT INTO calendario_feriado (id_calendario, fecha, descripcion) VALUES (1, '2026-12-08', 'Inmaculada ConcepciÃ³n');

-- ============================================================
-- PERIODO_ACADEMICO (12)
-- ============================================================
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (1, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (1, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (2, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (2, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (3, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (3, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (4, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (4, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (5, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (5, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (6, 2026, 'Primer Semestre', '2026-03-02', '2026-07-10', 'ACTIVO');
INSERT INTO periodo_academico (id_establecimiento, anio, nombre_periodo, fecha_inicio, fecha_termino, estado)
VALUES (6, 2026, 'Segundo Semestre', '2026-08-03', '2026-12-18', 'PENDIENTE');

-- ============================================================
-- ROL (4 - solo existen 4 roles en el sistema)
-- ============================================================
INSERT INTO rol (nombre_rol, descripcion) VALUES ('ADMINISTRADOR', 'Acceso total al sistema');
INSERT INTO rol (nombre_rol, descripcion) VALUES ('DOCENTE', 'GestiÃ³n acadÃ©mica y asistencia');
INSERT INTO rol (nombre_rol, descripcion) VALUES ('ESTUDIANTE', 'Consulta de notas y asistencia');
INSERT INTO rol (nombre_rol, descripcion) VALUES ('APODERADO', 'Consulta de informaciÃ³n de sus hijos');

-- ============================================================
-- USUARIO (12)
-- ============================================================
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 1, 'admin.bohiggins', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 1, 'admin.secundario', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin2@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 2, 'prof.matematica', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'matematica@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 2, 'prof.lenguaje', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'lenguaje@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 2, 'prof.historia', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'historia@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 2, 'prof.edfisica', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'edfisica@bohiggins.cl', 0, false, '2026-01-15T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 3, 'karla.blanco', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'karla.blanco@bohiggins.cl', 0, false, '2026-03-01T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 3, 'katherine.cuestas', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'katherine.cuestas@bohiggins.cl', 0, false, '2026-03-01T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 3, 'daniela.montefinale', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'daniela.montefinale@bohiggins.cl', 0, false, '2026-03-01T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 3, 'jaime.luna', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'jaime.luna@bohiggins.cl', 0, false, '2026-03-01T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 4, 'apod.blanco', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'apod.blanco@gmail.com', 0, false, '2026-03-01T08:00:00', 'ACTIVO');
INSERT INTO usuario (id_establecimiento, id_rol, username, password_hash, correo_electronico, intentos_fallidos, bloqueado, fecha_creacion, estado)
VALUES (1, 4, 'apod.cuestas', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'apod.cuestas@gmail.com', 0, false, '2026-03-01T08:00:00', 'ACTIVO');

-- ============================================================
-- CATEGORIA_SNED (12)
-- ============================================================
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a A+', 'DesempeÃ±o excepcional', 100.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a A', 'DesempeÃ±o sobresaliente', 90.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a B+', 'DesempeÃ±o muy bueno', 80.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a B', 'DesempeÃ±o bueno', 70.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a C+', 'DesempeÃ±o regular alto', 60.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a C', 'DesempeÃ±o regular', 50.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a D+', 'DesempeÃ±o bÃ¡sico alto', 40.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a D', 'DesempeÃ±o bÃ¡sico', 30.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('CategorÃ­a E', 'DesempeÃ±o insuficiente', 10.00, true);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('En evaluaciÃ³n', 'Proceso de evaluaciÃ³n en curso', 0.00, false);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('Sin categorÃ­a', 'No evaluado en este perÃ­odo', 0.00, false);
INSERT INTO categoria_sned (nombre, descripcion, porcentaje_bono, tiene_bono)
VALUES ('Nuevo ingreso', 'Docente con menos de un aÃ±o en el establecimiento', 0.00, false);

-- ============================================================
-- TIPO_CALIFICACION (12)
-- ============================================================
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('NumÃ©rica', '1.0 - 7.0', true, 4.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Conceptual', 'MB, B, S, I', false, 0.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Porcentual', '0% - 100%', false, 60.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('NumÃ©rica Diferenciada', '1.0 - 7.0', true, 3.5);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Literal', 'A, B, C, D, E', false, 0.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('NumÃ©rica BÃ¡sica', '1.0 - 7.0', true, 4.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Descriptiva', 'Logrado, Por lograr, No logrado', false, 0.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('NumÃ©rica PIE', '1.0 - 7.0', true, 3.5);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Formativa', 'Satisfactorio, En proceso, Inicial', false, 0.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('NumÃ©rica Media', '1.0 - 7.0', true, 4.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Conceptual Extendida', 'MB+, MB, B, S, I', false, 0.0);
INSERT INTO tipo_calificacion (nombre, escala, entra_promedio, minimo_aprobacion)
VALUES ('Binaria', 'Aprobado, Reprobado', false, 0.0);

-- ============================================================
-- ASIGNATURA (12)
-- ============================================================
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'MatemÃ¡tica', 'MAT001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'Lenguaje y ComunicaciÃ³n', 'LEN001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'Historia, GeografÃ­a y CS', 'HIS001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'EducaciÃ³n FÃ­sica', 'EDF001', 2, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'Ciencias Naturales', 'CIE001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'InglÃ©s', 'ING001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'Artes Visuales', 'ART001', 2, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'MÃºsica', 'MUS001', 2, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'TecnologÃ­a', 'TEC001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'FilosofÃ­a', 'FIL001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'QuÃ­mica', 'QUI001', 1, 'ACTIVO');
INSERT INTO asignatura (id_establecimiento, nombre, codigo, id_tipo_calificacion, estado) VALUES (1, 'FÃ­sica', 'FIS001', 1, 'ACTIVO');

-- ============================================================
-- PLAN_ESTUDIO (12)
-- ============================================================
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'Media', 1, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'Media', 2, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'Media', 3, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'Media', 4, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'BÃ¡sica', 5, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'BÃ¡sica', 6, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'BÃ¡sica', 7, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (1, 'BÃ¡sica', 8, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (2, 'Media', 1, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (2, 'Media', 2, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (2, 'BÃ¡sica', 7, 2026, 'ACTIVO');
INSERT INTO plan_estudio (id_establecimiento, tipo_ensenanza, nivel, anio, estado)
VALUES (2, 'BÃ¡sica', 8, 2026, 'ACTIVO');

-- ============================================================
-- PLAN_ESTUDIO_ASIGNATURA (12)
-- ============================================================
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 1, 6);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 2, 6);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 3, 4);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 4, 3);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 5, 4);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 6, 3);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 7, 2);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 8, 2);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 9, 2);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 10, 2);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 11, 3);
INSERT INTO plan_estudio_asignatura (id_plan_estudio, id_asignatura, horas_semanales) VALUES (1, 12, 3);

-- ============================================================
-- TIPO_NEE (12)
-- ============================================================
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('DEA', 'Dificultad EspecÃ­fica de Aprendizaje', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TDAH', 'Trastorno por DÃ©ficit de AtenciÃ³n e Hiperactividad', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TEA', 'Trastorno del Espectro Autista', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('DI', 'Discapacidad Intelectual', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('DV', 'Discapacidad Visual', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('DA', 'Discapacidad Auditiva', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('DM', 'Discapacidad Motora', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TL', 'Trastorno del Lenguaje', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TGD', 'Trastorno Generalizado del Desarrollo', true);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('EM', 'Enfermedades o Condiciones MÃ©dicas', false);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TA', 'Trastorno Ansioso', false);
INSERT INTO tipo_nee (nombre, nombre_completo, requiere_pie) VALUES ('TC', 'Trastorno Conductual', false);

-- ============================================================
-- DOCENTE (12) -- usando usuarios del id 3 al 6 (4 docentes)
-- se repiten con distintas asignaturas para llegar a 12
-- ============================================================
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (1, 3, 1, 2024, '12345678', '9', 'Pedro', 'RamÃ­rez', 'Soto', '1980-03-15', 'matematica@bohiggins.cl', '987654321', 'Av. IrarrÃ¡zaval', '100', 1, '2020-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (1, 4, 2, 2024, '23456789', 'K', 'MarÃ­a', 'GonzÃ¡lez', 'PÃ©rez', '1985-07-22', 'lenguaje@bohiggins.cl', '987654322', 'Av. Providencia', '200', 3, '2018-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (1, 5, 1, 2024, '34567890', '2', 'Carlos', 'MuÃ±oz', 'Lagos', '1978-11-10', 'historia@bohiggins.cl', '987654323', 'Calle Nueva', '300', 2, '2019-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (1, 6, 3, 2024, '45678901', '3', 'Ana', 'LÃ³pez', 'Vera', '1990-05-18', 'edfisica@bohiggins.cl', '987654324', 'Pasaje Los Pinos', '45', 1, '2022-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (2, 3, 2, 2024, '56789012', '4', 'Luis', 'Vega', 'Mora', '1975-09-25', 'lvega@seugenio.cl', '987654325', 'Calle Sur', '150', 2, '2017-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (2, 4, 1, 2024, '67890123', '5', 'Sandra', 'Torres', 'RÃ­os', '1983-02-14', 'storres@seugenio.cl', '987654326', 'Av. Norte', '220', 3, '2021-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (3, 5, 3, 2024, '78901234', '6', 'Roberto', 'Pino', 'Castro', '1970-06-30', 'rpino@providencia.cl', '987654327', 'Av. Este', '310', 2, '2015-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (3, 6, 2, 2024, '89012345', '7', 'Patricia', 'Fuentes', 'DÃ­az', '1988-10-08', 'pfuentes@providencia.cl', '987654328', 'Pasaje Central', '55', 3, '2020-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (4, 3, 1, 2024, '90123456', '8', 'Jorge', 'Araya', 'Soto', '1982-04-17', 'jaraya@losandes.cl', '987654329', 'Calle Los Robles', '75', 4, '2019-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (4, 4, 4, 2024, '01234567', '9', 'Carmen', 'Silva', 'Vera', '1977-08-22', 'csilva@losandes.cl', '987654330', 'Av. Los Pinos', '180', 4, '2016-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (5, 5, 2, 2024, '11223344', '5', 'Miguel', 'Castro', 'Rojas', '1986-12-03', 'mcastro@liceovapo.cl', '987654331', 'Calle Marina', '90', 5, '2021-03-01', 'ACTIVO');
INSERT INTO docente (id_establecimiento, id_usuario, id_categoria_sned, anio_evaluacion_sned, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, fecha_contratacion, estado)
VALUES (5, 6, 3, 2024, '22334455', '6', 'VerÃ³nica', 'Morales', 'Lagos', '1991-03-28', 'vmorales@liceovapo.cl', '987654332', 'Pasaje Cerro', '33', 5, '2023-03-01', 'ACTIVO');

-- ============================================================
-- ESTUDIANTE (12) -- 4 del grupo + 8 generados
-- ============================================================
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 7, '19876543', '2', 'Karla', 'Blanco', 'Salazar', '1997-04-10', 'karla.blanco@bohiggins.cl', '912345671', 'San Eugenio', '40', 1, 'Colegio Anterior Santiago', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 8, '20987654', '3', 'Katherine', 'Cuestas', 'Blanco', '1999-08-15', 'katherine.cuestas@bohiggins.cl', '912345672', 'CautÃ­n', '1319', 2, 'Colegio Anterior Santiago', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 9, '21098765', '4', 'Daniela', 'Montefinale', 'Rojas', '2001-02-20', 'daniela.montefinale@bohiggins.cl', '912345673', 'Carlos AntÃºnez', '333', 3, 'Colegio Anterior Providencia', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 10, '11234567', '8', 'Jaime', 'Luna', 'AlarcÃ³n', '1989-12-05', 'jaime.luna@bohiggins.cl', '912345674', 'Los Andes', '500', 4, 'Colegio Anterior Los Andes', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 7, '31234567', '1', 'Felipe', 'Rojas', 'Castro', '2006-05-12', 'felipe.rojas@bohiggins.cl', '912345675', 'Av. Matta', '234', 2, 'Colegio San Pedro', '2026-03-01', true, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 8, '41234567', '2', 'Valentina', 'Soto', 'Mora', '2007-09-18', 'valentina.soto@bohiggins.cl', '912345676', 'Pasaje Las Flores', '12', 1, 'Colegio Las Rosas', '2026-03-01', false, true, true, true, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 9, '51234567', '3', 'SebastiÃ¡n', 'MuÃ±oz', 'Vega', '2006-11-30', 'sebastian.munoz@bohiggins.cl', '912345677', 'Calle Los Olmos', '89', 2, 'Colegio El Bosque', '2026-03-01', true, true, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (1, 10, '61234567', '4', 'Camila', 'Torres', 'DÃ­az', '2007-03-05', 'camila.torres@bohiggins.cl', '912345678', 'Av. Santa Rosa', '456', 3, 'Colegio Santa Ana', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (2, 7, '71234567', '5', 'Ignacio', 'PÃ©rez', 'Silva', '2006-07-22', 'ignacio.perez@seugenio.cl', '912345679', 'Calle Larga', '123', 4, 'Colegio Los Andes', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (2, 8, '81234567', '6', 'Isidora', 'Lagos', 'Fuentes', '2007-01-15', 'isidora.lagos@seugenio.cl', '912345680', 'Pasaje Norte', '67', 5, 'Colegio Valpo', '2026-03-01', true, false, true, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (2, 9, '91234567', '7', 'MatÃ­as', 'Araya', 'Rivas', '2006-04-08', 'matias.araya@seugenio.cl', '912345681', 'Av. del Mar', '321', 5, 'Colegio Del Puerto', '2026-03-01', false, false, false, false, 'ACTIVO');
INSERT INTO estudiante (id_establecimiento, id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, correo_electronico, telefono, calle, numero, id_comuna, colegio_procedente, fecha_matricula, prioritario, preferente, tiene_nee, en_pie, estado)
VALUES (2, 10, '10234567', 'K', 'SofÃ­a', 'Vera', 'Morales', '2007-06-19', 'sofia.vera@seugenio.cl', '912345682', 'Calle El Puerto', '78', 5, 'Colegio Cerro', '2026-03-01', false, true, false, false, 'ACTIVO');

-- ============================================================
-- APODERADO (12)
-- ============================================================
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '87654321', '1', 'Rosa', 'Salazar', 'Mora', 'apod.blanco@gmail.com', '912000001', '222000001', 'San Eugenio', '40', 1, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '76543210', '2', 'Luis', 'Cuestas', 'Silva', 'apod.cuestas@gmail.com', '912000002', '222000002', 'CautÃ­n', '1319', 2, 'Padre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '65432109', '3', 'Elena', 'Rojas', 'Castro', 'apod.montefinale@gmail.com', '912000003', '222000003', 'Carlos AntÃºnez', '333', 3, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '54321098', '4', 'Jorge', 'AlarcÃ³n', 'Vega', 'apod.luna@gmail.com', '912000004', '222000004', 'Los Andes', '500', 4, 'Padre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '43210987', '5', 'Carmen', 'Rojas', 'Soto', 'carmen.rojas@gmail.com', '912000005', '222000005', 'Av. Matta', '234', 2, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '32109876', '6', 'Mario', 'Soto', 'Mora', 'mario.soto@gmail.com', '912000006', '222000006', 'Pasaje Las Flores', '12', 1, 'Padre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '21098765', '7', 'Gloria', 'MuÃ±oz', 'Vega', 'gloria.munoz@gmail.com', '912000007', '222000007', 'Calle Los Olmos', '89', 2, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '10987654', '8', 'HÃ©ctor', 'Torres', 'DÃ­az', 'hector.torres@gmail.com', '912000008', '222000008', 'Av. Santa Rosa', '456', 3, 'Padre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '19876540', '9', 'MÃ³nica', 'PÃ©rez', 'Silva', 'monica.perez@gmail.com', '912000009', '222000009', 'Calle Larga', '123', 4, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '29876540', 'K', 'Ricardo', 'Lagos', 'Fuentes', 'ricardo.lagos@gmail.com', '912000010', '222000010', 'Pasaje Norte', '67', 5, 'Padre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (11, '39876540', '1', 'Andrea', 'Araya', 'Rivas', 'andrea.araya@gmail.com', '912000011', '222000011', 'Av. del Mar', '321', 5, 'Madre', '2026-03-01', 'ACTIVO');
INSERT INTO apoderado (id_usuario, rut, dv, nombres, apellido_paterno, apellido_materno, correo_electronico, telefono_principal, telefono_secundario, calle, numero, id_comuna, parentesco, fecha_registro, estado)
VALUES (12, '49876540', '2', 'Felipe', 'Vera', 'Morales', 'felipe.vera@gmail.com', '912000012', '222000012', 'Calle El Puerto', '78', 5, 'Padre', '2026-03-01', 'ACTIVO');

-- ============================================================
-- ESTUDIANTE_APODERADO (12)
-- ============================================================
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (1, 1, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (2, 2, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (3, 3, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (4, 4, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (5, 5, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (6, 6, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (7, 7, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (8, 8, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (9, 9, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (10, 10, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (11, 11, true, '2026-03-01');
INSERT INTO estudiante_apoderado (id_estudiante, id_apoderado, es_apoderado_titular, fecha_asignacion) VALUES (12, 12, true, '2026-03-01');

-- ============================================================
-- CURSO (12)
-- ============================================================
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 1, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 1, 'B', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 2, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 2, 'B', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 3, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 3, 'B', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 4, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (1, 4, 'B', 'Media', 'CientÃ­fico Humanista', 2026, false, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (2, 1, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (2, 1, 'B', 'Media', 'TÃ©cnico Profesional', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (2, 2, 'A', 'Media', 'CientÃ­fico Humanista', 2026, true, 'ACTIVO');
INSERT INTO curso (id_establecimiento, numero, letra, tipo_ensenianza, modalidad, anio_academico, es_nivel_simce, estado) VALUES (2, 2, 'B', 'Media', 'TÃ©cnico Profesional', 2026, false, 'ACTIVO');

-- ============================================================
-- SALA (12)
-- ============================================================
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (101, 'Sala 101', 35, 'Aula', 1, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (102, 'Sala 102', 35, 'Aula', 1, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (103, 'Sala 103', 35, 'Aula', 1, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (104, 'Sala 104', 35, 'Aula', 1, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (201, 'Laboratorio Ciencias', 30, 'Laboratorio', 2, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (202, 'Sala de ComputaciÃ³n', 30, 'Laboratorio', 2, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (203, 'Laboratorio QuÃ­mica', 28, 'Laboratorio', 2, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (204, 'Sala Multimedia', 32, 'Multimedia', 2, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (301, 'Sala 301', 35, 'Aula', 3, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (302, 'Sala 302', 35, 'Aula', 3, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (303, 'Gimnasio', 100, 'Deportiva', 1, 'ACTIVO', 1);
INSERT INTO sala (numero, nombre, capacidad, tipo, piso, estado, id_establecimiento) VALUES (304, 'Biblioteca', 50, 'Biblioteca', 1, 'ACTIVO', 1);

-- ============================================================
-- CURSO_ASIGNATURA (12)
-- ============================================================
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (1, 1, 1, 6, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (1, 2, 2, 6, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (1, 3, 3, 4, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (1, 4, 4, 3, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (2, 1, 1, 6, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (2, 2, 2, 6, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (2, 3, 3, 4, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (2, 4, 4, 3, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (3, 5, 1, 4, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (3, 6, 2, 3, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (4, 7, 3, 2, 'ACTIVO');
INSERT INTO curso_asignatura (id_curso, id_asignatura, id_docente, horas_semanales, estado) VALUES (4, 8, 4, 2, 'ACTIVO');

-- ============================================================
-- HORARIO (12)
-- ============================================================
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 1, 1, 1, 1, 'LUNES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 2, 2, 1, 1, 'LUNES', '09:45', '11:15', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 3, 3, 1, 1, 'MARTES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 4, 4, 5, 1, 'MIÃ‰RCOLES', '10:00', '11:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 5, 1, 5, 1, 'JUEVES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (1, 6, 2, 1, 1, 'VIERNES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (2, 1, 1, 2, 1, 'LUNES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (2, 2, 2, 2, 1, 'MARTES', '09:45', '11:15', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (3, 1, 1, 3, 1, 'LUNES', '11:30', '13:00', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (3, 3, 3, 3, 1, 'MIÃ‰RCOLES', '08:00', '09:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (4, 2, 2, 4, 1, 'JUEVES', '10:00', '11:30', 2026);
INSERT INTO horario (id_curso, id_asignatura, id_docente, id_sala, id_periodo, dia_semana, hora_inicio, hora_termino, anio_academico) VALUES (4, 4, 4, 5, 1, 'VIERNES', '09:45', '11:15', 2026);

-- ============================================================
-- ESTUDIANTE_CURSO (12)
-- ============================================================
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (1, 1, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (2, 1, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (3, 1, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (4, 1, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (5, 2, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (6, 2, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (7, 3, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (8, 3, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (9, 4, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (10, 4, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (11, 1, '2026-03-01', 'ACTIVO');
INSERT INTO estudiante_curso (id_estudiante, id_curso, fecha_matricula, estado) VALUES (12, 2, '2026-03-01', 'ACTIVO');

-- ============================================================
-- TIPO_EVALUACION (12)
-- ============================================================
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Prueba', false, 'EvaluaciÃ³n escrita formal', 30.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Control', true, 'EvaluaciÃ³n corta de contenidos', 15.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Trabajo', true, 'Trabajo de investigaciÃ³n o proyecto', 25.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Examen', false, 'EvaluaciÃ³n final de semestre', 30.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('DisertaciÃ³n', true, 'PresentaciÃ³n oral ante el curso', 20.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Laboratorio', true, 'Informe de trabajo prÃ¡ctico', 15.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Proyecto', false, 'Proyecto de investigaciÃ³n semestral', 25.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Coeficiente 2', false, 'EvaluaciÃ³n con doble ponderaciÃ³n', 40.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Tarea', true, 'Trabajo enviado para el hogar', 10.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('AutoevaluaciÃ³n', true, 'EvaluaciÃ³n del propio estudiante', 10.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('CoevaluaciÃ³n', true, 'EvaluaciÃ³n entre pares', 10.0);
INSERT INTO tipo_evaluacion (nombre, puede_eliminarse, descripcion, peso_sugerido) VALUES ('Portafolio', true, 'ColecciÃ³n de trabajos del semestre', 20.0);

-- ============================================================
-- EVALUACION (12)
-- ============================================================
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 1, 1, 1, 1, 'Prueba 1 - Ãlgebra', 30.0, '2026-04-10');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 2, 2, 2, 1, 'Control 1 - ComprensiÃ³n Lectora', 15.0, '2026-04-12');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 3, 3, 3, 1, 'Trabajo - Independencia de Chile', 25.0, '2026-04-20');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 1, 1, 4, 1, 'Examen Final MatemÃ¡tica', 30.0, '2026-07-05');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 2, 2, 5, 1, 'DisertaciÃ³n - Obra Literaria', 20.0, '2026-05-15');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (1, 5, 1, 6, 1, 'Lab. Ciencias - Ecosistemas', 15.0, '2026-05-20');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (2, 1, 1, 1, 1, 'Prueba 1 MatemÃ¡tica 1B', 30.0, '2026-04-10');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (2, 2, 2, 1, 1, 'Prueba 1 Lenguaje 1B', 30.0, '2026-04-14');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (3, 3, 3, 2, 1, 'Control Historia 2A', 15.0, '2026-04-16');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (3, 1, 1, 7, 1, 'Proyecto MatemÃ¡tico 2A', 25.0, '2026-06-01');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (4, 2, 2, 1, 1, 'Prueba Lenguaje 2B', 30.0, '2026-04-18');
INSERT INTO evaluacion (id_curso, id_asignatura, id_docente, id_tipo_evaluacion, id_periodo, nombre, ponderacion, fecha) VALUES (4, 4, 4, 4, 1, 'Examen Ed. FÃ­sica 2B', 30.0, '2026-07-03');

-- ============================================================
-- NOTA (12) -- con las notas reales del grupo!
-- ============================================================
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (1, 1, 6.0, 'MB', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (2, 1, 7.0, 'MB', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (3, 1, 6.7, 'MB', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (4, 1, 6.0, 'MB', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (1, 2, 5.5, 'B', '2026-04-12', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (2, 2, 6.8, 'MB', '2026-04-12', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (3, 2, 5.9, 'B', '2026-04-12', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (4, 2, 4.5, 'S', '2026-04-12', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (5, 7, 5.0, 'B', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (6, 7, 4.2, 'S', '2026-04-10', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (7, 9, 6.5, 'MB', '2026-04-16', false);
INSERT INTO nota (id_estudiante, id_evaluacion, calificacion, calificacion_conceptual, fecha_registro, evaluacion_diferenciada) VALUES (8, 9, 3.8, 'I', '2026-04-16', false);

-- ============================================================
-- ANOTACION (12)
-- ============================================================
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (1, 1, 'POSITIVA', 'Excelente participaciÃ³n en clases de matemÃ¡tica', '2026-04-05', false, '2026-04-05', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (2, 2, 'POSITIVA', 'Destacada presentaciÃ³n oral en lenguaje', '2026-04-08', false, '2026-04-08', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (3, 3, 'NEGATIVA', 'No entregÃ³ tarea de historia asignada', '2026-04-09', true, '2026-04-15', 'PENDIENTE');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (4, 1, 'POSITIVA', 'Apoya constantemente a sus compaÃ±eros', '2026-04-10', false, '2026-04-10', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (5, 2, 'NEGATIVA', 'Uso de dispositivo mÃ³vil en clases', '2026-04-11', false, '2026-04-11', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (6, 3, 'POSITIVA', 'Obtuvo el mejor puntaje en prueba de historia', '2026-04-12', false, '2026-04-12', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (7, 4, 'POSITIVA', 'Liderazgo positivo en actividades deportivas', '2026-04-13', false, '2026-04-13', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (8, 1, 'NEGATIVA', 'InterrumpiÃ³ reiteradamente la clase', '2026-04-14', true, '2026-04-20', 'PENDIENTE');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (9, 2, 'POSITIVA', 'Excelente trabajo en equipo', '2026-04-15', false, '2026-04-15', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (10, 3, 'USO_MOVIL', 'Uso de dispositivo mÃ³vil segÃºn Modo Aula MINEDUC', '2026-04-16', false, '2026-04-16', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (11, 4, 'POSITIVA', 'AyudÃ³ a compaÃ±ero con dificultades motrices', '2026-04-17', false, '2026-04-17', 'APROBADA');
INSERT INTO anotacion (id_estudiante, id_docente, tipo, descripcion, fecha, requiere_citacion, fecha_citacion, estado) VALUES (12, 1, 'NEGATIVA', 'No trajo materiales requeridos para laboratorio', '2026-04-18', false, '2026-04-18', 'APROBADA');

-- ============================================================
-- ASISTENCIA (12)
-- ============================================================
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (1, 1, '2026-04-07', 'PRESENTE', '08:00', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (2, 1, '2026-04-07', 'PRESENTE', '08:02', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (3, 1, '2026-04-07', 'ATRASADO', '08:15', false, 'LlegÃ³ tarde sin justificaciÃ³n');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (4, 1, '2026-04-07', 'AUSENTE', '00:00', true, 'Certificado mÃ©dico presentado');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (1, 2, '2026-04-07', 'PRESENTE', '09:45', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (2, 2, '2026-04-07', 'PRESENTE', '09:46', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (3, 2, '2026-04-07', 'PRESENTE', '09:45', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (4, 2, '2026-04-07', 'AUSENTE', '00:00', true, 'Certificado mÃ©dico presentado');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (5, 7, '2026-04-07', 'PRESENTE', '08:00', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (6, 7, '2026-04-07', 'ATRASADO', '08:20', false, 'Sin justificaciÃ³n');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (7, 9, '2026-04-08', 'PRESENTE', '11:30', false, 'Sin observaciones');
INSERT INTO asistencia (id_estudiante, id_horario, fecha, estado_asistencia, hora_llegada, justificada, observacion) VALUES (8, 9, '2026-04-08', 'AUSENTE', '00:00', false, 'Ausencia injustificada');

-- ============================================================
-- MENSAJE (12)
-- ============================================================
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (1, 11, 'ReuniÃ³n apoderados mayo', 'Se cita a reuniÃ³n el viernes 15 de mayo a las 19:00 hrs.', '2026-05-01T10:00:00', '2026-05-01T10:00:00', 'COMUNICADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (3, 7, 'Resultado Prueba 1 MatemÃ¡tica', 'Karla, tu nota fue 6.0. Felicitaciones por tu desempeÃ±o.', '2026-04-11T09:00:00', '2026-04-11T09:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (1, 12, 'ReuniÃ³n apoderados mayo', 'Se cita a reuniÃ³n el viernes 15 de mayo a las 19:00 hrs.', '2026-05-01T10:00:00', '2026-05-01T10:00:00', 'COMUNICADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (4, 8, 'Felicitaciones nota mÃ¡xima', 'Katherine, obtuviste la nota mÃ¡xima en la prueba. Excelente trabajo.', '2026-04-11T09:05:00', '2026-04-11T09:05:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (1, 11, 'Actividad extraprogramÃ¡tica', 'Se informa sobre la actividad de ciencias del dÃ­a 20 de mayo.', '2026-05-05T08:00:00', '2026-05-05T08:00:00', 'COMUNICADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (5, 9, 'CitaciÃ³n apoderado', 'Se le cita para conversar sobre el rendimiento acadÃ©mico de su pupilo.', '2026-04-15T10:00:00', '2026-04-15T10:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (3, 7, 'PrÃ³xima evaluaciÃ³n', 'Recuerda que la prueba de Ã¡lgebra es el prÃ³ximo lunes.', '2026-04-20T11:00:00', '2026-04-20T11:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (4, 8, 'Material de estudio', 'Adjunto el material de estudio para el control de lenguaje.', '2026-04-22T09:00:00', '2026-04-22T09:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (1, 12, 'Cierre primer semestre', 'Se informa las fechas de cierre del primer semestre acadÃ©mico.', '2026-06-01T08:00:00', '2026-06-01T08:00:00', 'COMUNICADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (6, 10, 'AnotaciÃ³n registrada', 'Se informa que se registrÃ³ una anotaciÃ³n en el libro de clases.', '2026-04-16T12:00:00', '2026-04-16T12:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (3, 11, 'Consulta nota', 'Apoderado consulta sobre la nota de la Ãºltima evaluaciÃ³n.', '2026-04-25T14:00:00', '2026-04-25T14:00:00', 'PRIVADO', false);
INSERT INTO mensaje (id_remitente, id_destinatario, asunto, cuerpo, fecha_envio, fecha_lectura, tipo, leido) VALUES (1, 12, 'Aviso vacaciones invierno', 'Se informa que las vacaciones de invierno serÃ¡n del 13 al 31 de julio.', '2026-06-15T08:00:00', '2026-06-15T08:00:00', 'COMUNICADO', false);

-- ============================================================
-- MENSAJE_DESTINATARIO (12)
-- ============================================================
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (1, 11, false, '2026-05-01T10:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (2, 7, false, '2026-04-11T09:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (3, 12, false, '2026-05-01T10:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (4, 8, false, '2026-04-11T09:05:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (5, 11, false, '2026-05-05T08:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (6, 9, false, '2026-04-15T10:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (7, 7, false, '2026-04-20T11:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (8, 8, false, '2026-04-22T09:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (9, 12, false, '2026-06-01T08:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (10, 10, false, '2026-04-16T12:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (11, 11, false, '2026-04-25T14:00:00');
INSERT INTO mensaje_destinatario (id_mensaje, id_usuario, leido, fecha_lectura) VALUES (12, 12, false, '2026-06-15T08:00:00');
